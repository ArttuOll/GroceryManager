package com.bsuuv.grocerymanager;

import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bsuuv.grocerymanager.logic.FoodScheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class FoodSchedulerTests {

    private FoodScheduler mFoodScheduler;
    private List<FoodItem> mTestFoodItems;
    private int mCurrentDayOfWeekInt;

    @Before
    public void initialize() {
        this.mTestFoodItems = new ArrayList<>();
        mTestFoodItems.add(new FoodItem("Makkara", "Atria", "Helvetin hyvää", 10, 1, 5, ""));
        mTestFoodItems.add(new FoodItem("Juusto", "Valio", "Maukasta", 3, 4, 2, ""));

        Set<String> mTestGroceryDays = new HashSet<>();
        // Always add the current day as grocery day.
        mTestGroceryDays.add(DayOfWeek.of(getCurrentWeekDayInt()).toString());
        this.mFoodScheduler = new FoodScheduler(mTestGroceryDays, mTestFoodItems);
        this.mCurrentDayOfWeekInt = DayOfWeek.of(getCurrentWeekDayInt()).getValue();
    }

    @Test
    public void returnsCorrectListOnCorrectDay() {
        List<FoodItem> actual = mFoodScheduler.getGroceryList(mCurrentDayOfWeekInt);

        Assert.assertEquals(mTestFoodItems, actual);
    }

    private int getCurrentWeekDayInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
