package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

public class GroceryListExtractor {

  public static final String MODIFIED_ITEMS_KEY = "modifiedlist";
  public static final String CHECKED_ITEMS_KEY = "checkeditems";

  private List<FoodItemEntity> mModifiedItems;
  private List<FoodItemEntity> mCheckedItems;
  private DateHelper mDateHelper;
  private FrequencyQuotientCalculator mFqCalculator;
  private SharedPreferencesHelper mSharedPrefsHelper;

  public GroceryListExtractor(SharedPreferencesHelper sharedPreferencesHelper,
      DateHelper dateHelper, FrequencyQuotientCalculator fqCalculator) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mDateHelper = dateHelper;
    this.mFqCalculator = fqCalculator;
    this.mModifiedItems = mSharedPrefsHelper.getList(MODIFIED_ITEMS_KEY);
    this.mCheckedItems = mSharedPrefsHelper.getList(CHECKED_ITEMS_KEY);
  }

  public List<FoodItemEntity> extractGroceryListFromFoodItems(List<FoodItemEntity> foodItems) {
    List<FoodItemEntity> groceries = new ArrayList<>();
    if (mDateHelper.isGroceryDay()) {
      for (FoodItemEntity foodItem : foodItems) {
        if (shouldAppearInGroceryList(foodItem)) {
          groceries.add(foodItem);
          resetCountdownValue(foodItem);
        } else {
          incrementCountdownValue(foodItem);
        }
        if (notModified(foodItem)) {
          mModifiedItems.add(foodItem);
        }
      }
    }
    return groceries;
  }

  private boolean shouldAppearInGroceryList(FoodItemEntity foodItem) {
    return foodItem.getCountdownValue() >= 1 && !mCheckedItems.contains(foodItem);
  }

  private void resetCountdownValue(FoodItemEntity foodItem) {
    double frequencyQuotient = mFqCalculator.getFrequencyQuotient(foodItem);
    foodItem.setCountdownValue(frequencyQuotient);
  }

  private void incrementCountdownValue(FoodItemEntity foodItem) {
    double frequencyQuotient = mFqCalculator.getFrequencyQuotient(foodItem);
    foodItem.setCountdownValue(foodItem.getCountdownValue() + frequencyQuotient);
  }

  private boolean notModified(FoodItemEntity foodItem) {
    return !mModifiedItems.contains(foodItem);
  }

  public List<FoodItemEntity> getModifiedItems() {
    return mModifiedItems;
  }

  public List<FoodItemEntity> getCheckedItems() {
    return mCheckedItems;
  }

  public void saveState(List<FoodItemEntity> modifiedItems,
      List<FoodItemEntity> checkedItems) {
    mSharedPrefsHelper.saveList(modifiedItems, MODIFIED_ITEMS_KEY);
    mSharedPrefsHelper.saveList(checkedItems, CHECKED_ITEMS_KEY);
  }

  public void clearState() {
    mSharedPrefsHelper.clearList(MODIFIED_ITEMS_KEY);
    mSharedPrefsHelper.clearList(CHECKED_ITEMS_KEY);
  }

}
