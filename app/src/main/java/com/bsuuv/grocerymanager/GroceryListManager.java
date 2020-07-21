package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GroceryListManager {
    private final Set<String> mGroceryDays;
    private final int mGroceryDaysAWeek;
    private List<FoodItemEntity> mModifiedList;
    private SharedPreferencesHelper mSharedPrefsHelper;

    public GroceryListManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.mSharedPrefsHelper = sharedPreferencesHelper;
        this.mGroceryDays = mSharedPrefsHelper.getGroceryDays();
        this.mGroceryDaysAWeek = mGroceryDays.size();

        this.mModifiedList = mSharedPrefsHelper.getModifiedList();
    }

    public List<FoodItemEntity> getGroceryItemsFromFoodItems(List<FoodItemEntity> foodItems) {
        List<FoodItemEntity> groceryItems = new ArrayList<>();
        if (isGroceryDay()) {
            for (FoodItemEntity foodItem : foodItems) {
                // Each grocery day the countdown value is incremented by the value of frequency
                // quotient.
                // When it reaches 1, it's time for the item to appear in the grocery list.
                double countdownValue = foodItem.getCountdownValue();

                // If the frequency quotient for a food-item has reached 1, put it to grocery
                // list. The value can be greater than one if the number of grocery days decreases
                // while the countdownValue has already been assigned a value.
                if (countdownValue >= 1) {
                    groceryItems.add(foodItem);

                    // Reset the food-item frequency quotient.
                    foodItem.setCountdownValue(0);
                } else {
                    double frequencyQuotient = getFrequencyQuotient(foodItem);

                    // Increment the food-item countdown value by adding to
                    // it the original value.
                    foodItem.setCountdownValue(countdownValue + frequencyQuotient);
                }
                if (!mModifiedList.contains(foodItem)) mModifiedList.add(foodItem);
            }
        }
        return groceryItems;
    }

    public boolean isGroceryDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // When Date-object is instantiated without parameters, it is set to the current day.
        calendar.setTime(new Date());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        for (String groceryDay : mGroceryDays)
            if (stringWeekDayToInt(groceryDay) == today) return true;

        return false;
    }


    public List<FoodItemEntity> getModifiedList() {
        return mModifiedList;
    }

    public void saveUpdateList(List<FoodItemEntity> mModifiedList) {
        mSharedPrefsHelper.saveModifiedList(mModifiedList);
    }

    public void clearUpdateList() {
        mSharedPrefsHelper.clearUpdateList();
    }

    private double getFrequencyQuotient(FoodItemEntity foodItem) {
        // A double representing the frequency in which a food-item should appear in
        // grocery list. If there's one grocery day a week and a food-item is to be had
        // once every two weeks, then the frequency quotient is
        // 1 per week / (1 grocery day * 2 weeks) = 1/2.
        return ((double) foodItem.getFrequency()) /
                (foodItem.getTimeFrame() * mGroceryDaysAWeek);
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
