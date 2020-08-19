package com.bsuuv.grocerymanager.util;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeHelperTests {

  @Mock
  private Context mContext;
  @Mock
  private Resources mResources;
  @Mock
  // Though not used, Mockito needs this for constructing DateTimeHelper
  private SharedPreferencesHelper mSharedPrefsHelper;
  @Spy
  private Set<String> mGroceryDays;
  @InjectMocks
  private DateTimeHelper mDateTimeHelper;

  @Before
  public void init() {
    this.mGroceryDays = new HashSet<>();
    MockitoAnnotations.initMocks(this);
    configureMocks();
  }

  private void configureMocks() {
    when(mContext.getResources()).thenReturn(mResources);
    when(mResources.getStringArray(anyInt())).thenReturn(getDaysOfWeek());
  }

  private String[] getDaysOfWeek() {
    String[] daysOfWeek = new String[7];
    daysOfWeek[0] = "Sunday";
    daysOfWeek[1] = "Monday";
    daysOfWeek[2] = "Tuesday";
    daysOfWeek[3] = "Wednesday";
    daysOfWeek[4] = "Thursday";
    daysOfWeek[5] = "Friday";
    daysOfWeek[6] = "Saturday";

    return daysOfWeek;
  }

  @After
  public void clear() {
    this.mGroceryDays.clear();
  }

  @Test
  public void isGroceryDay_groceryDayNormalDay() {
    mDateTimeHelper.setToday(2);
    mGroceryDays.add("Monday");
    Assert.assertTrue(mDateTimeHelper.isGroceryDay());
  }

  @Test
  public void isGroceryDay_groceryDayEdgeCase1() {
    mDateTimeHelper.setToday(1);
    mGroceryDays.add("Sunday");
    Assert.assertTrue(mDateTimeHelper.isGroceryDay());
  }

  @Test
  public void isGroceryDay_groceryDayEdgeCase2() {
    mDateTimeHelper.setToday(7);
    mGroceryDays.add("Saturday");
    Assert.assertTrue(mDateTimeHelper.isGroceryDay());
  }

  @Test
  public void timeUntilNextGroceryDay_groceryWeekDayInFuture() {
    mDateTimeHelper.setToday(2);
    mGroceryDays.add("Tuesday");
    Assert.assertEquals(1, mDateTimeHelper.getTimeUntilNextGroceryDay());
  }

  @Test
  public void timeUntilNextGroceryDay_groceryWeekDayInPast() {
    mDateTimeHelper.setToday(4);
    mGroceryDays.add("Monday");
    Assert.assertEquals(5, mDateTimeHelper.getTimeUntilNextGroceryDay());
  }
}
