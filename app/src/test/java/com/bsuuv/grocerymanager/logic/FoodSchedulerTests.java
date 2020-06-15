package com.bsuuv.grocerymanager.logic;

import com.bsuuv.grocerymanager.domain.FoodItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(JUnit4.class)
public class FoodSchedulerTests {

    private FoodItem mCorrectFoodItem1;
    private FoodItem mFaultyFoodItem1;
    private List<FoodItem> mFoodItems;

    @Before
    public void initialize() {
        this.mFoodItems = new ArrayList<>();
        this.mCorrectFoodItem1 = new FoodItem("Juusto", "Valio", "Maukasta",
                3, 4, 2, "");
        this.mFaultyFoodItem1 = new FoodItem("Olut", "Karjala", "Helvetin hyvää",
                3, 1, 3, "");
    }

    @Test
    public void returnsCorrectGroceryListOnGroceryDay() {
        Set<String> groceryDays = new HashSet<>();
        // Add current weekday as grocery day
        groceryDays.add(intWeekDayToString(getTodayInt()));

        mFoodItems.add(mCorrectFoodItem1);

        FoodScheduler foodScheduler = new FoodScheduler(groceryDays, mFoodItems);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling the second time, frequency quotient has been incremented, so list containing
        // mCorrectFoodItem1 is returned.
        Assert.assertEquals(mFoodItems, foodScheduler.getGroceryList());
    }

    @Test
    public void returnsEmptyGroceryListOnNotGroceryDay() {
        Set<String> groceryDays = new HashSet<>();
        int tomorrow = (getTodayInt() != 7) ? (getTodayInt() + 1) : 1;

        groceryDays.add(intWeekDayToString(tomorrow));

        mFoodItems.add(mCorrectFoodItem1);

        FoodScheduler foodScheduler = new FoodScheduler(groceryDays, mFoodItems);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is always returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling second time, empty list should still be returned, since it's not grocery
        // day.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
    }

    @Test
    public void generatesCorrectQuotientMap() {
        mFoodItems.add(mCorrectFoodItem1);

        Set<String> groceryDays = new HashSet<>();
        groceryDays.add("sunday");

        FoodScheduler foodScheduler = new FoodScheduler(groceryDays, mFoodItems);
        Map<FoodItem, Double> expected = new HashMap<>();
        expected.put(mCorrectFoodItem1, 0.5);

        Assert.assertEquals(expected, foodScheduler.getFoodItemTracker());
    }

    @Test
    public void throwsExceptionWhenFaultyFoodItem() {
        mFoodItems.add(mFaultyFoodItem1);

        Set<String> groceryDays = new HashSet<>();
        groceryDays.add("sunday");

        boolean throwsException = false;

        try {
            FoodScheduler foodScheduler = new FoodScheduler(groceryDays, mFoodItems);
            foodScheduler.getFoodItemTracker();
        } catch (UnsupportedOperationException exception) {
            throwsException = true;
        }

        Assert.assertTrue(throwsException);
    }

    private int getTodayInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private String intWeekDayToString(int groceryDay) {
        switch (groceryDay) {
            case 1:
                return "sunday";
            case 2:
                return "monday";
            case 3:
                return "tuesday";
            case 4:
                return "wednesday";
            case 5:
                return "thursday";
            case 6:
                return "friday";
            case 7:
                return "saturday";
            default:
                return "";
        }
    }
}
