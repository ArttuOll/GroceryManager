package com.bsuuv.grocerymanager.util;

import android.content.Context;
import com.bsuuv.grocerymanager.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DateHelper {

  public static int NO_GROCERY_DAYS_SET = 8;

  private Set<String> mGroceryDays;
  private Context mContext;
  private int today;
  private Calendar mCalendar;

  public DateHelper(Context context, SharedPreferencesHelper sharedPrefsHelper) {
    this.mContext = context;
    this.mGroceryDays = sharedPrefsHelper.getGroceryDays();

    this.mCalendar = createAndConfigureCalendar();
    this.today = mCalendar.get(Calendar.DAY_OF_WEEK);
  }

  private Calendar createAndConfigureCalendar() {
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
    // When Date-object is instantiated without parameters, its time is set to the current day.
    calendar.setTime(new Date());
    return calendar;
  }

  public boolean isGroceryDay() {
    for (String groceryDayString : mGroceryDays) {
      int groceryDay = stringWeekDayToInt(groceryDayString);
      if (groceryDay == today) {
        return true;
      }
    }
    return false;
  }

  public int timeUntilNextGroceryDay() {
    int daysUntilClosestGroceryDay = NO_GROCERY_DAYS_SET;
    for (String groceryDayString : mGroceryDays) {
      int groceryDay = getGroceryDayInt(groceryDayString);
      int daysFromTodayToGroceryDay = Math.abs(today - groceryDay);
      if (daysFromTodayToGroceryDay < daysUntilClosestGroceryDay) {
        daysUntilClosestGroceryDay = daysFromTodayToGroceryDay;
      }
    }
    return daysUntilClosestGroceryDay;
  }

  private int getGroceryDayInt(String groceryDay) {
    int groceryDayInt = stringWeekDayToInt(groceryDay);
    // If the weekday of the grocery day is already behind, put it to next week by adding 7
    // to it.
    if (groceryDayInt < today) {
      groceryDayInt += 7;
    }
    return groceryDayInt;
  }

  public String getCurrentDate() {
    DateFormat defaultLocaleFormat = SimpleDateFormat.getDateInstance();
    return defaultLocaleFormat.format(mCalendar.getTime());
  }

  private int stringWeekDayToInt(String weekday) {
    String[] daysOfWeek = mContext.getResources().getStringArray(R.array.daysofweek_datehelper);

    // Days of the week start from Sunday and are represented by integers 1..7
    if (daysOfWeek[0].equals(weekday)) {
      return 1;
    } else if (daysOfWeek[1].equals(weekday)) {
      return 2;
    } else if (daysOfWeek[2].equals(weekday)) {
      return 3;
    } else if (daysOfWeek[3].equals(weekday)) {
      return 4;
    } else if (daysOfWeek[4].equals(weekday)) {
      return 5;
    } else if (daysOfWeek[5].equals(weekday)) {
      return 6;
    } else if (daysOfWeek[6].equals(weekday)) {
      return 7;
    }

    throw new IllegalArgumentException("Given string didn't match any weekday!");
  }

  void setToday(int today) {
    this.today = today;
  }
}
