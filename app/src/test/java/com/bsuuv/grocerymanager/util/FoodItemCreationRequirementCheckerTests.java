package com.bsuuv.grocerymanager.util;

import static org.mockito.Mockito.when;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.util.FoodItemCreationRequirementChecker.RequirementNotMetException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FoodItemCreationRequirementCheckerTests {

  @Mock
  SharedPreferencesHelper mSharedPreferencesHelper;
  private FoodItemCreationRequirementChecker mChecker;
  private Set<String> mGroceryDays;
  private String label;
  private int amount;
  private TimeFrame timeFrame;
  private int frequency;
  private double frequencyQuotient;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    initMembers();
    when(mSharedPreferencesHelper.getGroceryDays()).thenReturn(mGroceryDays);
  }

  private void initMembers() {
    this.mChecker = new FoodItemCreationRequirementChecker(mSharedPreferencesHelper);
    this.mGroceryDays = new HashSet<>();
    mGroceryDays.add("monday");
    this.label = "testi";
    this.amount = 1;
    this.timeFrame = TimeFrame.WEEK;
    this.frequency = 1;
    this.frequencyQuotient = 1.0;
  }

  @Test
  public void allRequirementsMet() throws RequirementNotMetException {
    Assert.assertTrue(
        mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient));
  }

  @Test
  public void emptyLabel() {
    this.label = "";
    try {
      mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
    } catch (RequirementNotMetException e) {
      Assert.assertEquals(R.string.snackbar_label_empty, e.getMessageResId());
    }
  }

  @Test
  public void amountZero() {
    this.amount = 0;
    try {
      mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
    } catch (RequirementNotMetException e) {
      Assert.assertEquals(R.string.snackbar_amount_empty, e.getMessageResId());
    }
  }

  @Test
  public void timeFrameNull() {
    this.timeFrame = TimeFrame.NULL;
    try {
      mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
    } catch (RequirementNotMetException e) {
      Assert.assertEquals(R.string.snackbar_time_frame_not_chosen, e.getMessageResId());
    }
  }

  @Test
  public void frequencyZero() {
    this.frequency = 0;
    try {
      mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
    } catch (RequirementNotMetException e) {
      Assert.assertEquals(R.string.snackbar_frequency_not_set, e.getMessageResId());
    }
  }

  @Test
  public void frequencyQuotientTooLarge() {
    this.frequencyQuotient = 1.5;
    try {
      mChecker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
    } catch (RequirementNotMetException e) {
      Assert.assertEquals(R.string.snackbar_not_enough_grocery_days, e.getMessageResId());
    }
  }
}
