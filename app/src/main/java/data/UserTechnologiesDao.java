package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.davidfreemangames.idleindustrialist.Technology;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserTechnologiesDao {
    // Inserts new userTech row to table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUserTechnology(UserTechnologies userTech);

    // Gets techIds from table
    @Query("SELECT techId FROM UserTechnologies")
    List<Integer> getTechIds();

    // Gets economy of scales for each tech
    @Query("SELECT techEconomyOfScale FROM UserTechnologies")
    List<Integer> getEconsOfScale();

    // Deletes specific userTech row from table
    @Delete
    void deleteUserTechnology(UserTechnologies userTech);
}
