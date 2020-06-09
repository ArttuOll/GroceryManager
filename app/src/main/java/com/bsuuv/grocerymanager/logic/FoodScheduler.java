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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FoodScheduler {

    private Set<String> mGroceryDays;
    private Context mContext;
    private List<FoodItem> mFoodItems;
    private int todayInt;
    private int mGroceryDaysAWeek;
    private Map<FoodItem, Double> mFoodItemTracker;
    private SharedPreferences mPreferences;

    public FoodScheduler(Context context) {
        this.mContext = context;
        this.mGroceryDays = mPreferences.getStringSet("grocerydays", new HashSet<>());
        this.mGroceryDaysAWeek = mGroceryDays.size();
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mFoodItems = getFoodItemsFromSharedPreferences();
        this.todayInt = getTodayInt();
        this.mFoodItemTracker = getFoodItemTracker();
    }

    private Map<FoodItem, Double> getFoodItemTracker() {
        Map<FoodItem, Double> foodItemQuotientMap = new HashMap<>();
        for (FoodItem foodItem : mFoodItems) {
            double foodItemFrequencyQuotient =
                    ((double) foodItem.getFrequency()) / (foodItem.getTimeFrame() * mGroceryDaysAWeek);
            foodItemQuotientMap.put(foodItem, foodItemFrequencyQuotient);
        }

        return foodItemQuotientMap;
    }

    public List<FoodItem> getGroceryList() {
        List<FoodItem> groceryList = new ArrayList<>();

        if (isGroceryDay()) {
            for (FoodItem foodItem : mFoodItemTracker.keySet()) {
                Double frequencyQuotient = mFoodItemTracker.get(foodItem);

                if (frequencyQuotient != null) {
                    if (frequencyQuotient == 1) { groceryList.add(foodItem); } else {
                        mFoodItemTracker.put(foodItem, frequencyQuotient + frequencyQuotient);
                    }
                }
            }
        } else {
            throw new IllegalStateException(mContext.getString(R.string.error_not_grocery_day));
        }

        return groceryList;
    }

    private List<FoodItem> getFoodItemsFromSharedPreferences() {
        String jsonFoodItems = mPreferences.getString(Configurations.FOOD_ITEMS_KEY, "");
        Type listType = new TypeToken<List<FoodItem>>() {
        }.getType();
        Gson gson = new Gson();

        List<FoodItem> foodItems = gson.fromJson(jsonFoodItems, listType);

        return (foodItems != null) ? foodItems : new ArrayList<>();
    }

    private int getTodayInt() {
        Calendar calendar = Calendar.getInstance();

        // When Date is instantiated with zero parameters, it's value is set to today.
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private boolean isGroceryDay() {
        for (String groceryDay : mGroceryDays) {
            int groceryDayInt = stringWeekDayToInt(groceryDay);

            if (groceryDayInt == todayInt) {
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
