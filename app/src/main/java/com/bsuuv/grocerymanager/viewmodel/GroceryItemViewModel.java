package com.bsuuv.grocerymanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.GroceryListManager;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.GroceryDayInspector;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {
    private FoodItemRepository mRepository;
    private LiveData<List<FoodItemEntity>> mFoodItems;
    private List<FoodItemEntity> mModifiedList;
    private GroceryListManager mGroceryListManager;
    private GroceryDayInspector mInspector;
    private List<FoodItemEntity> mCheckedItems;

    public GroceryItemViewModel(Application application) {
        super(application);
        this.mRepository = new FoodItemRepository(application);
        this.mFoodItems = mRepository.getFoodItems();
        this.mGroceryListManager = new GroceryListManager(new SharedPreferencesHelper(application));
        this.mInspector = new GroceryDayInspector(application);

        this.mCheckedItems = mGroceryListManager.getCheckedItems();
        this.mModifiedList = mGroceryListManager.getModifiedList();

        if (!mInspector.isGroceryDay()) updateDatabase();
    }

    public LiveData<List<FoodItemEntity>> getGroceryList() {
        LiveData<List<FoodItemEntity>> groceryList = Transformations.map(mFoodItems,
                mGroceryListManager::getGroceryItemsFromFoodItems);
        return Transformations.map(groceryList, this::filterChecked);
    }

    private List<FoodItemEntity> filterChecked(List<FoodItemEntity> foodItems) {
        ArrayList<FoodItemEntity> unchecked = new ArrayList<>();

        for (FoodItemEntity foodItem : foodItems) {
            if (!mCheckedItems.contains(foodItem)) unchecked.add(foodItem);
        }

        return unchecked;
    }

    public void check(FoodItemEntity foodItem) {
        this.mCheckedItems.add(foodItem);
    }

    public void delete(FoodItemEntity foodItem) {
        mRepository.delete(foodItem);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mInspector.isGroceryDay()) {
            mGroceryListManager.saveBuffers(mModifiedList, mCheckedItems);
        }
    }
    private void updateDatabase() {
        for (FoodItemEntity foodItem : mModifiedList) mRepository.update(foodItem);
        mGroceryListManager.clearBuffers();
    }
}

