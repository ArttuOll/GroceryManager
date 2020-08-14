package com.bsuuv.grocerymanager.data;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

public class GroceryListState {

  private static final String MODIFIED_ITEMS_KEY = "modifiedlist";
  private static final String CHECKED_ITEMS_KEY = "checkeditems";

  private SharedPreferencesHelper mSharedPrefsHelper;
  private List<FoodItemEntity> mGroceryList, mModifiedItems, mCheckedItems;

  public GroceryListState(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mGroceryList = new ArrayList<>();
    this.mModifiedItems = mSharedPrefsHelper.getList(MODIFIED_ITEMS_KEY);
    this.mCheckedItems = mSharedPrefsHelper.getList(CHECKED_ITEMS_KEY);
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

  public List<FoodItemEntity> getCheckedItems() {
    return mCheckedItems;
  }

  public void check(FoodItemEntity foodItem) {
    mCheckedItems.add(foodItem);
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
    mSharedPrefsHelper.saveList(mCheckedItems, CHECKED_ITEMS_KEY);
  }

  public void clearState() {
    mSharedPrefsHelper.clearList(MODIFIED_ITEMS_KEY);
    mSharedPrefsHelper.clearList(CHECKED_ITEMS_KEY);
  }
}
