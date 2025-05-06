package data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserProductsDao {
    // Inserts new userTech row to table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUserProduct(UserProducts userProduct);

    // Gets techIds from table
    @Query("SELECT productId FROM UserProducts")
    List<Integer> getUserProdIds();

    // Deletes specific userTech row from table
    @Delete
    Void deleteUserTechnology(UserProducts userProd);
}