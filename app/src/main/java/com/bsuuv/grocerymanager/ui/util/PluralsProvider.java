package com.bsuuv.grocerymanager.ui.util;

import android.content.Context;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.util.TimeFrame;

/**
 * Utility class for creating certain strings of the app that require plural forms.
 */
public class PluralsProvider {

  private Context mContext;

  public PluralsProvider(Context context) {
    this.mContext = context;
  }

  /**
   * Creates a string describing a food-item's schedule based on its frequency and time frame in
   * correct plural format. For example: when frequency = 1 and timeFrame =
   * <code>TimeFrame.WEEK</code>, the produced string is "Once a week". If frequency = 2, then the
   * string is "Twice in a week".
   *
   * @param frequency Frequency of the food-item which the string describes
   * @param timeFrame Time frame of the food-item which the string describes
   * @return String describing the schedule in which a food-item appears on the grocery list
   */
  public String getScheduleString(int frequency, TimeFrame timeFrame) {
    switch (timeFrame) {
      case WEEK:
        return mContext.getResources().getQuantityString(
            R.plurals.times_a_week, frequency, frequency);
      case TWO_WEEKS:
        return mContext.getResources().getQuantityString(
            R.plurals.times_in_two_weeks, frequency, frequency);
      case MONTH:
        return mContext.getResources().getQuantityString(
            R.plurals.times_in_a_month, frequency, frequency);
      default:
        return "";
    }
  }

  /**
   * Creates a string describing food-item's amount and unit in correct plural format. For example,
   * when amount = 1 and unit = piece, the resulting string is "One piece". If the amount = 2, then
   * the string is "Two pieces".
   *
   * @param amount Amount of the food-item which the string describes
   * @param unit   Unit of the food-item which the string describes
   * @return String describing the quantity of the food-item
   */
  public String getAmountString(int amount, String unit) {
    String[] units = mContext.getResources().getStringArray(R.array.units_plural);
    if (units[0].equals(unit)) {
      return mContext.getResources().getQuantityString(R.plurals.Pieces, amount,
          amount);
    } else if (units[1].equals(unit)) {
      return mContext.getResources().getQuantityString(R.plurals.Packets, amount,
          amount);
    } else if (units[2].equals(unit)) {
      return mContext.getResources().getQuantityString(R.plurals.Cans, amount,
          amount);
    } else if (units[3].equals(unit)) {
      return mContext.getResources().getQuantityString(R.plurals.Bags,
          amount,
          amount);
    } else if (units[4].equals(unit)) {
      return mContext.getResources().getQuantityString(R.plurals.Bottles,
          amount, amount);
    }
    return String.valueOf(amount);
  }
}
