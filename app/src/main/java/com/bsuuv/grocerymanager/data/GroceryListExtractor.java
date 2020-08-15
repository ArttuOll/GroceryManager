package com.bsuuv.grocerymanager.data;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic class that coordinates when food-items are ready to appear in the grocery list.
 * <p>
 * Food-items are ready to appear in the grocery list, when their countdown value is > 1.0 and they
 * haven't been removed from the grocery list by the user. Food-item's countdown value starts at its
 * frequency quotient (see {@link FrequencyQuotientCalculator} for more details on frequency
 * quotients) and is incremented by the value of its frequency quotient whenever its eligibility to
 * be on the grocery list is evaluated. When a food-item is added on the grocery list, its countdown
 * value is reset to its frequency quotient.
 *
 * @see FrequencyQuotientCalculator
 */
public class GroceryListExtractor {

  private GroceryListState mGroceries;
  private SharedPreferencesHelper mSharedPrefsHelper;

  public GroceryListExtractor(GroceryListState groceries,
      SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
    this.mGroceries = groceries;
  }

  /**
   * Iterates through the given list of food-items and checks which of them are ready to appear on
   * the grocery list.
   *
   * @param foodItems List of food-items from which the grocery list should be formed. In practice,
   *                  in this app this always corresponds to all food-items.
   * @return Food items, that are eligible to appear on the grocery list (countdown value >1.0 and
   * not previously removed from grocery list).
   */
  public List<FoodItemEntity> extractGroceryListFromFoodItems(List<FoodItemEntity> foodItems) {
    List<FoodItemEntity> groceries = new ArrayList<>();
    for (FoodItemEntity foodItem : foodItems) {
      double frequencyQuotient = FrequencyQuotientCalculator
          .calculate(mSharedPrefsHelper, foodItem);
      if (shouldAppearInGroceryList(foodItem)) {
        groceries.add(foodItem);
        resetCountdownValue(foodItem, frequencyQuotient);
      } else {
        incrementCountdownValue(foodItem, frequencyQuotient);
      }
      mGroceries.addToIncrementedItems(foodItem);
    }
    return groceries;
  }

  private boolean shouldAppearInGroceryList(FoodItemEntity foodItem) {
    return foodItem.getCountdownValue() >= 1 && !mGroceries.getRemovedItems().contains(foodItem);
  }

  private void resetCountdownValue(FoodItemEntity foodItem, double frequencyQuotient) {
    foodItem.setCountdownValue(frequencyQuotient);
  }

  private void incrementCountdownValue(FoodItemEntity foodItem, double frequencyQuotient) {
    foodItem.setCountdownValue(foodItem.getCountdownValue() + frequencyQuotient);
  }
}
