package com.bsuuv.grocerymanager.logic;

import com.bsuuv.grocerymanager.domain.FoodItem;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FoodSchedulerTests {

    private FoodItem mCorrectFoodItem1;
    private FoodItem mFaultyFoodItem1;
    private List<FoodItem> mFoodItems;
    @Mock
    SharedPreferencesHelper sharedPrefsHelper;
    private Set<String> mGroceryDays;
    private Map<FoodItem, Double> mFoodItemTracker;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        this.mFoodItems = new ArrayList<>();
        this.mCorrectFoodItem1 = new FoodItem("Juusto", "Valio", "Maukasta",
                3, 4, 2, "");
        this.mFaultyFoodItem1 = new FoodItem("Olut", "Karjala", "Helvetin hyvää",
                3, 1, 3, "");
        this.mGroceryDays = new HashSet<>();
        this.mFoodItemTracker = new HashMap<>();

        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(mFoodItemTracker);
        when(sharedPrefsHelper.getFoodItems()).thenReturn(mFoodItems);
        when(sharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);
    }

    @Test
    public void returnsCorrectGroceryListOnGroceryDay() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mCorrectFoodItem1);
        mFoodItemTracker.put(mCorrectFoodItem1, 0.5);

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling the second time, frequency quotient has been incremented, so list containing
        // mCorrectFoodItem1 is returned.
        Assert.assertEquals(mFoodItems, foodScheduler.getGroceryList());
    }

    @Test
    public void returnsEmptyGroceryListOnNotGroceryDay() {
        int tomorrow = (getTodayInt() != 7) ? (getTodayInt() + 1) : 1;

        mGroceryDays.add(intWeekDayToString(tomorrow));
        mFoodItems.add(mCorrectFoodItem1);
        mFoodItemTracker.put(mCorrectFoodItem1, 0.5);

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        // When calling .getGroceryList for the first time, frequency quotient for mCorrectFoodItem1
        // is 0.5, so an empty list is always returned.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
        // When calling second time, empty list should still be returned, since it's not grocery
        // day.
        Assert.assertEquals(new ArrayList<>(), foodScheduler.getGroceryList());
    }

    @Test
    public void generatesCorrectQuotientMap() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mCorrectFoodItem1);
        // Override previous stubbing of the same method. Null is required for
        // FoodScheduler.getFoodItemTracker to create a new quotinent map.
        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(null);

        FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);

        Map<FoodItem, Double> expected = new HashMap<>();
        expected.put(mCorrectFoodItem1, 0.5);

        Assert.assertEquals(expected, foodScheduler.getFoodItemTracker());
    }

    @Test
    public void throwsExceptionWhenFaultyFoodItemQuotient() {
        // Add current weekday as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));
        mFoodItems.add(mFaultyFoodItem1);
        // Doesn't matter what is returned when getFoodItemTracker is called,
        // getFoodItemFrequencyQuotient is called anyway.
        when(sharedPrefsHelper.getFoodItemTracker()).thenReturn(null);

        boolean throwsException = false;

        try {
            FoodScheduler foodScheduler = new FoodScheduler(sharedPrefsHelper);
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
