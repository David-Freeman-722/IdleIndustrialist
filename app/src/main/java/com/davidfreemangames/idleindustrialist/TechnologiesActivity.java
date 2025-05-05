package com.davidfreemangames.idleindustrialist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class TechnologiesActivity extends AppCompatActivity implements MainFactory.OnMoneyChangeListener{
    double currentBalance;
    String currentBalanceText;
    MainFactory mainFactory;
    Button backButton;
    TextView moneyTextTechnologies;
    ScrollView techScrollView;
    LinearLayout techScrollLinearLayout;
    View upgradeTemplate;
    ImageView upgradeImage;
    TextView upgradeName;
    TextView upgradeMoneyPerSec;
    TextView techInstructions;
    Button upgradeButton;

//    Technology seedDrill = new Technology(0, "Seed Drill", 0.5, R.drawable.wheat_seed_drill, 1, 0);
//    Technology beehivePollination = new Technology(1, "Beehive Pollination", 2, R.drawable.apple_beehive, 2, 0);
//    Technology steelPlow = new Technology(2, "Steel Plow", 4, R.drawable.steel_plow, 3, 0);
//    Technology terraces = new Technology(3, "Terracing", 10, R.drawable.coffee_terraces, 4, 0);
//    Technology cottonGin = new Technology(4, "Cotton Gin", 20, R.drawable.cotton_gin, 5, 0);
//
//    ArrayList<Technology> technologies = new ArrayList<>();




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_technologies);
        hideSystemUI();
        mainFactory = MainFactory.getInstance();
        mainFactory.setOnMoneyChangeListener(this);

        techScrollView =  findViewById(R.id.technologiesScrollView);
        techScrollLinearLayout = findViewById(R.id.technologiesScrollLinearLayout);
        ArrayList<Technology> technologies = new ArrayList<>(mainFactory.getAvailableTechs().values());

        for(int i = 0; i< Objects.requireNonNull(technologies).size(); i++) {
            Technology currentTech = technologies.get(i);
            upgradeTemplate = LayoutInflater.from(this).inflate(R.layout.upgrade_template, techScrollLinearLayout, false);
            upgradeImage = upgradeTemplate.findViewById(R.id.upgradeImage);
            upgradeName = upgradeTemplate.findViewById(R.id.upgradeName);
            upgradeMoneyPerSec = upgradeTemplate.findViewById(R.id.upgradePriceText);
            upgradeButton = upgradeTemplate.findViewById(R.id.upgradeButton);

            upgradeImage.setImageResource(currentTech.getImageFile());
            upgradeName.setText(currentTech.getName());
            upgradeName.setTextColor(Color.BLACK);
            String techMoneyPerSecond = String.valueOf(currentTech.getBaseMoneyPerSecond());
            techMoneyPerSecond = "$" + techMoneyPerSecond + "/sec";
            upgradeMoneyPerSec.setText(techMoneyPerSecond);
            upgradeMoneyPerSec.setTextColor(Color.BLACK);
            String techPrice = String.valueOf(currentTech.getPrice());
            techPrice = "$" + techPrice;
            upgradeButton.setText(techPrice);
            upgradeButton.setOnClickListener(v -> {
                Button clickedButton = (Button) v;
                // Fill out code to return the values from the product or the
                // Checks to see if user has enough money to purchase product
                if(mainFactory.getMoney() >= currentTech.getPrice()){
                    double newTechPrice = mainFactory.purchaseTechUpgrade(currentTech);
                    String newPrice = "$" + String.valueOf(newTechPrice);
                    clickedButton.setText(newPrice);
                    this.updateScreen();

                }
            });
            currentTech.setTechTemplate(upgradeTemplate);
            currentTech.setTechImageView(upgradeImage);
            currentTech.setTechTextView(upgradeName);
            currentTech.setTechPriceTextView(upgradeMoneyPerSec);
            currentTech.setTechUpgradeButton(upgradeButton);

            techScrollLinearLayout.addView(upgradeTemplate);
        }

        backButton = findViewById(R.id.technologiesBackButton);
        moneyTextTechnologies = findViewById(R.id.moneyTextTechnologies);
        String backButtonText = "Back";
        // Finishes applying text to the rest of the views
        backButton.setText(backButtonText);

        techInstructions = findViewById(R.id.techInstructions);
        String techInstr = "Buy these upgrades for passive income.";
        techInstructions.setText(techInstr);
        techInstructions.setTextColor(Color.BLACK);

        // Link the tech in mainFactory to the tech created in the products list
        for(int i=0; i<technologies.size(); i++){
            if(mainFactory.getUpgradeTechs().containsKey(technologies.get(i))){
                // Sets tech object in mainFactory to be the one described in this file
                mainFactory.getUpgradeTechs().put(technologies.get(i).getName(), technologies.get(i));
            }
        }

        // Updates the balance on the screen
        this.updateScreen();
    }

    public void onStart(){
        super.onStart();

        // Sets money value on the screen
        this.updateScreen();
//        this.updateTechMonPerSec();

    }

    public void onResume(){
        super.onResume();
        hideSystemUI();
        updateScreen();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(TechnologiesActivity.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });
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

    public void updateScreen(){
        currentBalance = mainFactory.getMoney();
        currentBalanceText = "$" + currentBalance;
        moneyTextTechnologies.setText(currentBalanceText);
    }

    public void updateTechPrice(String price) {
        upgradeButton.setText(price);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
