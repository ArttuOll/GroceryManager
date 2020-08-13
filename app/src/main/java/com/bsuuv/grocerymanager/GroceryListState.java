package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;

public class GroceryListState {

  private static final String MODIFIED_ITEMS_KEY = "modifiedlist";
  private static final String CHECKED_ITEMS_KEY = "checkeditems";

  private SharedPreferencesHelper mSharedPrefsHelper;
  private List<FoodItemEntity> mModifiedItems, mCheckedItems;

  public GroceryListState(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mModifiedItems = mSharedPrefsHelper.getList(MODIFIED_ITEMS_KEY);
    this.mCheckedItems = mSharedPrefsHelper.getList(CHECKED_ITEMS_KEY);
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
    mModifiedItems.add(foodItem);
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
