package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.davidfreemangames.idleindustrialist.MainFactory;
import com.davidfreemangames.idleindustrialist.Product;
import com.davidfreemangames.idleindustrialist.Technology;

import java.util.HashMap;

@Entity
public class UserInfo {
    @PrimaryKey
    public int uid;
    public double money;
}
