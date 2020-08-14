package com.bsuuv.grocerymanager.util;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;

//TODO: muokkaa staattiseksi
public class FrequencyQuotientCalculator {

  private SharedPreferencesHelper mSharedPrefsHelper;

  public FrequencyQuotientCalculator(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
  }

  public double getFrequencyQuotient(FoodItemEntity foodItem) {
    int groceryDaysAWeek = mSharedPrefsHelper.getGroceryDays().size();

    // A double representing the frequency in which a food-item should appear in
    // grocery list. If there's one grocery day a week and a food-item is to be had
    // once every two weeks, then the frequency quotient is
    // 1 per week / (1 grocery day * 2 weeks) = 1/2.
    double frequencyQuotient = ((double) foodItem.getFrequency()) /
        (foodItem.getTimeFrame().value() * groceryDaysAWeek);
    // Round to nearest 0.05
    return Math.round(frequencyQuotient * 20) / 20.0;
  }

  public double getFrequencyQuotient(int frequency, TimeFrame timeFrame, int groceryDaysAWeek) {
    // A double representing the frequency in which a food-item should
    // appear in
    // grocery list. If there's one grocery day a week and a food-item is
    // to be had
    // once every two weeks, then the frequency quotient is
    // 1 per week / (1 grocery day * 2 weeks) = 1/2.
    double frequencyQuotient =
        ((double) frequency) / (timeFrame.value() * groceryDaysAWeek);
    // Round to nearest 0.05
    return Math.round(frequencyQuotient * 20) / 20.0;
  }
}
