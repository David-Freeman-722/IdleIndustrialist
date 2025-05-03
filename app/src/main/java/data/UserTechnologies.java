package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserTechnologies")
    public class UserTechnologies{
        @PrimaryKey()
        public int techId;

        @ColumnInfo(name = "tech_name")
        public String techName;

        @ColumnInfo(name = "tech_money_per_sec")
        public double techMoneyPerSec;

        @ColumnInfo(name = "tech_economy_of_scale")
        public int techEconomyOfScale;

    }

