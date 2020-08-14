package com.bsuuv.grocerymanager.util;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;

public class FrequencyQuotientCalculator {

  public static double calculate(SharedPreferencesHelper sharedPreferencesHelper,
      FoodItemEntity foodItem) {
    int groceryDaysAWeek = sharedPreferencesHelper.getGroceryDays().size();
    double frequencyQuotient = ((double) foodItem.getFrequency()) /
        (foodItem.getTimeFrame().value() * groceryDaysAWeek);
    return Math.round(frequencyQuotient * 20) / 20.0;
  }

  public static double calculate(int frequency, TimeFrame timeFrame, int groceryDaysAWeek) {
    double frequencyQuotient = ((double) frequency) / (timeFrame.value() * groceryDaysAWeek);
    return Math.round(frequencyQuotient * 20) / 20.0;
  }
}
