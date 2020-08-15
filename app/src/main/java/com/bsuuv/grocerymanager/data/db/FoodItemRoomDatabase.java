package com.bsuuv.grocerymanager.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.bsuuv.grocerymanager.data.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A <code>Room</code> database definition for saving food-items.
 *
 * @see <a href=https://developer.android.com/topic/libraries/architecture/room>Room Persistence
 * Library</a>
 */
@Database(entities = {FoodItemEntity.class}, version = 3)
public abstract class FoodItemRoomDatabase extends RoomDatabase {

  private static final int NUMBER_OF_THREADS = 2;
  public static final ExecutorService dbExecService = Executors
      .newFixedThreadPool(NUMBER_OF_THREADS);
  private static FoodItemRoomDatabase INSTANCE;

  public static FoodItemRoomDatabase getInstance(final Context context) {
    if (INSTANCE == null) {
      synchronized (FoodItemRoomDatabase.class) {
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
            FoodItemRoomDatabase.class, "fooditem_database")
            .build();
      }
    }
    return INSTANCE;
  }

  /**
   * Defines the type of DAO this <code>Room</code> database should provide.
   *
   * @return <code>FoodItemDao</code> that grants access to values stored in
   * this database
   */
  public abstract FoodItemDao foodItemDao();
}
