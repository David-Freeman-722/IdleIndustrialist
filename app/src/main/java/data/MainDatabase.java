package data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public class MainDatabase {
    @Database(entities = {UserTechnologies.class, UserProducts.class, UserInfo.class}, version = 2, exportSchema = false)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract UserTechnologiesDao userTechDao();
        public abstract UserProductsDao userProdsDao();
//        public abstract UserInfoDao userInfoDao();
    }
}


