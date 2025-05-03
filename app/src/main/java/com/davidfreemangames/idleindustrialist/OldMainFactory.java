package com.davidfreemangames.idleindustrialist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Objects;

public class OldMainFactory implements Parcelable {
    private Product product;
    // Add functionality for upgrades (make them objects as well)
//    private ArrayList<Upgrades> upgrades;
    private double money;

    private double sumMoneyPerSec;

    private HashMap<String, Technology> upgradeTechs = new HashMap<>();

    // Constructor for if the product is provided
    OldMainFactory(Product product){
        this.money = 0;
        this.product = product;
    }

    protected OldMainFactory(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        money = in.readDouble();
    }

    public static final Creator<OldMainFactory> CREATOR = new Creator<OldMainFactory>() {
        @Override
        public OldMainFactory createFromParcel(Parcel in) {
            return new OldMainFactory(in);
        }

        @Override
        public OldMainFactory[] newArray(int size) {
            return new OldMainFactory[size];
        }
    };

    // TapMoney adds the correct tapvalue of money to the mainfactory balance
    public void tapMoney(){
        double moneyPerTap = this.product.getMoneyPerTap();
        this.money += moneyPerTap;
    }

    public void autoMoneyIncrease(double autoMoneyIncrease){
        System.out.println("ADDING AUTOMONEY GEN MONEY (BEOFRE)");
        System.out.println(this.money);
        System.out.println("AUTO MONEY INCREASE");
        System.out.println(autoMoneyIncrease);
        this.money += autoMoneyIncrease;
        System.out.println(this.money);
    }

    public Product getProduct(){
        return this.product;
    }

    public double getMoney(){
        return this.money;
    }

    public double getSumMoneyPerSec() {return this.sumMoneyPerSec;}
    public HashMap<String, Technology> getUpgradeTechs(){
        return this.upgradeTechs;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public void purchaseProduct(Product product){
        this.money -= product.getPrice();
        this.setProduct(product);
    }

    public void purchaseTechUpgrade(Technology tech){
        System.out.println("TECHNOLOGY IS BEING PURCHASED");
        this.money -= tech.getPrice();
        String tech_name = tech.getName();
//        System.out.println("This is the tech name " + tech_name);
        if(this.upgradeTechs.containsKey(tech_name)){
            double monpersec = Objects.requireNonNull(this.upgradeTechs.get(tech_name)).getMoneyPerSecond();
            System.out.println("Tech money per second before " + monpersec);
            Objects.requireNonNull(upgradeTechs.get(tech_name)).upgradeMoneyPerSec();
            monpersec = Objects.requireNonNull(this.upgradeTechs.get(tech_name)).getMoneyPerSecond();
            System.out.println("Tech money per second after " + monpersec);
        } else {
            this.upgradeTechs.put(tech_name, tech);
            System.out.println("NEW MONEY PER SEC " + tech.getMoneyPerSecond());
            Objects.requireNonNull(upgradeTechs.get(tech_name)).upgradeMoneyPerSec();
        }
        this.calcMoneyPerSec();
    }

    public void calcMoneyPerSec(){
        double moneyPerSec = 0; // Stores money per second user generates
        for(HashMap.Entry<String, Technology> entry: this.upgradeTechs.entrySet()){
            moneyPerSec += entry.getValue().getMoneyPerSecond();
        }
        this.sumMoneyPerSec = moneyPerSec;
        System.out.println("THIS IS SUM MONEY PER SEC " + this.sumMoneyPerSec);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeDouble(money);
    }
    
}
