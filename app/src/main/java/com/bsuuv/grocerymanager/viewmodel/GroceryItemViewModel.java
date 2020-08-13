package com.bsuuv.grocerymanager.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.GroceryListExtractor;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;

public class GroceryItemViewModel extends AndroidViewModel {

  private FoodItemRepository mRepository;
  private LiveData<List<FoodItemEntity>> mFoodItems;
  private List<FoodItemEntity> mModifiedList;
  private GroceryListExtractor mGroceryListExtractor;
  private DateHelper mInspector;
  private List<FoodItemEntity> mCheckedItems;

  public GroceryItemViewModel(Application application) {
    super(application);
    this.mRepository = new FoodItemRepository(application);
    this.mFoodItems = mRepository.getFoodItems();

    // Dependencies for GroceryListExtractor
    Context context = application.getApplicationContext();

    SharedPreferencesHelper sharedPreferencesHelper =
        new SharedPreferencesHelper(application);

    DateHelper dateHelper = new DateHelper(context,
        sharedPreferencesHelper);

    FrequencyQuotientCalculator calculator =
        new FrequencyQuotientCalculator(sharedPreferencesHelper);

    this.mGroceryListExtractor =
        new GroceryListExtractor(sharedPreferencesHelper, dateHelper,
            calculator
        );

    this.mInspector = new DateHelper(application, sharedPreferencesHelper);

    this.mCheckedItems = mGroceryListExtractor.getCheckedItems();
    this.mModifiedList = mGroceryListExtractor.getModifiedItems();

    if (!mInspector.isGroceryDay()) {
      updateDatabase();
    }
  }

  public LiveData<List<FoodItemEntity>> getGroceryList() {
    return Transformations.map(mFoodItems,
        mGroceryListExtractor::extractGroceryListFromFoodItems);
  }

  public void check(FoodItemEntity foodItem) {
    this.mCheckedItems.add(foodItem);
  }

  public FoodItemEntity get(int foodItemId) {
    return mRepository.getFoodItem(foodItemId);
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    if (mInspector.isGroceryDay()) {
      mGroceryListExtractor.saveState(mModifiedList, mCheckedItems);
    }
  }

  private void updateDatabase() {
    for (FoodItemEntity foodItem : mModifiedList) {
      mRepository.update(foodItem);
    }
    mGroceryListExtractor.clearState();
  }
}

