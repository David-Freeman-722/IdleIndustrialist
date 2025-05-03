//package data;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.davidfreemangames.idleindustrialist.MainFactory;
//import com.davidfreemangames.idleindustrialist.Product;
//
//import java.util.List;
//
//@Dao
//    public interface UserInfoDao {
//        // Gets the entire user info row
//        @Query("SELECT * FROM UserInfo WHERE uid=0 LIMIT 1")
//        UserInfo getUserSettings();
//        @Query("SELECT main_factory FROM UserInfo WHERE uid LIKE :userInfoId LIMIT 1")
//        MainFactory findMainFactoryById(int userInfoId);
//
//        // Inserts a new row into the UserInfo table and replaces duplicate ID rows
//        @Insert(onConflict = OnConflictStrategy.REPLACE)
//        void saveUserInfo(UserInfo userInfo);
//
//        // Deletes user info row in the user info table
//        @Delete
//        void deleteUserInfo(UserInfo userInfo);
//
//        // Gets count of rows in table
//        @Query("SELECT COUNT(*) FROM USERINFO")
//        int getNumUserInfoRows();
//
//    }
