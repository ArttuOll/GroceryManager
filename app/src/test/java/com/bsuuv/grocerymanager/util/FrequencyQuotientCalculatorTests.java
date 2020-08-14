package com.bsuuv.grocerymanager.util;

import static org.mockito.Mockito.when;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FrequencyQuotientCalculatorTests {

  @Mock
  private SharedPreferencesHelper mSharedPrefsHelper;
  private Set<String> mGroceryDays;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    this.mGroceryDays = new HashSet<>();
  }

  @Test
  public void getFrequencyQuotient_singleGroceryDay() {
    mGroceryDays.add("tuesday");

    when(mSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);

    FoodItemEntity foodItem = new FoodItemEntity("Olut", "Karjala", "Raikasta",
        2, "Packets", TimeFrame.TWO_WEEKS, 1, "", 0.0);

    Assert.assertEquals(0.5, FrequencyQuotientCalculator.calculate(mSharedPrefsHelper, foodItem),
        0.001);
    Assert.assertEquals(0.5, FrequencyQuotientCalculator.calculate(1, TimeFrame.TWO_WEEKS, 1),
        0.001);
  }

  @Test
  public void getFrequencyQuotient_multipleGroceryDays() {
    mGroceryDays.add("tuesday");
    mGroceryDays.add("wednesday");
    mGroceryDays.add("saturday");

    when(mSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);

    FoodItemEntity foodItem = new FoodItemEntity("Olut", "Karjala",
        "Raikasta", 2, "Packets", TimeFrame.TWO_WEEKS, 1,
        "", 0.0);

    Assert.assertEquals(0.15, FrequencyQuotientCalculator.calculate(mSharedPrefsHelper, foodItem),
        0.001);
    Assert.assertEquals(0.15, FrequencyQuotientCalculator.calculate(1, TimeFrame.TWO_WEEKS, 3),
        0.001);
  }
}
