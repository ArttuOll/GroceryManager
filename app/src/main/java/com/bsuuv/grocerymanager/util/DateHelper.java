package com.bsuuv.grocerymanager.util;

import android.content.Context;

import com.bsuuv.grocerymanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DateHelper {

    private Calendar mCalendar;
    private Set<String> mGroceryDays;
    private Context mContext;

    public DateHelper(Context context) {
        this.mCalendar = Calendar.getInstance();
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // When Date-object is instantiated without parameters, it is set to the current day.
        mCalendar.setTime(new Date());

        this.mContext = context;
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
        String[] daysOfWeek = mContext.getResources().getStringArray(R.array.daysofweek_datehelper);

        if (daysOfWeek[0].equals(groceryDay)) {
            return 1;
        } else if (daysOfWeek[1].equals(groceryDay)) {
            return 2;
        } else if (daysOfWeek[2].equals(groceryDay)) {
            return 3;
        } else if (daysOfWeek[3].equals(groceryDay)) {
            return 4;
        } else if (daysOfWeek[4].equals(groceryDay)) {
            return 5;
        } else if (daysOfWeek[5].equals(groceryDay)) {
            return 6;
        } else if (daysOfWeek[6].equals(groceryDay)) return 7;

        return 0;
    }
}
