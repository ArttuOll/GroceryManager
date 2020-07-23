package com.bsuuv.grocerymanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bsuuv.grocerymanager.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;

@Database(entities = {FoodItemEntity.class}, version = 2)
public abstract class FoodItemRoomDatabase extends RoomDatabase {

    private static FoodItemRoomDatabase INSTANCE;

    public static FoodItemRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (FoodItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodItemRoomDatabase.class, "fooditem_database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract FoodItemDao foodItemDao();
}
