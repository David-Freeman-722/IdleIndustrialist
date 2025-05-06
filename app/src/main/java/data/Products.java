package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Products {
    @PrimaryKey
    public int productId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public double moneyPerTap;

    @ColumnInfo
    public int imageFile;

    @ColumnInfo
    public double price;

    @ColumnInfo
    public boolean isPurchased;
}
