package com.bsuuv.grocerymanager.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.logic.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GroceryItemViewModel extends AndroidViewModel {
    private final Set<String> mGroceryDays;
    private final int mGroceryDaysAWeek;
    private FoodItemRepository mRepository;
    private LiveData<List<FoodItemEntity>> mGroceryItems;
    private List<FoodItemEntity> mUpdateList;
    private SharedPreferencesHelper mSharedPrefsHelper;

    public GroceryItemViewModel(Application application) {
        super(application);
        this.mRepository = new FoodItemRepository(application);
        this.mGroceryItems = mRepository.getFoodItems();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application);
        this.mSharedPrefsHelper = new SharedPreferencesHelper(sharedPrefs);

        this.mGroceryDays = mSharedPrefsHelper.getGroceryDays();
        this.mGroceryDaysAWeek = mGroceryDays.size();

        this.mUpdateList = mSharedPrefsHelper.getUpdateList();
    }

    public LiveData<List<FoodItemEntity>> getGroceryList() {
        return Transformations.map(mGroceryItems, this::getGroceryItemsFromFoodItems);
    }

    public void delete(FoodItemEntity foodItem) {
        mRepository.delete(foodItem);
    }

    @Override
    public void onCleared() {
        for (FoodItemEntity foodItem : mUpdateList) {
            mRepository.update(foodItem);
        }
    }

    private List<FoodItemEntity> getGroceryItemsFromFoodItems(List<FoodItemEntity> foodItems) {
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
                mUpdateList.add(foodItem);
            }
        }
        return groceryItems;
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
}
