package com.bsuuv.grocerymanager.db.viewmodel;

import com.bsuuv.grocerymanager.GroceryListManager;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroceryListManagerTests {

    @Mock
    private SharedPreferencesHelper mMockSharedPrefsHelper;

    @Mock
    private List<FoodItemEntity> mModifiedList;

    @Mock
    private List<FoodItemEntity> mCheckedItems;


    @InjectMocks
    private GroceryListManager mGroceryListManager;

    private Set<String> mGroceryDays;
    private List<FoodItemEntity> mFoodItems;
    // TODO: nimeä käyttötarkoituksien mukaan
    private FoodItemEntity mFoodItem1;
    private FoodItemEntity mFoodItem2;
    private FoodItemEntity mFoodItem3;
    private FoodItemEntity mFoodItem4;
    private FoodItemEntity mFoodItem5;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        this.mGroceryDays = new HashSet<>();
        // Set today as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));

        this.mFoodItems = new ArrayList<>();
        this.mFoodItem1 = new FoodItemEntity("Kalja", "Karjala", "Raikasta",
                2, "Packets", 1, 1, "");
        this.mFoodItem2 = new FoodItemEntity("Makkara", "Atria", "Lihaisaa",
                3, "Bags", 2, 2, "");
        this.mFoodItem3 = new FoodItemEntity("Parsakaali", "", "Tylsää",
                5, "Bags", 1, 1, "");
        this.mFoodItem4 = new FoodItemEntity("Kanaa", "Saarioinen", "Tylsää",
                5, "Bags", 1, 1, "");
        this.mFoodItem5 = new FoodItemEntity("Voi", "Valio", "Tylsää",
                5, "Bags", 1, 1, "");


        when(mMockSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);
        when(mMockSharedPrefsHelper.getModifiedList()).thenReturn(mModifiedList);
        when(mMockSharedPrefsHelper.getCheckedItems()).thenReturn(mCheckedItems);
        when(mModifiedList.contains(mFoodItem1)).thenReturn(false);
        when(mModifiedList.contains(mFoodItem2)).thenReturn(false);
        when(mModifiedList.contains(mFoodItem3)).thenReturn(true);
        when(mModifiedList.contains(mFoodItem5)).thenReturn(true);
        when(mCheckedItems.contains(mFoodItem4)).thenReturn(true);
        when(mCheckedItems.contains(mFoodItem5)).thenReturn(true);
    }

    @After
    public void clean() {
        mGroceryDays.clear();
        mFoodItems.clear();
    }

    @Test
    public void getGroceryList_isGroceryDay_countdownValueNotOne() {
        mFoodItems.add(mFoodItem1);
        mFoodItems.add(mFoodItem2);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Was added to list of modified food-items
        verify(mModifiedList).add(mFoodItem1);
        verify(mModifiedList).add(mFoodItem2);

        // Neither of the food-items was added to the grocery list.
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_countdownValueOne() {
        mFoodItems.add(mFoodItem1);
        mFoodItem1.setCountdownValue(1.0);
        mFoodItems.add(mFoodItem2);
        mFoodItem2.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();
        expected.add(mFoodItem1);
        expected.add(mFoodItem2);

        // Was added to list of modified food-items
        verify(mModifiedList).add(mFoodItem1);
        verify(mModifiedList).add(mFoodItem2);

        // Both food-items added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isNotGroceryDay() {
        // Set tomorrow as grocery day
        mGroceryDays.clear();
        mGroceryDays.add(intWeekDayToString(getTodayInt() + 1));

        mFoodItems.add(mFoodItem1);
        mFoodItem1.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Nothing was added to list of modified items
        verifyNoInteractions(mModifiedList);

        // Food-item not added to grocery list, even though countdown value is 1
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_notAddedIfInModifiedList() {
        mFoodItems.add(mFoodItem3);
        mFoodItem3.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();
        expected.add(mFoodItem3);

        // Nothing was added to the list of modified food-items.
        verify(mModifiedList, never()).add(mFoodItem3);

        // The food-item was added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_notAddedIfInCheckedItems() {
        mFoodItems.add(mFoodItem4);
        mFoodItem4.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Nothing was added to the list of modified food-items.
        verify(mCheckedItems, never()).add(mFoodItem4);

        // The food-item was not added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_foodItemInCheckedAndModified() {
        mFoodItems.add(mFoodItem4);
        mFoodItems.add(mFoodItem5);
        mFoodItem4.setCountdownValue(1.0);
        mFoodItem5.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Nothing was added to the list of modified food-items.
        verify(mCheckedItems, never()).add(mFoodItem4);

        // Nothing was added to the list of modified food-items.
        verify(mModifiedList, never()).add(mFoodItem3);

        // The food-item was not added to grocery list
        Assert.assertEquals(expected, actual);
    }


    private int getTodayInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private String intWeekDayToString(int groceryDay) {
        if (groceryDay == 8) groceryDay = 1;
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
