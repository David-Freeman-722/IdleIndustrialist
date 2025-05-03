package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.davidfreemangames.idleindustrialist.MainFactory;
import com.davidfreemangames.idleindustrialist.Product;
import com.davidfreemangames.idleindustrialist.Technology;

import java.util.HashMap;

@Entity(tableName = "UserInfo")
    public class UserInfo {
        @PrimaryKey
        public int uid;

        @ColumnInfo(name = "money")
        public double money;

        @ColumnInfo(name = "sum_money_per_sec")
        public double sumMoneyPerSecond;

        @ColumnInfo(name = "product_id")
        public int productId;

//        @ColumnInfo(name = "main_factory")
//        public MainFactory mainFactory;


    }
