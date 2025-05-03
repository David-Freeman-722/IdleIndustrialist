package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.davidfreemangames.idleindustrialist.Technology;

@Dao
public interface UserTechnologiesDao {
    // Inserts new userTech row to table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUserTechnology(UserTechnologies userTech);

    // Gets techIds from table
    @Query("SELECT * FROM UserTechnologies")
    public UserTechnologies getTechIds();

    // Deletes specific userTech row from table
    @Delete
    Void deleteUserTechnology(Technology userTech);

    // Gets row count for userTech table
    @Query("SELECT COUNT(*) FROM USERINFO")
    int getNumUserInfoRows();
}
