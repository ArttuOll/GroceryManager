package com.bsuuv.grocerymanager.util;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class GroceryDayInspector {

    private Calendar mCalendar;
    private Set<String> mGroceryDays;

    public GroceryDayInspector(Context context) {
        init();
        SharedPreferencesHelper sharedPrefsHelper = new SharedPreferencesHelper(context);
        this.mGroceryDays = sharedPrefsHelper.getGroceryDays();

    }

    public GroceryDayInspector(SharedPreferencesHelper sharedPreferencesHelper) {
        init();
        this.mGroceryDays = sharedPreferencesHelper.getGroceryDays();
    }

    private void init() {
        this.mCalendar = Calendar.getInstance();
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // When Date-object is instantiated without parameters, it is set to the current day.
        mCalendar.setTime(new Date());
    }

    public boolean isGroceryDay() {
        int today = mCalendar.get(Calendar.DAY_OF_WEEK);

        for (String groceryDay : mGroceryDays) {
            if (stringWeekDayToInt(groceryDay) == today) return true;
        }

        return false;
    }

    private int stringWeekDayToInt(String groceryDay) {
        switch (groceryDay) {
            case "sunday":
                return 1;
            case "monday":
                return 2;
            case "tuesday":
                return 3;
            case "wednesday":
                return 4;
            case "thursday":
                return 5;
            case "friday":
                return 6;
            case "saturday":
                return 7;
            default:
                return 0;
        }
    }
}