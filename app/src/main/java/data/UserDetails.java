package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserDetails {
    @PrimaryKey(autoGenerate = true)
    public int userDetailsId;
    public double money;
}
