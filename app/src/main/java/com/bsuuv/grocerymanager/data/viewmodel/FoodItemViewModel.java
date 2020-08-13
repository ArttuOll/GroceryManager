package com.bsuuv.grocerymanager.data.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.bsuuv.grocerymanager.data.FoodItemRepository;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;

public class FoodItemViewModel extends AndroidViewModel {

  private FoodItemRepository mRepository;
  private LiveData<List<FoodItemEntity>> mFoodItems;

  public FoodItemViewModel(Application application) {
    super(application);
    this.mRepository = new FoodItemRepository(application);
    this.mFoodItems = mRepository.getFoodItems();
  }

  public void insert(FoodItemEntity foodItem) {
    mRepository.insert(foodItem);
  }

  public void delete(FoodItemEntity foodItem) {
    mRepository.delete(foodItem);
  }

  public void deleteAll() {
    mRepository.deleteAll();
  }

  public void update(FoodItemEntity foodItem) {
    mRepository.update(foodItem);
  }

  public LiveData<List<FoodItemEntity>> getFoodItems() {
    return mFoodItems;
  }
}