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
public class DateHelperTests {

  @Mock
  private Context mContext;

  @Mock
  private Resources mResources;

  @Mock
  // Though not used, Mockito needs this for constructing DateHelper
  private SharedPreferencesHelper mSharedPrefsHelper;

  @Spy
  private Set<String> mGroceryDays;

  @InjectMocks
  private DateHelper mDateHelper;

  private static String[] getDaysOfWeek() {
    String[] daysOfWeek = new String[7];
    daysOfWeek[0] = "sunday";
    daysOfWeek[1] = "monday";
    daysOfWeek[2] = "tuesday";
    daysOfWeek[3] = "wednesday";
    daysOfWeek[4] = "thursday";
    daysOfWeek[5] = "friday";
    daysOfWeek[6] = "saturday";

    return daysOfWeek;
  }

  @Before
  public void init() {
    this.mGroceryDays = new HashSet<>();

    MockitoAnnotations.initMocks(this);

    // Current day is Monday
    mDateHelper.setToday(2);
    when(mContext.getResources()).thenReturn(mResources);
    when(mResources.getStringArray(anyInt())).thenReturn(getDaysOfWeek());
  }

  @After
  public void clear() {
    this.mGroceryDays.clear();
  }

  @Test
  public void isGroceryDay_groceryDay() {
    mGroceryDays.add("monday");

    Assert.assertTrue(mDateHelper.isGroceryDay());
  }

  @Test
  public void timeUntilNextGroceryDay_groceryWeekDayInFuture() {
    mGroceryDays.add("tuesday");

    Assert.assertEquals(1, mDateHelper.timeUntilNextGroceryDay());
  }

  @Test
  public void timeUntilNextGroceryDay_groceryWeekDayInPast() {
    // Current day is Wednesday
    mDateHelper.setToday(4);
    mGroceryDays.add("monday");

    Assert.assertEquals(5, mDateHelper.timeUntilNextGroceryDay());
  }
}
