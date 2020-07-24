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
    private FoodItemEntity mFoodItem1;
    private FoodItemEntity mFoodItem2;
    private FoodItemEntity mModifiedFoodItem;
    private FoodItemEntity mCheckedFoodItem;
    private FoodItemEntity mModifiedCheckedFoodItem;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        this.mGroceryDays = new HashSet<>();
        // Set today as grocery day
        mGroceryDays.add(intWeekDayToString(getTodayInt()));

        this.mFoodItems = new ArrayList<>();
        this.mFoodItem1 = new FoodItemEntity("Kalja", "Karjala", "Raikasta",
                2, "Packets", 2, 1, "", 0.0);
        this.mFoodItem2 = new FoodItemEntity("Makkara", "Atria", "Lihaisaa",
                3, "Bags", 3, 1, "", 0.0);
        this.mModifiedFoodItem = new FoodItemEntity("Parsakaali", "", "Tylsää",
                5, "Bags", 1, 1, "", 0.0);
        this.mCheckedFoodItem = new FoodItemEntity("Kanaa", "Saarioinen", "Tylsää",
                5, "Bags", 1, 1, "", 0.0);
        this.mModifiedCheckedFoodItem = new FoodItemEntity("Voi", "Valio", "Tylsää",
                5, "Bags", 1, 1, "", 0.0);


        when(mMockSharedPrefsHelper.getGroceryDays()).thenReturn(mGroceryDays);
        when(mMockSharedPrefsHelper.getList(GroceryListManager.MODIFIED_LIST_KEY))
                .thenReturn(mModifiedList);
        when(mMockSharedPrefsHelper.getList(GroceryListManager.CHECKED_ITEMS_KEY))
                .thenReturn(mCheckedItems);

        when(mModifiedList.contains(mFoodItem1)).thenReturn(false);
        when(mModifiedList.contains(mFoodItem2)).thenReturn(false);
        when(mModifiedList.contains(mModifiedFoodItem)).thenReturn(true);
        when(mModifiedList.contains(mModifiedCheckedFoodItem)).thenReturn(true);

        when(mCheckedItems.contains(mCheckedFoodItem)).thenReturn(true);
        when(mCheckedItems.contains(mModifiedCheckedFoodItem)).thenReturn(true);
    }

    @After
    public void clean() {
        mGroceryDays.clear();
        mFoodItems.clear();
        mFoodItem1.setCountdownValue(0.0);
        mFoodItem2.setCountdownValue(0.0);
        mModifiedFoodItem.setCountdownValue(0.0);
        mCheckedFoodItem.setCountdownValue(0.0);
        mModifiedCheckedFoodItem.setCountdownValue(0.0);
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
        mFoodItem1.setCountdownValue(1.0);
        mFoodItems.add(mFoodItem1);
        mFoodItem2.setCountdownValue(1.0);
        mFoodItems.add(mFoodItem2);

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
        mModifiedFoodItem.setCountdownValue(1.0);
        mFoodItems.add(mModifiedFoodItem);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();
        expected.add(mModifiedFoodItem);

        // Nothing was added to the list of modified food-items.
        verify(mModifiedList, never()).add(mModifiedFoodItem);

        // The food-item was added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_notAddedIfInCheckedItems() {
        mFoodItems.add(mCheckedFoodItem);
        mCheckedFoodItem.setCountdownValue(1.0);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Nothing was added to the list of modified food-items.
        verify(mCheckedItems, never()).add(mCheckedFoodItem);

        // The food-item was not added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_isGroceryDay_foodItemInCheckedAndModified() {
        mCheckedFoodItem.setCountdownValue(1.0);
        mModifiedCheckedFoodItem.setCountdownValue(1.0);
        mFoodItems.add(mCheckedFoodItem);
        mFoodItems.add(mModifiedCheckedFoodItem);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);
        List<FoodItemEntity> expected = new ArrayList<>();

        // Nothing was added to the list of modified food-items.
        verify(mCheckedItems, never()).add(mCheckedFoodItem);

        // Nothing was added to the list of modified food-items.
        verify(mModifiedList, never()).add(mModifiedFoodItem);

        // The food-item was not added to grocery list
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroceryList_itemsAppearInCorrectFrequencies() {
        mFoodItem1.setCountdownValue(0.5);
        mFoodItem2.setCountdownValue(0.33);
        mFoodItems.add(mFoodItem1);
        mFoodItems.add(mFoodItem2);

        mGroceryListManager = new GroceryListManager(mMockSharedPrefsHelper);
        List<FoodItemEntity> actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);

        // Neither should appear since countdown values are not one
        Assert.assertTrue(!actual.contains(mFoodItem1) && !actual.contains(mFoodItem2));

        actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);

        // mFoodItem1 countdown value should be incremented to one
        Assert.assertTrue(actual.contains(mFoodItem1) && !actual.contains(mFoodItem2));

        actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);

        // mFoodItem1 countdown value should be reset and mFoodItem2 should be 1
        Assert.assertTrue(!actual.contains(mFoodItem1) && actual.contains(mFoodItem2));

        actual = mGroceryListManager.getGroceryItemsFromFoodItems(mFoodItems);

        // mFoodItem1 should reappear and mFoodItem2 should be reset
        Assert.assertTrue(actual.contains(mFoodItem1) && !actual.contains(mFoodItem2));
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
