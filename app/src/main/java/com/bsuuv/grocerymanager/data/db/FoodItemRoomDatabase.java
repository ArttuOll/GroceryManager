package com.bsuuv.grocerymanager.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.bsuuv.grocerymanager.data.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodItemEntity.class}, version = 3)
public abstract class FoodItemRoomDatabase extends RoomDatabase {

  private static final int NUMBER_OF_THREADS = 2;
  public static final ExecutorService dbExecService =
      Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  private static FoodItemRoomDatabase INSTANCE;

  public static FoodItemRoomDatabase getInstance(final Context context) {
    if (INSTANCE == null) {
      synchronized (FoodItemRoomDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(),
                  FoodItemRoomDatabase.class,
                  "fooditem_database")
                  .fallbackToDestructiveMigration()
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract FoodItemDao foodItemDao();
}