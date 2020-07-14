package com.bsuuv.grocerymanager;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps track of which foods should appear in the grocery list.
 */
public class FoodScheduler {

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private final FoodItemRepository mFoodItemRepository;
    private Set<String> mGroceryDays;
    private LiveData<List<FoodItemEntity>> mFoodItemLiveData;
    private int mGroceryDaysAWeek;
    private List<FoodItemEntity> mFoodItems;
    private androidx.lifecycle.Observer<List<FoodItemEntity>> mObserver;

    public FoodScheduler(Application application) {
        this.mFoodItemRepository = new FoodItemRepository(application);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);
        this.mGroceryDays = sharedPrefs.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
        this.mGroceryDaysAWeek = mGroceryDays.size();
        this.mFoodItemLiveData = mFoodItemRepository.getFoodItems();
        this.mObserver = foodItemEntities -> mFoodItems = foodItemEntities;
        mFoodItemLiveData.observeForever(mObserver);
    }

    /**
     * Gives a list of all food-items scheduled for the current grocery day.
     *
     * @return <code>List</code> containing <code>FoodItems</code>.
     */
    public List<FoodItemEntity> getGroceryList() {
        List<FoodItemEntity> groceryList = new ArrayList<>();
        // TODO: should I rather observe the LiveData?
        List<FoodItemEntity> foodItems = mFoodItemLiveData.getValue();

        if (isGroceryDay() && mFoodItems != null) {
            for (FoodItemEntity foodItem : mFoodItems) {
                // Each grocery day the countdown value is incremented by the value of frequency
                // quotient.
                // When it reaches 1, it's time for the item to appear in the grocery list.
                double countdownValue = foodItem.getCountdownValue();

                // If the frequency quotient for a food-item has reached 1, put it to grocery
                // list. The value can be greater than one if the number of grocery days decreases
                // while the countdownValue has already been assigned a value.
                if (countdownValue >= 1) {
                    groceryList.add(foodItem);

                    // Reset the food-item frequency quotient.
                    foodItem.setCountdownValue(0);
                } else {
                    double frequencyQuotient = getFrequencyQuotient(foodItem);

                    // Increment the food-item countdown value by adding to
                    // it the original value.
                    foodItem.setCountdownValue(countdownValue + frequencyQuotient);
                }
                mFoodItemRepository.update(foodItem);
            }
        }

        return groceryList;
    }

    public void removeObserver() {
        mFoodItemLiveData.removeObserver(mObserver);
    }

    private double getFrequencyQuotient(FoodItemEntity foodItem) {
        // A double representing the frequency in which a food-item should appear in
        // grocery list. If there's one grocery day a week and a food-item is to be had
        // once every two weeks, then the frequency quotient is
        // 1 per week / (1 grocery day * 2 weeks) = 1/2.
        return ((double) foodItem.getFrequency()) /
                (foodItem.getTimeFrame() * mGroceryDaysAWeek);
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
        int MONTH = 4;
    }
}
