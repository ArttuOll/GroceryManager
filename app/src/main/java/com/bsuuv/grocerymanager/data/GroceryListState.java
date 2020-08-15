package com.bsuuv.grocerymanager.data;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;

/**
 * Represent the state of the grocery list on any given moment. The state consists of food-items
 * that were not displayed on the grocery list yet, but whose countdown values (for information on
 * countdown values, see {@link GroceryListExtractor} were incremented, and food-items that the user
 * has removed from the grocery list.
 *
 * @see GroceryListExtractor
 */
public class GroceryListState {

  private static final String INCREMENTED_ITEMS_KEY = "incrementedItems";
  private static final String REMOVED_ITEMS_KEY = "removedItems";

  private SharedPreferencesHelper mSharedPrefsHelper;
  private List<FoodItemEntity> mIncrementedItems, mRemovedItems;

  public GroceryListState(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mIncrementedItems = mSharedPrefsHelper.getList(INCREMENTED_ITEMS_KEY);
    this.mRemovedItems = mSharedPrefsHelper.getList(REMOVED_ITEMS_KEY);
  }

  public List<FoodItemEntity> getIncrementedItems() {
    return mIncrementedItems;
  }

  public List<FoodItemEntity> getRemovedItems() {
    return mRemovedItems;
  }

  public void remove(FoodItemEntity foodItem) {
    mRemovedItems.add(foodItem);
  }

  public void addToIncrementedItems(FoodItemEntity foodItem) {
    if (notIncremented(foodItem)) {
      mIncrementedItems.add(foodItem);
    }
  }

  private boolean notIncremented(FoodItemEntity foodItem) {
    return !getIncrementedItems().contains(foodItem);
  }

  public void saveState() {
    mSharedPrefsHelper.saveList(mIncrementedItems, INCREMENTED_ITEMS_KEY);
    mSharedPrefsHelper.saveList(mRemovedItems, REMOVED_ITEMS_KEY);
  }

  public void clearState() {
    mSharedPrefsHelper.clearList(INCREMENTED_ITEMS_KEY);
    mSharedPrefsHelper.clearList(REMOVED_ITEMS_KEY);
  }
}
