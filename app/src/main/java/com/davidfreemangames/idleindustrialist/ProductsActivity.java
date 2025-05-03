package com.davidfreemangames.idleindustrialist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Objects;


public class ProductsActivity extends AppCompatActivity implements MainFactory.OnMoneyChangeListener {
    double currentBalance;
    MainFactory mainFactory;
    String currentBalanceText;
    Button backButton;
    TextView moneyTextProducts;
    ScrollView productsScrollView;
    LinearLayout productsScrollLinearLayout;
    View upgradeTemplate;
    ImageView upgradeImage;
    TextView upgradeName;
    TextView upgradeMoneyPerTap;
    TextView productsInstructions;
    Button upgradeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);
        hideSystemUI();
        mainFactory = MainFactory.getInstance();
        mainFactory.setOnMoneyChangeListener(this); // Register as a listener
        productsScrollView = findViewById(R.id.productsScrollView);
        productsScrollLinearLayout = findViewById(R.id.productsScrollLinearLayout);
        ArrayList<Product> products = mainFactory.getAvailableProducts();

        for(int i = 0; i<Objects.requireNonNull(products).size(); i++){
            Product currentProduct = products.get(i);
            upgradeTemplate = LayoutInflater.from(this).inflate(R.layout.upgrade_template, productsScrollLinearLayout, false);
            upgradeImage = upgradeTemplate.findViewById(R.id.upgradeImage);
            upgradeName = upgradeTemplate.findViewById(R.id.upgradeName);
            upgradeMoneyPerTap = upgradeTemplate.findViewById(R.id.upgradePriceText);
            upgradeButton = upgradeTemplate.findViewById(R.id.upgradeButton);

            upgradeImage.setImageResource(currentProduct.getImageFile());
            upgradeName.setText(currentProduct.getName());
            upgradeName.setTextColor(Color.BLACK);
            String productVal = String.valueOf(currentProduct.getMoneyPerTap());
            productVal = "$" + productVal + "/tap";
            upgradeMoneyPerTap.setText(productVal);
            upgradeMoneyPerTap.setTextColor(Color.BLACK);
            if (currentProduct.getIsPurchased()) {
                String purchasedText = "Purchased";
                upgradeButton.setText(purchasedText);
                upgradeButton.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
            } else {
                String prodPrice = String.valueOf(currentProduct.getPrice());
                prodPrice = "$" + prodPrice;
                upgradeButton.setText(prodPrice);
            }
            upgradeButton.setOnClickListener(v -> {
                Button clickedButton = (Button) v;
                // Fill out code to return the values from the product or the
                // Checks to see if user has enough money to purchase product
                // and if the product gives more money per tap than the current one
                if(mainFactory.getMoney() >= currentProduct.getPrice() && currentProduct.getMoneyPerTap() > mainFactory.getProduct().getMoneyPerTap()){
                    mainFactory.purchaseProduct(currentProduct);
                    this.updateBalance();
                    currentProduct.setIsPurchased(true);
                    String purchasedText = "Purchased";
                    clickedButton.setText(purchasedText);
                    clickedButton.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.green));
                }
            });

            currentProduct.setProductTemplate(upgradeTemplate);
            currentProduct.setProductImageView(upgradeImage);
            currentProduct.setProductTextView(upgradeName);
            currentProduct.setProductPriceTextView(upgradeMoneyPerTap);
            currentProduct.setProductUpgradeButton(upgradeButton);

            // Adds complete containerized view back to the container
            productsScrollLinearLayout.addView(upgradeTemplate);
        }
        this.updateScreen();

        // Link the product in mainFactory to the product created in the products list
        for(int i=0; i<products.size(); i++){
            if (mainFactory.getProduct().getName().equals(products.get(i).getName())){
                mainFactory.setProduct(products.get(i));
            }
        }

        // Sets current balance on the screen
        this.updateBalance();
    }

    public void onStart(){
        super.onStart();
        mainFactory = MainFactory.getInstance();

        // Sets money value on the screen
        this.updateBalance();

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(ProductsActivity.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });
    }

    public void onResume(){
        super.onResume();
        updateBalance();
        hideSystemUI();
    }

    public void onDestroy(){
        super.onDestroy();
        mainFactory.setOnMoneyChangeListener(null);
    }

    @Override
    public void onMoneyChanged() {
        // This method is called from MainFactory on the main thread
        updateBalance();
    }

    // Updates balance on screen
    public void updateBalance(){
        currentBalance = mainFactory.getMoney();
        currentBalanceText = "$" + currentBalance;
        moneyTextProducts.setText(currentBalanceText);
    }

    // Updates buttons and text on screen
    public void updateScreen(){
        backButton = findViewById(R.id.productsBackButton);
        moneyTextProducts = findViewById(R.id.moneyTextProducts);
        productsInstructions = findViewById(R.id.productsInstructions);
        String backButtonText = "Back";
        // Finishes applying text to the rest of the views
        backButton.setText(backButtonText);
        String prodInstrText = "Purchase these upgrades to get more money per tap!";
        productsInstructions.setText(prodInstrText);
    }

    // Hides the home, back, and menu buttons on android
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
