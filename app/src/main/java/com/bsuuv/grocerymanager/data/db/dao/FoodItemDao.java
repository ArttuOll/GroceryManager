package com.bsuuv.grocerymanager.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.bsuuv.grocerymanager.data.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;

/**
 * Data-access-object definition for accessing food-items in the {@link FoodItemRoomDatabase}. The
 * actual implementation is generated automatically by the <code>Room Persistence Library</code>.
 *
 * @see FoodItemRoomDatabase
 * @see <a href=https://developer.android.com/topic/libraries/architecture/room>Room Persistence
 * Library</a>
 */
@Dao
public interface FoodItemDao {

  @Insert
  void insert(FoodItemEntity foodItem);

  @Query("SELECT * FROM FoodItemEntity WHERE id = :foodItemId")
  FoodItemEntity get(int foodItemId);

  @Delete
  void delete(FoodItemEntity foodItem);

  @Query("SELECT * FROM FoodItemEntity ORDER BY label ASC")
  LiveData<List<FoodItemEntity>> getAllFoodItems();

  @Update
  void update(FoodItemEntity foodItem);
}
