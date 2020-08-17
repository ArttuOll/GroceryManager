package com.bsuuv.grocerymanager.util;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;

/**
 * Utility class for calculating food-item frequency quotients.
 * <p>
 * Frequency quotient is food-item's frequency divided by multiplication of its time frame and the
 * amount of grocery days in a week. It is used as an unit of incrementation for a food-item's
 * countdown value.
 * <p>
 * A food-item appears on the grocery list when its countdown value is 1.0 (or greater). The
 * countdown value starts at the value of the food-item's frequency quotient and each grocery day
 * the countdown value is increased by the amount of the food-item's frequency quotient. The
 * frequency quotient makes sure the food-item's countdown value reaches 1.0 exactly when the user
 * wants it (based on the time frame and frequency properties set by the user).
 * <p>
 * Example: The user wants to have a certain food-item appear on the grocery list once a week. He
 * has set there to be two grocery days in a week. When creating the food-item, he chooses a
 * frequency of 1 and a time frame of a week (remember, TimeFrame.WEEk = 1). Thus the frequency
 * quotient is: 1 / (1 * 2) = 0.5. On the next grocery day the countdown value is 0.5, so the item
 * is not shown on the grocery list. On the next grocery day after that, the countdown value is
 * summed by the frequency quotient, so its value becomes 1.0 and it's shown on the grocery list.
 * <p>
 * The countdown value can exceed 1.0 if the frequency quotient changes between grocery days.
 */
public class FrequencyQuotientCalculator {

  /**
   * @param sharedPreferencesHelper Used to retrieve the number of grocery days a week
   * @param foodItem                The food-item for which the frequency quotient is calculated.
   * @return Frequency quotient of the supplied food-item, rounded to the nearest 0.05.
   */
  public static double calculate(SharedPreferencesHelper sharedPreferencesHelper,
      FoodItemEntity foodItem) {
    int groceryDaysAWeek = sharedPreferencesHelper.getGroceryDays().size();
    double frequency = (double) foodItem.getFrequency();
    int timeFrame = foodItem.getTimeFrame().value();
    double frequencyQuotient = frequency / (timeFrame * groceryDaysAWeek);
    return Math.round(frequencyQuotient * 20) / 20.0;
  }

  /**
   * @param frequency        Frequency of the food-item
   * @param timeFrame        Time frame of the food-item
   * @param groceryDaysAWeek Number of grocery days a week
   * @return Frequency quotient calculated based on the given arguments, rounded to the nearest
   * 0.05.
   */
  public static double calculate(int frequency, TimeFrame timeFrame, int groceryDaysAWeek) {
    double timeFrameValue = timeFrame.value();
    double frequencyQuotient = (double) frequency / (timeFrameValue * groceryDaysAWeek);
    return Math.round(frequencyQuotient * 20) / 20.0;
  }
}
