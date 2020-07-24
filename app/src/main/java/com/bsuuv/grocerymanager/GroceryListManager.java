package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.GroceryDayInspector;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroceryListManager {

    public static final String MODIFIED_LIST_KEY = "modifiedlist";
    public static final String CHECKED_ITEMS_KEY = "checkeditems";
    private final int mGroceryDaysAWeek;
    private List<FoodItemEntity> mModifiedList;
    private List<FoodItemEntity> mCheckedItems;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private GroceryDayInspector mInspector;

    public GroceryListManager(SharedPreferencesHelper sharedPreferencesHelper) {
        this.mSharedPrefsHelper = sharedPreferencesHelper;

        Set<String> mGroceryDays = mSharedPrefsHelper.getGroceryDays();
        this.mGroceryDaysAWeek = mGroceryDays.size();
        this.mInspector = new GroceryDayInspector(sharedPreferencesHelper);

        this.mModifiedList = mSharedPrefsHelper.getList(MODIFIED_LIST_KEY);
        this.mCheckedItems = mSharedPrefsHelper.getList(CHECKED_ITEMS_KEY);
    }

    public List<FoodItemEntity> getGroceryItemsFromFoodItems(List<FoodItemEntity> foodItems) {
        List<FoodItemEntity> groceryItems = new ArrayList<>();
        if (mInspector.isGroceryDay()) {
            for (FoodItemEntity foodItem : foodItems) {
                // Each grocery day the countdown value is incremented by the value of frequency
                // quotient.
                // When it reaches 1, it's time for the item to appear in the grocery list.
                double countdownValue = foodItem.getCountdownValue();

                // If the frequency quotient for a food-item has reached 1, put it to grocery
                // list. The value can be greater than one if the number of grocery days decreases
                // while the countdownValue has already been assigned a value.
                if (countdownValue >= 1 && !mCheckedItems.contains(foodItem)) {
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

    public List<FoodItemEntity> getModifiedList() {
        return mModifiedList;
    }

    public List<FoodItemEntity> getCheckedItems() {
        return mCheckedItems;
    }

    public void saveBuffers(List<FoodItemEntity> modifiedItems,
                            List<FoodItemEntity> checkedItems) {
        mSharedPrefsHelper.saveList(modifiedItems, MODIFIED_LIST_KEY);
        mSharedPrefsHelper.saveList(checkedItems, CHECKED_ITEMS_KEY);
    }

    public void clearBuffers() {
        mSharedPrefsHelper.clearList(MODIFIED_LIST_KEY);
        mSharedPrefsHelper.clearList(CHECKED_ITEMS_KEY);
    }

    private double getFrequencyQuotient(FoodItemEntity foodItem) {
        // A double representing the frequency in which a food-item should appear in
        // grocery list. If there's one grocery day a week and a food-item is to be had
        // once every two weeks, then the frequency quotient is
        // 1 per week / (1 grocery day * 2 weeks) = 1/2.
        return ((double) foodItem.getFrequency()) /
                (foodItem.getTimeFrame() * mGroceryDaysAWeek);
    }


}
