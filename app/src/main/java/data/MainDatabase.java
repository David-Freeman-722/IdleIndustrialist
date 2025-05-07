package data;
import androidx.room.Database;
import androidx.room.RoomDatabase;

public class MainDatabase {
    @Database(entities = {UserTechnologies.class, UserProducts.class}, version = 8, exportSchema = false)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract UserTechnologiesDao userTechDao();
        public abstract UserProductsDao userProdsDao();
    }
}


