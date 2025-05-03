package com.davidfreemangames.idleindustrialist;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.HashMap;
import java.util.Objects;

public class MainFactory {

    private static MainFactory instance;
    private Product product;
    private double money;
    private double sumMoneyPerSec;
    private HashMap<String, Technology> upgradeTechs = new HashMap<>();
    private ArrayList<Product> availableProducts = new ArrayList<>();

    private HashMap<String, Technology> availableTechs = new HashMap<>();

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    // Using ScheduledExecutorService for more robust periodic tasks
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Private constructor to enforce Singleton pattern
    private MainFactory(Product prod) {
        initializeProductList();
        initializeTechsList();
        this.money = 0;
        this.product = prod;
        this.sumMoneyPerSec = 0;
        startAutoMoneyGeneration();
    }

    public static synchronized MainFactory getInstance() {
        if (instance == null) {
            Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat,0);
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

    public double getSumMoneyPerSec() {
        return this.sumMoneyPerSec;
    }

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
        Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat,0);
        Product appleProduct = new Product(1, "Apple", 2, R.drawable.apple, 500);
        Product soybeanProduct = new Product(2, "Soybean", 5, R.drawable.soybean, 2000);
        Product coffeeProduct = new Product(3, "Coffee", 10, R.drawable.coffee, 5000);
        Product cottonProduct = new Product(4, "Cotton", 20, R.drawable.cotton, 10000);
        this.availableProducts.add(wheatProduct);
        this.availableProducts.add(appleProduct);
        this.availableProducts.add(soybeanProduct);
        this.availableProducts.add(coffeeProduct);
        this.availableProducts.add(cottonProduct);
    }

    public void initializeTechsList() {
        Technology seedDrill = new Technology(0, "Seed Drill", 0.5, R.drawable.wheat_seed_drill, 1, 0);
        Technology beehivePollination = new Technology(1, "Beehive Pollination", 2, R.drawable.apple_beehive, 2, 0);
        Technology steelPlow = new Technology(2, "Steel Plow", 4, R.drawable.steel_plow, 3, 0);
        Technology terraces = new Technology(3, "Terracing", 10, R.drawable.coffee_terraces, 4, 0);
        Technology cottonGin = new Technology(4, "Cotton Gin", 20, R.drawable.cotton_gin, 5, 0);
        this.availableTechs.put(seedDrill.getName(), seedDrill);
        this.availableTechs.put(beehivePollination.getName(), beehivePollination);
        this.availableTechs.put(steelPlow.getName(), steelPlow);
        this.availableTechs.put(terraces.getName(), terraces);
        this.availableTechs.put(cottonGin.getName(), cottonGin);
    }
}