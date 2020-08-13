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
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {

  private FoodItemRepository mRepository;
  private LiveData<List<FoodItemEntity>> mFoodItems;
  private GroceryListExtractor mGroceryListExtractor;
  private DateHelper mDateHelper;
  private GroceryListState mState;

  public GroceryItemViewModel(Application application) {
    super(application);
    initMembers(application);
    if (!mDateHelper.isGroceryDay()) {
      updateDatabase();
    }
  }

  private void updateDatabase() {
    for (FoodItemEntity foodItem : mState.getModifiedItems()) {
      mRepository.update(foodItem);
    }
    mState.clearState();
  }

  private void initMembers(Application application) {
    SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(application);
    this.mRepository = new FoodItemRepository(application);
    this.mFoodItems = mRepository.getFoodItems();
    this.mState = new GroceryListState(sharedPreferencesHelper);
    this.mGroceryListExtractor = initExtractor(sharedPreferencesHelper);
    this.mDateHelper = new DateHelper(application, sharedPreferencesHelper);
  }

  private GroceryListExtractor initExtractor(SharedPreferencesHelper sharedPreferencesHelper) {
    FrequencyQuotientCalculator calculator = new FrequencyQuotientCalculator(
        sharedPreferencesHelper);
    return new GroceryListExtractor(mState, calculator);
  }

  public LiveData<List<FoodItemEntity>> getGroceryList() {
    return Transformations.map(mFoodItems, mGroceryListExtractor::extractGroceryListFromFoodItems);
  }

  public FoodItemEntity get(int foodItemId) {
    return mRepository.getFoodItem(foodItemId);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    if (mDateHelper.isGroceryDay()) {
      mState.saveState();
    }
  }

  public void check(FoodItemEntity foodItem) {
    mState.check(foodItem);
  }
}

