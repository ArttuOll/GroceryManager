package com.bsuuv.grocerymanager.util;

import android.content.Context;
import com.bsuuv.grocerymanager.R;

public class PluralsProvider {

  private Context mContext;

  public PluralsProvider(Context context) {
    this.mContext = context;
  }

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
    return "";
  }
}
