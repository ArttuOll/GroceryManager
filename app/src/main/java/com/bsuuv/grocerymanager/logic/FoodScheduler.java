package com.bsuuv.grocerymanager.logic;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.activities.Configurations;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodScheduler {

    private Set<String> mGroceryDays;
    private Context mContext;
    private List<FoodItem> mFoodItems;
    private List<FoodItem> mBiweeklyItems;
    private List<FoodItem> mWeeklyItems;
    private List<FoodItem> mMonthlyItems;
    private int todayInt;
    private int mGroceryDayCounter;
    private int mGroceryDaysInWeek;
    private SharedPreferences mPreferences;

    public FoodScheduler(Context context) {
        this.mContext = context;
        this.mGroceryDayCounter = 0;
        this.mGroceryDaysInWeek = (mGroceryDays != null) ? mGroceryDays.size() : 0;
        this.mBiweeklyItems = new ArrayList<>();
        this.mWeeklyItems = new ArrayList<>();
        this.mMonthlyItems = new ArrayList<>();
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mGroceryDays = mPreferences.getStringSet("grocerydays", new HashSet<>());
        this.mFoodItems = new ArrayList<>();

        getFoodItemsFromSharedPreferences();

        setTodayInt();

        fillChecklistsAndCountersWithDefaultValues();
    }

    public List<FoodItem> getGroceryList() {
        if (!isGroceryDay()) {
            throw new IllegalStateException(mContext.getString(R.string.error_not_grocery_day));
        }

        List<FoodItem> groceryList = new ArrayList<>();

        // When counter's value is multiple of four times grocery days a week, add monthly items.
        if (mGroceryDayCounter % (4 * mGroceryDaysInWeek) == 0) groceryList.addAll(mMonthlyItems);
        // When counter's value is multiple of grocery days a week, add weekly items.
        if (mGroceryDayCounter % mGroceryDaysInWeek == 0) groceryList.addAll(mWeeklyItems);
        // Since max frequency is twice a week and max grocery days a week is two, biweekly items
        // are added always.
        groceryList.addAll(mBiweeklyItems);

        return groceryList;
    }

    private void getFoodItemsFromSharedPreferences() {
        String jsonFoodItems = mPreferences.getString(Configurations.FOOD_ITEMS_KEY, "");
        Type listType = new TypeToken<List<FoodItem>>() {
        }.getType();

        Gson gson = new Gson();
        List<FoodItem> foodItems = gson.fromJson(jsonFoodItems, listType);
        if (foodItems != null) this.mFoodItems = foodItems;
    }

    private void fillChecklistsAndCountersWithDefaultValues() {
        mFoodItems.forEach(foodItem -> {
            switch (foodItem.getTimeFrame()) {
                case TimeFrame.WEEK:
                    mBiweeklyItems.add(foodItem);
                case TimeFrame.TWO_WEEKS:
                    mWeeklyItems.add(foodItem);
                case TimeFrame.MONTH:
                    mMonthlyItems.add(foodItem);
            }
        });
    }

    private void setTodayInt() {
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        this.todayInt = calendar.get(Calendar.DAY_OF_WEEK);
    }

    private boolean isGroceryDay() {
        for (String groceryDay : mGroceryDays) {
            int groceryDayInt = stringWeekDayToInt(groceryDay);

            if (groceryDayInt == todayInt) {
                mGroceryDayCounter++;
                return true;
            }
        }

        return false;
    }

    private int stringWeekDayToInt(String groceryDay) {
        switch (groceryDay) {
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;
            default:
                return 0;
        }
    }

    public interface TimeFrame {
        int WEEK = 1;
        int TWO_WEEKS = 2;
        int MONTH = 3;
    }
}
