package com.davidfreemangames.idleindustrialist;

import static android.content.Intent.getIntent;
import static com.davidfreemangames.idleindustrialist.R.*;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import data.DatabaseHelper;
//import data.MainDatabase;
import data.UserInfo;
//import data.UserInfoDao;
import data.UserTechnologies;
import data.UserTechnologiesDao;

public class MainActivity extends AppCompatActivity implements MainFactory.OnMoneyChangeListener{
    // Declares global use variables for use in app (views, technology and product objects, current money object)

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
//    private MainDatabase.AppDatabase appDatabase;
//    private UserInfoDao userInfoDao;
    private UserTechnologiesDao userTechDao;

    MainFactory mainFactory;

    ArrayList<Product> products;

    Handler autoMoneyGenHandler = new Handler();
    Runnable autoMoneyGenRunnable;

    // Declares the views on main screen
    private ImageButton productImageButton;
    private TextView instructions;
    private TextView moneyText;
    private Button technologiesButton;
    private Button productsButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI();
        mainFactory = MainFactory.getInstance();
        mainFactory.setOnMoneyChangeListener(this);
//        appDatabase = DatabaseHelper.getDatabase(this);
//        userInfoDao = appDatabase.userInfoDao();

        // Check if products table has rows if so add them
        // Add code to take product fields and create those products and add them to
        // the products array list that will be sent
        // Do the same for the technologies array list


//        if(userInfoDao.getNumUserInfoRows() < 1){
//            Product wheatProduct = new Product(0, "Wheat", 1, R.drawable.wheat,0);
//            mainFactory = new MainFactory(wheatProduct);
//        } else {
//            System.out.println("ALL OF THE DATA IN THE DATABASE");
//            System.out.println(userInfoDao.getUserSettings());
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    final UserInfo userInfo = userInfoDao.getUserSettings();
//                    // Handle the result on the main thread
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mainFactory.setProduct(userInfo.productId);
//                        }
//                    });
//                }
//            });
//        }
//        // Creates Gson object
//        Gson gson = new Gson();
//        File mainFactoryFile = new File(getFilesDir(), "mainFactory.json");

        // Checks if the mainFactory file exists


        mainFactory = MainFactory.getInstance();

        productImageButton = findViewById(id.productButton);
        instructions = findViewById(id.instructions);
        moneyText = findViewById(id.moneyText);
        technologiesButton = findViewById(id.technologies);
        productsButton = findViewById(id.products);
    }

    @Override
    public void onStop(){
        super.onStop();
//        appDatabase = DatabaseHelper.getDatabase(this);
//        userInfoDao = appDatabase.userInfoDao();
//        UserInfo userInfo = new UserInfo();
//
//        userInfo.uid = 0;
//        userInfo.money = mainFactory.getMoney();
//        userInfo.sumMoneyPerSecond = mainFactory.getSumMoneyPerSec();
//        userInfo.productId = mainFactory.getProduct().getId();
//
//        HashMap<String, Technology> upgradeTechs = mainFactory.getUpgradeTechs();
//
//
//        for(HashMap.Entry<String, Technology> entry: upgradeTechs.entrySet()){
//            UserTechnologies userTechs = new UserTechnologies();
//            Technology tech = entry.getValue();
//            userTechs.techId = tech.getId();
//            userTechs.techEconomyOfScale = tech.getEconomyOfScale();
//            userTechs.techName = tech.getName();
//            userTechs.techMoneyPerSec = tech.getMoneyPerSecond();
//            new Thread(() -> userTechDao.saveUserTechnology(userTechs)).start();
//        }

//        new Thread(() -> userInfoDao.saveUserInfo(userInfo)).start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mainFactory.setOnMoneyChangeListener(null);
    }

    @Override
    public void onMoneyChanged() {
        // This method is called from MainFactory on the main thread
        updateScreen();
    }

    @Override
    public void onStart(){
        super.onStart();
        updateScreen();
    }

    @Override
    public void onResume(){
        super.onResume();
        hideSystemUI();
        updateScreen(); // Updates screen with updated info

        // Updates the screen with updated info
        this.updateScreen();

        // Increases money based on taps
        productImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFactory.tapMoney();
                double currentMoney = mainFactory.getMoney();
                String moneyBalance =  "$" + currentMoney;
                moneyText.setText(moneyBalance);
            }
        });

        technologiesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goToTechnologiesActivity = new Intent(MainActivity.this, TechnologiesActivity.class);
                startActivity(goToTechnologiesActivity);
            }
        });

        productsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent goToProductsActivity = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(goToProductsActivity);
            }
        });
    }

    public void updateScreen(){
        productsButton = findViewById(id.products);
        // Sets money balance on the screen
        String startMoneyBalance = "$" + mainFactory.getMoney();
        moneyText.setText(startMoneyBalance);

        // Sets the instructions on the home page to tell the user to tap the screen
        // to earn more money
        String directions = "Tap on the product to earn money!";
        instructions.setText(directions);

        // Sets the image for the product that is currently on the screen
        productImageButton.setImageResource(mainFactory.getProduct().getImageFile());

        // Sets the values of the technologies and products buttons to their names
        String productsName = "Products";
        String techsName = "Technologies";
        technologiesButton.setText(techsName);
        productsButton.setText(productsName);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}