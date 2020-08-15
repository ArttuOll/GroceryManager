package com.bsuuv.grocerymanager.data;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

public class GroceryListState {

  private static final String MODIFIED_ITEMS_KEY = "modifiedItems";
  private static final String REMOVED_ITEMS_KEY = "removedItems";

  private SharedPreferencesHelper mSharedPrefsHelper;
  private List<FoodItemEntity> mGroceryList, mModifiedItems, mRemovedItems;

  public GroceryListState(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mGroceryList = new ArrayList<>();
    this.mModifiedItems = mSharedPrefsHelper.getList(MODIFIED_ITEMS_KEY);
    this.mRemovedItems = mSharedPrefsHelper.getList(REMOVED_ITEMS_KEY);
  }

  public void addToGroceryList(FoodItemEntity foodItem) {
    mGroceryList.add(foodItem);
  }

  public List<FoodItemEntity> getGroceryList() {
    return mGroceryList;
  }

  public List<FoodItemEntity> getModifiedItems() {
    return mModifiedItems;
  }

  public List<FoodItemEntity> getRemovedItems() {
    return mRemovedItems;
  }

  public void remove(FoodItemEntity foodItem) {
    mRemovedItems.add(foodItem);
  }

  public void markAsModified(FoodItemEntity foodItem) {
    if (notModified(foodItem)) {
      mModifiedItems.add(foodItem);
    }
  }

  private boolean notModified(FoodItemEntity foodItem) {
    return !getModifiedItems().contains(foodItem);
  }

  public void saveState() {
    mSharedPrefsHelper.saveList(mModifiedItems, MODIFIED_ITEMS_KEY);
    mSharedPrefsHelper.saveList(mRemovedItems, REMOVED_ITEMS_KEY);
  }

  public void clearState() {
    mSharedPrefsHelper.clearList(MODIFIED_ITEMS_KEY);
    mSharedPrefsHelper.clearList(REMOVED_ITEMS_KEY);
  }
}
