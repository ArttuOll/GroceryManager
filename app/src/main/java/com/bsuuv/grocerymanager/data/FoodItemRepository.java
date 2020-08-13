package com.bsuuv.grocerymanager.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.bsuuv.grocerymanager.data.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.data.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodItemRepository {

  private FoodItemDao mDao;
  private LiveData<List<FoodItemEntity>> mFoodItems;

  public FoodItemRepository(Application application) {
    FoodItemRoomDatabase database = FoodItemRoomDatabase.getInstance(application);
    mDao = database.foodItemDao();
    mFoodItems = mDao.getAllFoodItems();
  }

  public LiveData<List<FoodItemEntity>> getFoodItems() {
    return mFoodItems;
  }

  public FoodItemEntity getFoodItem(int foodItemId) {
    FoodItemEntity result = null;
    try {
      result = FoodItemRoomDatabase.dbExecService.submit(() -> mDao.get(foodItemId)).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void insert(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mDao.insert(foodItem));
  }

  public void delete(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mDao.delete(foodItem));
  }

  public void update(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mDao.update(foodItem));
  }

  public void deleteAll() {
    FoodItemRoomDatabase.dbExecService.execute(() -> mDao.deleteAll());
  }
}