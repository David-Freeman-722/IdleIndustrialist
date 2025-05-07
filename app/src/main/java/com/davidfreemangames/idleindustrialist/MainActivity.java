package com.davidfreemangames.idleindustrialist;
import static com.davidfreemangames.idleindustrialist.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements MainFactory.OnMoneyChangeListener{
    // Declares global use variables for use in app (views, technology and product objects, current money object)

    // Singleton class that holds all of my app data and functionality
    MainFactory mainFactory;

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

        mainFactory = ((MainApplication) getApplicationContext()).getMainFactory();
        mainFactory.initializeDatabase(getApplicationContext());
        mainFactory.setOnMoneyChangeListener(this);

        hideSystemUI();

        productImageButton = findViewById(id.productButton);
        instructions = findViewById(id.instructions);
        moneyText = findViewById(id.moneyText);
        technologiesButton = findViewById(id.technologies);
        productsButton = findViewById(id.products);
    }

    @Override
    public void onStop(){
        super.onStop();
        mainFactory.saveUserData();
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