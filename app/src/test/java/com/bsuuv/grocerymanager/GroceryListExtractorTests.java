package com.bsuuv.grocerymanager;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.util.TimeFrame;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GroceryListExtractorTests {

  @Mock
  private SharedPreferencesHelper mSharedPrefsHelper;

  @Mock
  private List<FoodItemEntity> mModifiedList;

  @Mock
  private List<FoodItemEntity> mCheckedItems;

  @Mock
  private DateHelper mDateHelper;

  @Mock
  private FrequencyQuotientCalculator mCalculator;

  @InjectMocks
  private GroceryListExtractor mGroceryListExtractor;

  private List<FoodItemEntity> mFoodItems;
  private FoodItemEntity mFoodItem1;
  private FoodItemEntity mFoodItem2;
  private FoodItemEntity mModifiedFoodItem;
  private FoodItemEntity mCheckedFoodItem;
  private FoodItemEntity mModifiedCheckedFoodItem;

  @Before
  public void initialize() {
    MockitoAnnotations.initMocks(this);

    this.mFoodItems = new ArrayList<>();
    this.mFoodItem1 = new FoodItemEntity("Kalja", "Karjala", "Raikasta",
        2, "Packets", TimeFrame.TWO_WEEKS, 1, "", 0.0);
    this.mFoodItem2 = new FoodItemEntity("Makkara", "Atria", "Lihaisaa",
        3, "Bags", TimeFrame.MONTH, 1, "", 0.0);
    this.mModifiedFoodItem = new FoodItemEntity("Parsakaali", "", "Tylsää",
        5, "Bags", TimeFrame.WEEK, 1, "", 0.0);
    this.mCheckedFoodItem = new FoodItemEntity("Kanaa", "Saarioinen",
        "Tylsää",
        5, "Bags", TimeFrame.WEEK, 1, "", 0.0);
    this.mModifiedCheckedFoodItem = new FoodItemEntity("Voi", "Valio",
        "Tylsää",
        5, "Bags", TimeFrame.WEEK, 1, "", 0.0);

    when(mSharedPrefsHelper.getList(GroceryListExtractor.MODIFIED_ITEMS_KEY))
        .thenReturn(mModifiedList);
    when(mSharedPrefsHelper.getList(GroceryListExtractor.CHECKED_ITEMS_KEY))
        .thenReturn(mCheckedItems);

    when(mModifiedList.contains(mFoodItem1)).thenReturn(false);
    when(mModifiedList.contains(mFoodItem2)).thenReturn(false);
    when(mModifiedList.contains(mModifiedFoodItem)).thenReturn(true);
    when(mModifiedList.contains(mModifiedCheckedFoodItem)).thenReturn(true);

    when(mCheckedItems.contains(mCheckedFoodItem)).thenReturn(true);
    when(mCheckedItems.contains(mModifiedCheckedFoodItem)).thenReturn(true);

    when(mDateHelper.isGroceryDay()).thenReturn(true);
  }

  @After
  public void clean() {
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

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
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

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
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
    when(mDateHelper.isGroceryDay()).thenReturn(false);

    mFoodItems.add(mFoodItem1);
    mFoodItem1.setCountdownValue(1.0);

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
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

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
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

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
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

    mGroceryListExtractor = new GroceryListExtractor(mSharedPrefsHelper,
        mDateHelper, mCalculator);
    List<FoodItemEntity> actual =
        mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
    List<FoodItemEntity> expected = new ArrayList<>();

    // Nothing was added to the list of modified food-items.
    verify(mCheckedItems, never()).add(mCheckedFoodItem);

    // Nothing was added to the list of modified food-items.
    verify(mModifiedList, never()).add(mModifiedFoodItem);

    // The food-item was not added to grocery list
    Assert.assertEquals(expected, actual);
  }
}
