package com.bsuuv.grocerymanager.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DateHelper {

    private Calendar mCalendar;
    private Set<String> mGroceryDays;

    public DateHelper(Context context) {
        this.mCalendar = Calendar.getInstance();
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // When Date-object is instantiated without parameters, it is set to the current day.
        mCalendar.setTime(new Date());

        SharedPreferencesHelper sharedPrefsHelper = new SharedPreferencesHelper(context);
        this.mGroceryDays = sharedPrefsHelper.getGroceryDays();
    }

    public boolean isGroceryDay() {
        int today = mCalendar.get(Calendar.DAY_OF_WEEK);

        for (String groceryDay : mGroceryDays) {
            if (stringWeekDayToInt(groceryDay) == today) return true;
        }

        return false;
    }

    public String getCurrentDate() {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(Calendar.getInstance().getTime());
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
