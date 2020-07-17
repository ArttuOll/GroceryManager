package com.bsuuv.grocerymanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.GroceryListManager;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;

import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {
    private FoodItemRepository mRepository;
    private LiveData<List<FoodItemEntity>> mFoodItems;
    private List<FoodItemEntity> mModifiedList;
    private GroceryListManager mGroceryListManager;

    public GroceryItemViewModel(Application application) {
        super(application);
        this.mRepository = new FoodItemRepository(application);
        this.mFoodItems = mRepository.getFoodItems();
        this.mGroceryListManager = new GroceryListManager(application);

        this.mModifiedList = mGroceryListManager.getModifiedList();

        if (!mGroceryListManager.isGroceryDay()) {
            updateDatabase();
        }
    }

    public LiveData<List<FoodItemEntity>> getGroceryList() {
        return Transformations.map(mFoodItems, mGroceryListManager::getGroceryItemsFromFoodItems);
    }

    public void delete(FoodItemEntity foodItem) {
        mRepository.delete(foodItem);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mGroceryListManager.isGroceryDay()) {
            mGroceryListManager.saveUpdateList(mModifiedList);
        }
    }
    private void updateDatabase() {
        for (FoodItemEntity foodItem : mModifiedList) {
            mRepository.update(foodItem);
        }
        mGroceryListManager.clearUpdateList();
    }
}

