package com.bsuuv.grocerymanager.util;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.NewFoodItemActivity;

/**
 * Utility class containing logic to check that given food-item properties fit their requirements.
 * This class is used in {@link NewFoodItemActivity} to validate the values in its input fields.
 */
public class FoodItemCreationRequirementChecker {

  private static final int FREQUENCY_NOT_SET = 0;
  private static final int AMOUNT_FIELD_EMPTY = 0;
  private static final double MAX_FREQUENCY_QUOTIENT = 1.0;

  private SharedPreferencesHelper mSharedPrefsHelper;

  public FoodItemCreationRequirementChecker(SharedPreferencesHelper sharedPreferencesHelper) {
    this.mSharedPrefsHelper = sharedPreferencesHelper;
  }

  /**
   * The requirements are as follows:
   * <ul>
   *   <li>Label must not be empty</li>
   *   <li>Amount must be greater than zero</li>
   *   <li>TimeFrame must not equal <code>TimeFrame.NULL</code></li>
   *   <li>Frequency must be greater than zero</li>
   *   <li>Frequency quotient must not be more than 1.0</li>
   * </ul>
   *
   * @param label             Label of the food-item
   * @param amount            Amount of the food-item
   * @param timeFrame         Time frame property of the food-item (see {@link TimeFrame}).
   * @param frequency         Frequency property of the food-item
   * @param frequencyQuotient Frequency quotient of the food-item (see {@link
   *                          FrequencyQuotientCalculator}).
   * @return Boolean telling if all of the requirements were met.
   * @throws RequirementNotMetException An exception with message telling which of the requirements
   *                                    was not met.
   */
  public boolean requirementsMet(String label, int amount, TimeFrame timeFrame,
      int frequency, double frequencyQuotient) throws RequirementNotMetException {
    return groceryDaysSet() &&
        inputFieldsValid(label, amount, timeFrame, frequency) &&
        frequencyQuotientValid(frequencyQuotient);
  }

  private boolean groceryDaysSet() throws RequirementNotMetException {
    int groceryDaysAWeek = mSharedPrefsHelper.getGroceryDays().size();
    if (groceryDaysAWeek > 0) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_no_grocery_days);
  }

  private boolean inputFieldsValid(String label, int amount, TimeFrame timeFrame,
      int frequency) throws RequirementNotMetException {
    return labelFieldValid(label) &&
        amountFieldValid(amount) &&
        timeFrameSelected(timeFrame) &&
        frequencyFieldSet(frequency);
  }

  private boolean labelFieldValid(String label) throws RequirementNotMetException {
    if (!label.isEmpty()) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_label_empty);
  }

  private boolean amountFieldValid(int amount) throws RequirementNotMetException {
    if (amount > AMOUNT_FIELD_EMPTY) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_amount_empty);
  }

  private boolean timeFrameSelected(TimeFrame timeFrame) throws RequirementNotMetException {
    if (timeFrame != TimeFrame.NULL) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_time_frame_not_chosen);
  }

  private boolean frequencyFieldSet(int frequency) throws RequirementNotMetException {
    if (frequency > FREQUENCY_NOT_SET) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_frequency_not_set);
  }

  private boolean frequencyQuotientValid(double frequencyQuotient)
      throws RequirementNotMetException {
    if (frequencyQuotient <= MAX_FREQUENCY_QUOTIENT) {
      return true;
    }
    throw new RequirementNotMetException(R.string.snackbar_not_enough_grocery_days);
  }

  public static class RequirementNotMetException extends Exception {

    private int mMessageResId;

    public RequirementNotMetException(int messageResId) {
      this.mMessageResId = messageResId;
    }

    public int getMessageResId() {
      return mMessageResId;
    }
  }
}
