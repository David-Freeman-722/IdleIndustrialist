package data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "UserTechnologies")
    public class UserTechnologies{
        @PrimaryKey()
        public int techId;
        public int techEconomyOfScale;

    }

