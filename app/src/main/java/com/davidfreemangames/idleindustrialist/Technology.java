package com.davidfreemangames.idleindustrialist;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Technology {
    final private int id;

    final private String name;

    private final double baseMoneyPerSecond;

    private double moneyPerSecond;

    final private int imageFile;

    private double price;
    private int economyOfScale; // Multiplier for moneyPerSecond

    private View techTemplate;
    private ImageView techImageView;
    private TextView techTextView;
    private TextView techPriceTextView;
    private Button techUpgradeButton;

    Technology(int id, String name, double baseMoneyPerSec, int imageFile, double price, int econOfScale){
        this.id = id;
        this.name = name;
        this.baseMoneyPerSecond = baseMoneyPerSec;
        this.imageFile = imageFile;
        this.price = price;
        this.economyOfScale = econOfScale;
    }

    public int getId() {return this.id;}

    public String getName() {return  this.name;}

    public double getMoneyPerSecond() {return this.moneyPerSecond;}

    public int getImageFile() {return this.imageFile;}

    public double getPrice() {return this.price;}

    public int getEconomyOfScale() {return this.economyOfScale;}

    public double getBaseMoneyPerSecond() {return this.baseMoneyPerSecond;}

    public void setPrice(double price) {this.price = price;}

    public void setEconomyOfScale(int econOfScale) {this.economyOfScale = econOfScale;}

    public void setTechImageView(ImageView image){
        this.techImageView = image;
    }

    public void setTechTextView(TextView text){
        this.techTextView = text;
    }

    public void setTechPriceTextView(TextView price){
        this.techPriceTextView = price;
    }

    public void setTechUpgradeButton(Button upgradeButton){
        this.techUpgradeButton = upgradeButton;
    }

    public void setTechTemplate(View layoutInflater){
        this.techTemplate = layoutInflater;
    }

    public void upgradeMoneyPerSec(){
        this.economyOfScale += 1;
        this.moneyPerSecond = this.economyOfScale * this.baseMoneyPerSecond;
    }

    public void calculateMoneyPerSec(){
        this.moneyPerSecond = this.economyOfScale * this.baseMoneyPerSecond;
    }

    public void calculatePrice(){
        MainFactory mainFactory = MainFactory.getInstance();
        double newPrice = this.price;
        for(int i=0; i<this.economyOfScale; i++){
            newPrice = mainFactory.techPriceIncrease(newPrice);
        }
    }

}
