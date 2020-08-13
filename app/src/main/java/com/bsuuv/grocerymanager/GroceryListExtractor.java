package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import java.util.ArrayList;
import java.util.List;

public class GroceryListExtractor {

  private FrequencyQuotientCalculator mFqCalculator;
  private GroceryListState mState;

  public GroceryListExtractor(GroceryListState state, FrequencyQuotientCalculator fqCalculator) {
    this.mState = state;
    this.mFqCalculator = fqCalculator;
  }

  public List<FoodItemEntity> extractGroceryListFromFoodItems(List<FoodItemEntity> foodItems) {
    List<FoodItemEntity> groceries = new ArrayList<>();
    for (FoodItemEntity foodItem : foodItems) {
      if (shouldAppearInGroceryList(foodItem)) {
        groceries.add(foodItem);
        resetCountdownValue(foodItem);
      } else {
        incrementCountdownValue(foodItem);
      }
      if (notModified(foodItem)) {
        mState.markAsModified(foodItem);
      }
    }
    return groceries;
  }

  private boolean shouldAppearInGroceryList(FoodItemEntity foodItem) {
    return foodItem.getCountdownValue() >= 1 && !mState.getCheckedItems().contains(foodItem);
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
    return !mState.getModifiedItems().contains(foodItem);
  }
}
