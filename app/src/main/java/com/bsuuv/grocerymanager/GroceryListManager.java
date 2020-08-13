package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

public class GroceryListManager {

  public static final String MODIFIED_LIST_KEY = "modifiedlist";
  public static final String CHECKED_ITEMS_KEY = "checkeditems";

  private List<FoodItemEntity> mModifiedList;
  private List<FoodItemEntity> mCheckedItems;
  private DateHelper mDateHelper;
  private FrequencyQuotientCalculator mFqCalculator;
  private SharedPreferencesHelper mSharedPrefsHelper;

  public GroceryListManager(SharedPreferencesHelper sharedPreferencesHelper,
      DateHelper dateHelper,
      FrequencyQuotientCalculator fqCalculator) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mDateHelper = dateHelper;
    this.mFqCalculator = fqCalculator;

    this.mModifiedList = mSharedPrefsHelper.getList(MODIFIED_LIST_KEY);
    this.mCheckedItems = mSharedPrefsHelper.getList(CHECKED_ITEMS_KEY);
  }

  public List<FoodItemEntity> getGroceryItemsFromFoodItems(List<FoodItemEntity> foodItems) {
    List<FoodItemEntity> groceryItems = new ArrayList<>();
    if (mDateHelper.isGroceryDay()) {
      for (FoodItemEntity foodItem : foodItems) {
        double frequencyQuotient =
            mFqCalculator.getFrequencyQuotient(foodItem);
        // Each grocery day the countdown value is incremented by the
        // value of frequency
        // quotient.
        // When it reaches 1, it's time for the item to appear in the
        // grocery list.
        double countdownValue = foodItem.getCountdownValue();

        // If the countdown value for a food-item has reached 1 and
        // the user hasn't
        // removed it from RecyclerView in MainActivity, put it to
        // grocery list.
        // The value can be greater than one if the number of grocery
        // days decreases while
        // the countdownValue has already been assigned a value.
        if (countdownValue >= 1 && !mCheckedItems.contains(foodItem)) {
          groceryItems.add(foodItem);

          foodItem.setCountdownValue(frequencyQuotient);
        } else {

          // Increment the food-item countdown value by adding to
          // it the original value.
          foodItem.setCountdownValue(countdownValue + frequencyQuotient);
        }
        if (!mModifiedList.contains(foodItem)) {
          mModifiedList.add(foodItem);
        }
      }
    }
    return groceryItems;
  }

  public List<FoodItemEntity> getModifiedList() {
    return mModifiedList;
  }

  public List<FoodItemEntity> getCheckedItems() {
    return mCheckedItems;
  }

  public void saveBuffers(List<FoodItemEntity> modifiedItems,
      List<FoodItemEntity> checkedItems) {
    mSharedPrefsHelper.saveList(modifiedItems, MODIFIED_LIST_KEY);
    mSharedPrefsHelper.saveList(checkedItems, CHECKED_ITEMS_KEY);
  }

  public void clearBuffers() {
    mSharedPrefsHelper.clearList(MODIFIED_LIST_KEY);
    mSharedPrefsHelper.clearList(CHECKED_ITEMS_KEY);
  }

}
