package com.bsuuv.grocerymanager.data;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;

public class GroceryListExtractor {

  private GroceryListState mGroceries;
  private SharedPreferencesHelper mSharedPrefsHelper;

  public GroceryListExtractor(GroceryListState groceries,
      SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mGroceries = groceries;
  }

  public List<FoodItemEntity> extractGroceryListFromFoodItems(List<FoodItemEntity> foodItems) {
    for (FoodItemEntity foodItem : foodItems) {
      double frequencyQuotient = FrequencyQuotientCalculator
          .calculate(mSharedPrefsHelper, foodItem);
      if (shouldAppearInGroceryList(foodItem)) {
        mGroceries.addToGroceryList(foodItem);
        resetCountdownValue(foodItem, frequencyQuotient);
      } else {
        incrementCountdownValue(foodItem, frequencyQuotient);
      }
      mGroceries.markAsModified(foodItem);
    }
    return mGroceries.getGroceryList();
  }

  private boolean shouldAppearInGroceryList(FoodItemEntity foodItem) {
    return foodItem.getCountdownValue() >= 1 && !mGroceries.getCheckedItems().contains(foodItem);
  }

  private void resetCountdownValue(FoodItemEntity foodItem, double frequencyQuotient) {
    foodItem.setCountdownValue(frequencyQuotient);
  }

  private void incrementCountdownValue(FoodItemEntity foodItem, double frequencyQuotient) {
    foodItem.setCountdownValue(foodItem.getCountdownValue() + frequencyQuotient);
  }
}
