package com.bsuuv.grocerymanager.util;

/**
 * An enum value representing a food-item's time frame.
 * <p>
 * Time frame is the period in time to which the food-item's frequency should be applied. For
 * example, if frequency = 3 and time frame = WEEK, then the food item appears on the grocery list 3
 * times a week (given that there are enough grocery days set for that).
 */
public enum TimeFrame {
  WEEK(1), TWO_WEEKS(2), MONTH(4), NULL(-2);

  private final int mValue;

  TimeFrame(int value) {
    this.mValue = value;
  }

  /**
   * @return Integer representation of the enum
   */
  public int value() {
    return mValue;
  }
}
