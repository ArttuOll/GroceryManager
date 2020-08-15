package com.bsuuv.grocerymanager.data;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
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
  private GroceryListState mState;
  @Mock
  private SharedPreferencesHelper mSharedPrefsHelper;
  @Mock
  private List<FoodItemEntity> mModifiedList;
  @Mock
  private List<FoodItemEntity> mCheckedItems;
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
    initMembers();
    configureMocks();
  }

  private void initMembers() {
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
  }

  private void configureMocks() {
    when(mState.getRemovedItems()).thenReturn(mCheckedItems);
    when(mCheckedItems.contains(mCheckedFoodItem)).thenReturn(true);
    when(mCheckedItems.contains(mModifiedCheckedFoodItem)).thenReturn(true);
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
  public void getGroceryList_countdownValueNotOne() {
    mFoodItems.add(mFoodItem1);
    mFoodItems.add(mFoodItem2);

    mGroceryListExtractor = new GroceryListExtractor(mState, mSharedPrefsHelper);
    List<FoodItemEntity> actual = mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
    List<FoodItemEntity> expected = new ArrayList<>();

    // Was added to list of modified food-items
    verify(mState).markAsModified(mFoodItem1);
    verify(mState).markAsModified(mFoodItem2);

    // Neither of the food-items was added to the grocery list.
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void getGroceryList_countdownValueOne() {
    mFoodItem1.setCountdownValue(1.0);
    mFoodItems.add(mFoodItem1);
    mFoodItem2.setCountdownValue(1.0);
    mFoodItems.add(mFoodItem2);

    mGroceryListExtractor = new GroceryListExtractor(mState, mSharedPrefsHelper);
    mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);

    verify(mState).markAsModified(mFoodItem1);
    verify(mState).markAsModified(mFoodItem2);
    verify(mState).addToGroceryList(mFoodItem1);
    verify(mState).addToGroceryList(mFoodItem2);
  }

  @Test
  public void getGroceryList_notAddedIfInModifiedList() {
    mModifiedFoodItem.setCountdownValue(1.0);
    mFoodItems.add(mModifiedFoodItem);

    mGroceryListExtractor = new GroceryListExtractor(mState, mSharedPrefsHelper);
    mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);

    verify(mModifiedList, never()).add(mModifiedFoodItem);
    verify(mState).addToGroceryList(mModifiedFoodItem);
  }

  @Test
  public void getGroceryList_notAddedIfInCheckedItems() {
    mFoodItems.add(mCheckedFoodItem);
    mCheckedFoodItem.setCountdownValue(1.0);

    mGroceryListExtractor = new GroceryListExtractor(mState, mSharedPrefsHelper);
    List<FoodItemEntity> actual = mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
    List<FoodItemEntity> expected = new ArrayList<>();

    verify(mCheckedItems, never()).add(mCheckedFoodItem);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getGroceryList_foodItemInCheckedAndModified() {
    mCheckedFoodItem.setCountdownValue(1.0);
    mModifiedCheckedFoodItem.setCountdownValue(1.0);
    mFoodItems.add(mCheckedFoodItem);
    mFoodItems.add(mModifiedCheckedFoodItem);

    mGroceryListExtractor = new GroceryListExtractor(mState, mSharedPrefsHelper);
    List<FoodItemEntity> actual = mGroceryListExtractor.extractGroceryListFromFoodItems(mFoodItems);
    List<FoodItemEntity> expected = new ArrayList<>();

    verify(mCheckedItems, never()).add(mCheckedFoodItem);
    verify(mModifiedList, never()).add(mModifiedFoodItem);
    Assert.assertEquals(expected, actual);
  }
}
