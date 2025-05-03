//package data;
//
//import android.content.Context;
//
//import androidx.room.Room;
//
//public class DatabaseHelper {
//    private static volatile MainDatabase.AppDatabase INSTANCE;
//
//    public static MainDatabase.AppDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (MainDatabase.AppDatabase.class) {
//                if (INSTANCE == null) {
//                    String DBName = "IdleIndustrialistDB";
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            MainDatabase.AppDatabase.class, DBName).build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}
