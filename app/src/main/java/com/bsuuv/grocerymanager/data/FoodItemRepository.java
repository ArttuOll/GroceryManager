package com.bsuuv.grocerymanager.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.bsuuv.grocerymanager.data.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.data.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodItemRepository {

  private FoodItemDao mFoodItemDao;
  private LiveData<List<FoodItemEntity>> mFoodItems;

  public FoodItemRepository(Application application) {
    FoodItemRoomDatabase database = FoodItemRoomDatabase.getInstance(application);
    mFoodItemDao = database.foodItemDao();
    mFoodItems = mFoodItemDao.getAllFoodItems();
  }

  public LiveData<List<FoodItemEntity>> getFoodItems() {
    return mFoodItems;
  }

  public FoodItemEntity getFoodItem(int foodItemId) {
    FoodItemEntity result = null;
    try {
      result = FoodItemRoomDatabase.dbExecService.submit(() -> mFoodItemDao.get(foodItemId)).get();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void insert(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mFoodItemDao.insert(foodItem));
  }

  public void delete(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mFoodItemDao.delete(foodItem));
  }

  public void update(FoodItemEntity foodItem) {
    FoodItemRoomDatabase.dbExecService.execute(() -> mFoodItemDao.update(foodItem));
  }

  public void deleteAll() {
    FoodItemRoomDatabase.dbExecService.execute(() -> mFoodItemDao.deleteAll());
  }
}