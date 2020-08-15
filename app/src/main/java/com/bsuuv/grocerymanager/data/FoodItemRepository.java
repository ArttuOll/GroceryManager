package com.bsuuv.grocerymanager.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.bsuuv.grocerymanager.data.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.data.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A single source of data for the whole app. Handles communication with {@link
 * FoodItemRoomDatabase} making sure that all operations are executed on a separate thread.
 *
 * @see FoodItemRoomDatabase
 */
public class FoodItemRepository {

  private FoodItemDao mDao;
  private LiveData<List<FoodItemEntity>> mFoodItems;

  public FoodItemRepository(Application application) {
    FoodItemRoomDatabase database = FoodItemRoomDatabase.getInstance(application);
    mDao = database.foodItemDao();
    mFoodItems = mDao.getAllFoodItems();
  }

  /**
   * Returns an always-up-to-date list of all food-items created by the user.
   *
   * @return List of all created food-items, wrapped in an observable <code>LiveData</code> object.
   * @see LiveData
   */
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
}