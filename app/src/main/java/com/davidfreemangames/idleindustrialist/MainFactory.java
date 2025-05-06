package com.davidfreemangames.idleindustrialist;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

import java.util.HashMap;
import java.util.Objects;

import data.MainDatabase;
import data.UserInfo;
import data.UserInfoDao;
import data.UserProducts;
import data.UserProductsDao;
import data.UserTechnologies;
import data.UserTechnologiesDao;

public class MainFactory {

    private static MainFactory instance;

    private MainDatabase.AppDatabase database;
    private ExecutorService databaseExecutor;
    private Handler mainDatabaseHandler;
    private Product product;

    private int userId;
    private double money;
    private double sumMoneyPerSec;

    private List<Integer> databaseProdIds;
    private List<Integer> databaseTechIds;
    private List<Integer> databaseEcondsOfScale;
    private List<Integer> databaseUserIds;
    private List<Double> databaseMoneys;
    private HashMap<String, Technology> upgradeTechs = new HashMap<>();
    private ArrayList<Product> availableProducts = new ArrayList<>();

    private HashMap<String, Technology> availableTechs = new HashMap<>();

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    // Using ScheduledExecutorService for more robust periodic tasks
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Private constructor to enforce Singleton pattern
    private MainFactory(Product prod) {
        databaseExecutor = Executors.newFixedThreadPool(2);
        this.money = 0;
        this.product = prod;
        this.sumMoneyPerSec = 0;
        startAutoMoneyGeneration();
    }

    public static synchronized MainFactory getInstance() {
        if (instance == null) {
            Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat,0, true);
            instance = new MainFactory(wheatProduct);
        }
        return instance;
    }

    private void startAutoMoneyGeneration() {
        scheduler.scheduleWithFixedDelay(() -> {
            double moneyToAdd = this.getSumMoneyPerSec();
            this.money += moneyToAdd;
            // Post a Runnable to the main thread to notify UI of the update
            notifyMoneyUpdated();
        }, 1, 1, TimeUnit.SECONDS); // Run every 1 second
    }

    // Method to notify Activities about money updates using a callback
    private void notifyMoneyUpdated() {
        // Callback for activities to listen to when money has changed
        if (onMoneyChangeListener != null) {
            mainThreadHandler.post(onMoneyChangeListener::onMoneyChanged);
        }
    }

    // Interface for Activities to listen for money updates
    public interface OnMoneyChangeListener {
        void onMoneyChanged();
    }

    private OnMoneyChangeListener onMoneyChangeListener;

    public void setOnMoneyChangeListener(OnMoneyChangeListener listener) {
        this.onMoneyChangeListener = listener;
    }

    public void stopAutoMoneyGeneration() {
        scheduler.shutdown();
    }

    public void tapMoney() {
        double moneyPerTap = this.product.getMoneyPerTap();
        this.money += moneyPerTap;
    }

    public void autoMoneyIncrease(double autoMoneyIncrease) {
        this.money += autoMoneyIncrease;
    }

    public Product getProduct() {
        return this.product;
    }

    public double getMoney() {
        return this.money;
    }

    public int getUserId() {return this.userId;}

    public double getSumMoneyPerSec() {
        return this.sumMoneyPerSec;
    }

    public ArrayList<Product> getAvailableProducts() {return this.availableProducts;}

    public HashMap<String, Technology> getAvailableTechs() {return this.availableTechs;}

    public HashMap<String, Technology> getUpgradeTechs() {
        return this.upgradeTechs;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void purchaseProduct(Product product) {
        this.money -= product.getPrice();
        this.setProduct(product);
    }

    public double purchaseTechUpgrade(Technology tech) {
        double techPrice = tech.getPrice();
        this.money -= techPrice;
        String tech_name = tech.getName();
        if (this.upgradeTechs.containsKey(tech_name)) {
            Objects.requireNonNull(upgradeTechs.get(tech_name)).upgradeMoneyPerSec();
        } else {
            this.upgradeTechs.put(tech_name, tech);
            Objects.requireNonNull(upgradeTechs.get(tech_name)).upgradeMoneyPerSec();
        }
        this.calcMoneyPerSec();
        // Change price using quadratic function (make sure to update price in app too)
        double newTechPrice = techPriceIncrease(techPrice);
        tech.setPrice(newTechPrice); // Scales price with new income
        return newTechPrice;
    }

    public void calcMoneyPerSec() {
        double moneyPerSec = 0;
        for (HashMap.Entry<String, Technology> entry : this.upgradeTechs.entrySet()) {
            moneyPerSec += entry.getValue().getMoneyPerSecond();
        }
        this.sumMoneyPerSec = moneyPerSec;
    }

    public double techPriceIncrease(double price) {
        // Quadratic function to increase price for autoscaling
        return (1 + price) * price;
    }

    public void initializeProductList() {
        // Declares the product and technology objects and places them in arrays
        Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat, 0, true);
        Product appleProduct = new Product(1, "Apple", 2, R.drawable.apple, 500, false);
        Product soybeanProduct = new Product(2, "Soybean", 5, R.drawable.soybean, 2000, false);
        Product coffeeProduct = new Product(3, "Coffee", 10, R.drawable.coffee, 5000, false);
        Product cottonProduct = new Product(4, "Cotton", 20, R.drawable.cotton, 10000, false);
        this.availableProducts.add(wheatProduct);
        this.availableProducts.add(appleProduct);
        this.availableProducts.add(soybeanProduct);
        this.availableProducts.add(coffeeProduct);
        this.availableProducts.add(cottonProduct);

        // If there is a table for Products in database, select which ones are purchased
        if (databaseProdIds != null){
            // Loops through available products
            for (Product prod : availableProducts) {
                for (int id : databaseProdIds) {
                    if (prod.getId() == id) {
                        prod.setIsPurchased(true);
                    }
                }
            }
        }
    }

    public void initializeTechsList() {
        Technology seedDrill = new Technology(0, "Seed Drill", 0.5, R.drawable.wheat_seed_drill, 100, 0);
        Technology beehivePollination = new Technology(1, "Beehive Pollination", 2, R.drawable.apple_beehive, 200, 0);
        Technology steelPlow = new Technology(2, "Steel Plow", 4, R.drawable.steel_plow, 300, 0);
        Technology terraces = new Technology(3, "Terracing", 10, R.drawable.coffee_terraces, 400, 0);
        Technology cottonGin = new Technology(4, "Cotton Gin", 20, R.drawable.cotton_gin, 500, 0);
        this.availableTechs.put(seedDrill.getName(), seedDrill);
        this.availableTechs.put(beehivePollination.getName(), beehivePollination);
        this.availableTechs.put(steelPlow.getName(), steelPlow);
        this.availableTechs.put(terraces.getName(), terraces);
        this.availableTechs.put(cottonGin.getName(), cottonGin);

        if(databaseTechIds != null){
            // Loops through available techs
            for(Technology tech: availableTechs.values()){
                for(int i=0; i<databaseTechIds.size(); i++){
                    int id = databaseTechIds.get(i);
                    int econOfScale = databaseEcondsOfScale.get(i);
                    if(tech.getId() == id){
                        tech.setEconomyOfScale(econOfScale);
                        tech.calculateMoneyPerSec();
                        tech.calculatePrice();
                    }
                }
            }
        }
        // Recalculates money per second
        calcMoneyPerSec();
    }

    public void initializeUserInfo() {
        if(databaseUserIds != null){
            userId = databaseUserIds.get(0); // Will only have 1 user so grabs first ID
            money = databaseMoneys.get(0);
        }
    }

    public void initializeDatabase(Context context) {
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.AppDatabase.class, "idle_database").fallbackToDestructiveMigration().build();
        }
    }

    public MainDatabase.AppDatabase getDatabase() {
        if(database == null){
            throw new IllegalStateException("Database has not been initialized. Call initializeDatabase(Context) first.");
        }
        return database;
    }

    // Method to get techIds and economies of scale from the database
    public void fetchUserTechnologiesInfo() {
        databaseExecutor.execute(() -> {
            UserTechnologiesDao userTechDao = database.userTechDao();
            databaseTechIds = userTechDao.getTechIds();
            databaseEcondsOfScale = userTechDao.getEconsOfScale();
        });
    }

    // Method to get product Ids in database
    public void fetchUserProductsInfo() {
        databaseExecutor.execute(() -> {
            UserProductsDao userProdDao = database.userProdsDao();
            databaseProdIds = userProdDao.getUserProdIds();
        });

        System.out.println("THIS IS USER PRODUCT IDS");
        System.out.println(databaseProdIds);
    }

//    public void fetchUserInfo() {
//        databaseExecutor.execute(() -> {
//            UserInfoDao userInfoDao = database.userInfoDao();
//            databaseUserIds = userInfoDao.getUserIds();
//            databaseMoneys = userInfoDao.getUserMoney();
//        });
//    }

    public void initializeUserData() {
        fetchUserTechnologiesInfo();
        fetchUserProductsInfo();
//        fetchUserInfo();
        initializeProductList();
        initializeTechsList();
//        initializeUserInfo();
    }

    public void saveUserData() {
        int saveOperations = (int) (availableProducts.stream().filter(Product::getIsPurchased).count() + upgradeTechs.size()); // +1 for UserInfo (if you uncomment it)
        CountDownLatch latch = new CountDownLatch(saveOperations);

        // Saves the products that have been purchased to the database
        for(Product prod: availableProducts){
            if(prod.getIsPurchased()){
                // Creates UserProducts row for purchased product
                UserProducts userProd = new UserProducts();
                userProd.productId = prod.getId();
                saveUserProduct(userProd, latch);
            }
        }
        fetchUserProductsInfo();
        System.out.println("THESE ARE MY DATABASE PRODUCT IDS");
        System.out.println(databaseProdIds);

        // Saves technologies to database
        for(Technology tech: upgradeTechs.values()){
            UserTechnologies userTech = new UserTechnologies();
            userTech.techId = tech.getId();
            userTech.techEconomyOfScale = tech.getEconomyOfScale();
            saveUserTechnology(userTech, latch);
        }

//        // Saves user info to database
//        UserInfo userInfo = new UserInfo();
//        userInfo.money = this.getMoney();
//        userInfo.uid = this.getUserId();
//        saveUserInfo(userInfo);
    }

    // Method to save a UserTechnology (runs on a background thread)
    public void saveUserTechnology(UserTechnologies userTech, CountDownLatch latch) {
        databaseExecutor.execute(() -> {
            UserTechnologiesDao userTechDao = database.userTechDao();
            userTechDao.saveUserTechnology(userTech);
            latch.countDown();
        });
    }

    // Method to save a UserProduct (runs on a background thread)
    public void saveUserProduct(UserProducts userProd, CountDownLatch latch) {
        databaseExecutor.execute(() -> {
            UserProductsDao userProdDao = database.userProdsDao();
            userProdDao.saveUserProduct(userProd);
            latch.countDown();
            System.out.println("ID AFTER SAVING TO DB");
            System.out.println(userProdDao.getUserProdIds());
        });
    }

//    public void saveUserInfo(UserInfo userInfo){
//        databaseExecutor.execute(() -> {
//            UserInfoDao userInfoDao = database.userInfoDao();
//            userInfoDao.saveUserInfo(userInfo);
//        });
//    }

}

