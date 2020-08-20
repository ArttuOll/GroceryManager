package com.bsuuv.grocerymanager.data.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.bsuuv.grocerymanager.data.FoodItemRepository;
import com.bsuuv.grocerymanager.data.GroceryListExtractor;
import com.bsuuv.grocerymanager.data.GroceryListState;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.data.model.FoodItem;
import com.bsuuv.grocerymanager.ui.MainActivity;
import com.bsuuv.grocerymanager.util.DateTimeHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;
import java.util.Objects;

/**
 * A <code>ViewModel</code> that contains all the data and business logic calls required by {@link
 * MainActivity}.
 *
 * @see androidx.lifecycle.ViewModel
 * @see MainActivity
 */
public class GroceryItemViewModel extends AndroidViewModel {

  private FoodItemRepository mRepository;
  private LiveData<List<FoodItemEntity>> mFoodItems;
  private GroceryListExtractor mGroceryListExtractor;
  private DateTimeHelper mDateTimeHelper;
  private GroceryListState mGroceries;

  public GroceryItemViewModel(Application application) {
    super(application);
    initMembers(application);
    if (!mDateTimeHelper.isGroceryDay()) {
      updateDatabase();
    }
  }

  private void updateDatabase() {
    for (FoodItem foodItem : mGroceries.getIncrementedItems()) {
      mRepository.update((FoodItemEntity) foodItem);
    }
    mGroceries.clearState();
  }

  private void initMembers(Application application) {
    SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(application);
    this.mRepository = new FoodItemRepository(application);
    this.mFoodItems = mRepository.getFoodItems();
    this.mGroceries = new GroceryListState(sharedPreferencesHelper);
    this.mGroceryListExtractor = new GroceryListExtractor(mGroceries, sharedPreferencesHelper);
    this.mDateTimeHelper = new DateTimeHelper(application, sharedPreferencesHelper);
  }

  /**
   * Returns an always-up-to-date list of all food-items that are qualified to be on the grocery
   * list.
   *
   * @return List of all food-items eligible to be on the grocery list, wrapped in an observable
   * <code>LiveData</code> object.
   * @see LiveData
   */
  public LiveData<List<FoodItemEntity>> getGroceryList() {
    return Transformations.map(mFoodItems, mGroceryListExtractor::extractGroceryListFromFoodItems);
  }

  public FoodItemEntity get(int foodItemId) {
    return Objects.requireNonNull(mRepository.getFoodItem(foodItemId));
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    if (mDateTimeHelper.isGroceryDay()) {
      mGroceries.saveState();
    }
  }

  /**
   * Deletes the given food-item from the grocery list, but not from the database.
   *
   * @param foodItem Food-item to delete from the grocery list.
   */
  public void deleteFromGroceryList(FoodItemEntity foodItem) {
    mGroceries.remove(foodItem);
  }
}

