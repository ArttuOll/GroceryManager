package com.bsuuv.grocerymanager.logic;

import com.bsuuv.grocerymanager.domain.FoodItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FoodScheduler {

    private Set<String> mGroceryDays;
    private List<FoodItem> mFoodItems;
    private int mGroceryDaysAWeek;
    private Map<FoodItem, Double> mFoodItemTracker;
    private SharedPreferencesHelper mSharedPrefsHelper;

    public FoodScheduler(SharedPreferencesHelper sharedPrefsHelper) {
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mGroceryDays = sharedPrefsHelper.getGroceryDays();
        this.mGroceryDaysAWeek = mGroceryDays.size();
        this.mFoodItems = sharedPrefsHelper.getFoodItems();
        this.mFoodItemTracker = getFoodItemTracker();
    }

    public List<FoodItem> getGroceryList() {
        List<FoodItem> groceryList = new ArrayList<>();

        if (isGroceryDay()) {
            for (FoodItem foodItem : mFoodItemTracker.keySet()) {
                Double frequencyQuotient = mFoodItemTracker.get(foodItem);
                double startingFrequencyQuotient = getFrequencyQuotient(foodItem);

                // Calling .get() from a map returns a Double object, which can be null.
                if (frequencyQuotient != null) {
                    if (frequencyQuotient == 1) {
                        groceryList.add(foodItem);

                        // Reset the food-item frequency quotient back to its original value.
                        mFoodItemTracker.put(foodItem, startingFrequencyQuotient);
                    } else {
                        // Increment the food-item frequency quotient in the tracker by adding to
                        // it the original value.
                        mFoodItemTracker.put(foodItem, frequencyQuotient +
                                startingFrequencyQuotient);
                    }
                }
            }
        }

        return groceryList;
    }

    Map<FoodItem, Double> getFoodItemTracker() {
        Map<FoodItem, Double> tracker = mSharedPrefsHelper.getFoodItemTracker();

        // If no tracker was previously saved, create a new one.
        if (tracker.isEmpty()) {
            for (FoodItem foodItem : mFoodItems) {
                tracker.put(foodItem, getFrequencyQuotient(foodItem));
            }
        } else {
            // Add foods not in tracker from mFoodItems.
            mFoodItems.stream().filter(foodItem -> !tracker.containsKey(foodItem))
                    .forEach(foodItem -> tracker.put(foodItem, getFrequencyQuotient(foodItem)));
            deleteFromTrackerFoodsNotInFoodItemsList(tracker);
        }

        if (!tracker.isEmpty()) mSharedPrefsHelper.saveFoodItemTracker(tracker);

        return tracker;
    }

    private void deleteFromTrackerFoodsNotInFoodItemsList(Map<FoodItem, Double> tracker) {
        List<FoodItem> uselessKeys = new ArrayList<>();
        for (FoodItem key : tracker.keySet()) {
            if (!mFoodItems.contains(key)) uselessKeys.add(key);
        }
        for (FoodItem uselessKey : uselessKeys) tracker.remove(uselessKey);
    }

    private double getFrequencyQuotient(FoodItem foodItem) {
        double frequencyQuotient = ((double) foodItem.getFrequency()) /
                (foodItem.getTimeFrame() * mGroceryDaysAWeek);

        if (frequencyQuotient <= 1) {
            return frequencyQuotient;
        } else {
            throw new UnsupportedOperationException("Frequency quotient for food-item: " +
                    foodItem.getLabel() + " was over 1!");
        }
    }

    private boolean isGroceryDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        // When Date-object is instantiated without parameters, it is set to the current day.
        calendar.setTime(new Date());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        for (String groceryDay : mGroceryDays)
            if (stringWeekDayToInt(groceryDay) == today) return true;

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

    public interface TimeFrame {
        int WEEK = 1;
        int TWO_WEEKS = 2;
        int MONTH = 3;
    }
}
