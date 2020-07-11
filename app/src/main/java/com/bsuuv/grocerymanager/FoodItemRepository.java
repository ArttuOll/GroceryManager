package com.bsuuv.grocerymanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bsuuv.grocerymanager.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;

import java.util.List;

class FoodItemRepository {

    private FoodItemDao mFoodItemDao;
    private LiveData<List<FoodItemEntity>> mFoodItems;

    FoodItemRepository(Application application) {
        FoodItemRoomDatabase database = FoodItemRoomDatabase.getInstance(application);
        mFoodItemDao = database.foodItemDao();
        mFoodItems = mFoodItemDao.getAllFoodItems();
    }

    public LiveData<List<FoodItemEntity>> getFoodItems() {
        return mFoodItems;
    }

    public void insert(FoodItemEntity foodItem) {
        new InsertAsyncTask(mFoodItemDao).execute(foodItem);
    }

    private static class InsertAsyncTask extends AsyncTask<FoodItemEntity, Void, Void> {

        private FoodItemDao mAsyncTaskFoodItemDao;

        InsertAsyncTask(FoodItemDao foodItemDao) {
            this.mAsyncTaskFoodItemDao = foodItemDao;
        }

        @Override
        protected Void doInBackground(FoodItemEntity... foodItems) {
            mAsyncTaskFoodItemDao.insert(foodItems[0]);
            return null;
        }
    }
}
