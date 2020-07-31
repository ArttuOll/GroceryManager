package com.bsuuv.grocerymanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.GroceryListManager;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;

import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {
    private FoodItemRepository mRepository;
    private LiveData<List<FoodItemEntity>> mFoodItems;
    private List<FoodItemEntity> mModifiedList;
    private GroceryListManager mGroceryListManager;
    private DateHelper mInspector;
    private List<FoodItemEntity> mCheckedItems;

    public GroceryItemViewModel(Application application) {
        super(application);
        this.mRepository = new FoodItemRepository(application);
        this.mFoodItems = mRepository.getFoodItems();
        this.mGroceryListManager = new GroceryListManager(application);
        this.mInspector = new DateHelper(application);

        this.mCheckedItems = mGroceryListManager.getCheckedItems();
        this.mModifiedList = mGroceryListManager.getModifiedList();

        if (!mInspector.isGroceryDay()) updateDatabase();
    }

    public LiveData<List<FoodItemEntity>> getGroceryList() {
        return Transformations.map(mFoodItems,
                mGroceryListManager::getGroceryItemsFromFoodItems);
    }

    public void check(FoodItemEntity foodItem) {
        this.mCheckedItems.add(foodItem);
    }

    public FoodItemEntity get(int foodItemId) {
        return mRepository.getFoodItem(foodItemId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mInspector.isGroceryDay()) {
            mGroceryListManager.saveBuffers(mModifiedList, mCheckedItems);
        }
    }

    private void updateDatabase() {
        for (FoodItemEntity foodItem : mModifiedList)
            mRepository.update(foodItem);
        mGroceryListManager.clearBuffers();
    }
}

