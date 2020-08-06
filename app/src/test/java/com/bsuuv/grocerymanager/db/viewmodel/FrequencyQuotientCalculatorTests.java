package com.bsuuv.grocerymanager.db.viewmodel;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FrequencyQuotientCalculatorTests {

    @Mock
    private SharedPreferencesHelper mSharedPrefsHelper;

    @InjectMocks
    private FrequencyQuotientCalculator mFqCalculator;

    private Set<String> mGroceryDays;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        this.mGroceryDays = new HashSet<>();
    }

    @Test
    public void getFrequencyQuotient_singleGroceryDay() {
        mGroceryDays.add("tuesday");

        when(mSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);

        FoodItemEntity foodItem = new FoodItemEntity("Olut", "Karjala",
                "Raikasta", 2, "Packets", 2, 1,
                "", 0.0);

        Assert.assertEquals(0.5, mFqCalculator.getFrequencyQuotient(foodItem)
                , 0.001);

        Assert.assertEquals(0.5, mFqCalculator.getFrequencyQuotient(1, 2, 1),
                0.001);
    }

    @Test
    public void getFrequencyQuotient_multipleGroceryDays() {
        mGroceryDays.add("tuesday");
        mGroceryDays.add("wednesday");
        mGroceryDays.add("saturday");

        when(mSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);

        FoodItemEntity foodItem = new FoodItemEntity("Olut", "Karjala",
                "Raikasta", 2, "Packets", 2, 1,
                "", 0.0);

        Assert.assertEquals(0.15, mFqCalculator.getFrequencyQuotient(foodItem)
                , 0.001);

        Assert.assertEquals(0.15, mFqCalculator.getFrequencyQuotient(1, 2, 3),
                0.001);
    }
}
