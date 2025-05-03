package com.davidfreemangames.idleindustrialist;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class Product implements Parcelable{
    // Primary key ID for product in the SQLite Database
    final private int id;
    // Display name of product
    final private String name;
    // Amount of money per tap the product gives
    final private double moneyPerTap;
    // Filepath to product image to display on screens
    final private int imageFile;
    // Price to purchase the product upgrade
    private double price;
    // Boolean saying whether product has been purchased
    private boolean isPurchased;

    private View productTemplate;
    private ImageView productImageView;
    private TextView productTextView;
    private TextView productPriceTextView;
    private Button productUpgradeButton;



    public Product(int id, String name, double moneyPerTap, int imageFile, double price, Boolean purchased){
        this.id  = id;
        this.name = name;
        this.moneyPerTap = moneyPerTap;
        this.imageFile = imageFile;
        this.price = price;
        this.isPurchased = purchased;
    }


    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        moneyPerTap = in.readDouble();
        imageFile = in.readInt();
        price = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getMoneyPerTap(){
        return this.moneyPerTap;
    }

    public int getImageFile(){
        return this.imageFile;
    }

    public double getPrice(){
        return this.price;
    }

    public boolean getIsPurchased() {return this.isPurchased;}

    public void setIsPurchased(Boolean purchased) {this.isPurchased = purchased;}

    public void setProductTemplate(View layoutInflater){
        this.productTemplate = layoutInflater;
    }

    public void setProductImageView(ImageView productImage){
        this.productImageView = productImage;
    }

    public void setProductTextView(TextView productText){
        this.productTextView = productText;
    }

    public void setProductPriceTextView(TextView productPriceText){
        this.productPriceTextView = productPriceText;
    }

    public void setProductUpgradeButton(Button productUpgradeButton){
        this.productUpgradeButton = productUpgradeButton;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(moneyPerTap);
        dest.writeInt(imageFile);
        dest.writeDouble(price);
    }
}
