package data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUserDetails(UserDetails userDetails);

    @Query("SELECT money FROM UserDetails")
    List<Double> getUserMoney();
}
