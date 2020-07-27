package com.bsuuv.grocerymanager;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bsuuv.grocerymanager.db.FoodItemRoomDatabase;
import com.bsuuv.grocerymanager.db.dao.FoodItemDao;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;

import java.util.List;

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

    public void insert(FoodItemEntity foodItem) {
        new InsertAsyncTask(mFoodItemDao).execute(foodItem);
    }

    public void delete(FoodItemEntity foodItem) {
        new DeleteAsyncTask(mFoodItemDao).execute(foodItem);
    }

    public void update(FoodItemEntity foodItem) {
        new UpdateAsyncTask(mFoodItemDao).execute(foodItem);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(mFoodItemDao).execute();
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

    private static class DeleteAsyncTask extends AsyncTask<FoodItemEntity, Void, Void> {

        private FoodItemDao mAsyncTaskFoodItemDao;

        DeleteAsyncTask(FoodItemDao foodItemDao) {
            this.mAsyncTaskFoodItemDao = foodItemDao;
        }

        @Override
        protected Void doInBackground(FoodItemEntity... foodItems) {
            mAsyncTaskFoodItemDao.delete(foodItems[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<FoodItemEntity, Void, Void> {

        private FoodItemDao mAsyncTaskFoodItemDao;

        UpdateAsyncTask(FoodItemDao foodItemDao) {
            this.mAsyncTaskFoodItemDao = foodItemDao;
        }

        @Override
        protected Void doInBackground(FoodItemEntity... foodItems) {
            mAsyncTaskFoodItemDao.update(foodItems[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<FoodItemEntity, Void, Void> {

        private FoodItemDao mAsyncTaskFoodItemDao;

        DeleteAllAsyncTask(FoodItemDao foodItemDao) {
            this.mAsyncTaskFoodItemDao = foodItemDao;
        }

        @Override
        protected Void doInBackground(FoodItemEntity... foodItems) {
            mAsyncTaskFoodItemDao.deleteAll();
            return null;
        }
    }
}
