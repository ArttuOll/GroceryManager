package com.bsuuv.grocerymanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bsuuv.grocerymanager.logic.FoodScheduler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FoodSchedulerTests {

    @InjectMocks
    FoodScheduler mFoodScheduler;
    @Mock
    SharedPreferences sharedPreferences;
    @Mock
    Context context;
    private List<FoodItem> mTestFoodItems;
    private Set<String> mTestGroceryDays;
    private int mCurrentDayOfWeekInt;

    @Before
    public void initialize() {
        this.mTestFoodItems = new ArrayList<>();
        mTestFoodItems.add(new FoodItem("Makkara", "Atria", "Helvetin hyvää", 10, 1, 5, ""));
        mTestFoodItems.add(new FoodItem("Juusto", "Valio", "Maukasta", 3, 4, 2, ""));

        this.mTestGroceryDays = new HashSet<>();
        // Always add the current day as grocery day.
        mTestGroceryDays.add(DayOfWeek.of(getCurrentWeekDayInt()).toString());
//        this.mFoodScheduler = new FoodScheduler(context, mTestFoodItems);
        this.mCurrentDayOfWeekInt = DayOfWeek.of(getCurrentWeekDayInt()).getValue();

        MockitoAnnotations.initMocks(this);
//        when(context.getSharedPreferences(anyString(), anyInt()))
//                .thenReturn(sharedPreferences);
    }

    @Test
    public void returnsCorrectListOnCorrectDay() {
        when(sharedPreferences.getStringSet(anyString(), any()))
                .thenReturn(mTestGroceryDays);

        List<FoodItem> actual = mFoodScheduler.getGroceryList(mCurrentDayOfWeekInt);

        Assert.assertEquals(mTestFoodItems, actual);
    }

    private int getCurrentWeekDayInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
