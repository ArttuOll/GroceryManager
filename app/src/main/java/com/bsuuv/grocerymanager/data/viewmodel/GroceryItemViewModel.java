package com.bsuuv.grocerymanager.data.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.bsuuv.grocerymanager.data.FoodItemRepository;
import com.bsuuv.grocerymanager.data.GroceryListExtractor;
import com.bsuuv.grocerymanager.data.GroceryListState;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;
import java.util.Objects;

public class GroceryItemViewModel extends AndroidViewModel {

  private FoodItemRepository mRepository;
  private LiveData<List<FoodItemEntity>> mFoodItems;
  private GroceryListExtractor mGroceryListExtractor;
  private DateHelper mDateHelper;
  private GroceryListState mGroceries;

  public GroceryItemViewModel(Application application) {
    super(application);
    initMembers(application);
    if (!mDateHelper.isGroceryDay()) {
      updateDatabase();
    }
  }

  private void updateDatabase() {
    for (FoodItemEntity foodItem : mGroceries.getModifiedItems()) {
      mRepository.update(foodItem);
    }
    mGroceries.clearState();
  }

  private void initMembers(Application application) {
    SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(application);
    this.mRepository = new FoodItemRepository(application);
    this.mFoodItems = mRepository.getFoodItems();
    this.mGroceries = new GroceryListState(sharedPreferencesHelper);
    this.mGroceryListExtractor = new GroceryListExtractor(mGroceries, sharedPreferencesHelper);
    this.mDateHelper = new DateHelper(application, sharedPreferencesHelper);
  }

  public LiveData<List<FoodItemEntity>> getGroceryList() {
    return Transformations.map(mFoodItems, mGroceryListExtractor::extractGroceryListFromFoodItems);
  }

  public FoodItemEntity get(int foodItemId) {
    return Objects.requireNonNull(mRepository.getFoodItem(foodItemId));
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    if (mDateHelper.isGroceryDay()) {
      mGroceries.saveState();
    }
  }

  /**
   * Deletes the given food-item from the grocery list, but not from the database.
   *
   * @param foodItem Food-item to delete from the grocery list.
   */
  public void deleteFromGroceryList(FoodItemEntity foodItem) {
    mGroceries.check(foodItem);
  }
}

