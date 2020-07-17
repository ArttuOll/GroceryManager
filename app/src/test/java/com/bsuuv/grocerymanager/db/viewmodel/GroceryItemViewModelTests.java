package com.bsuuv.grocerymanager.db.viewmodel;

import android.app.Application;

import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;

import com.bsuuv.grocerymanager.FoodItemRepository;
import com.bsuuv.grocerymanager.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GroceryItemViewModelTests {

   private GroceryItemViewModel mGroceryItemViewModel;

   @Mock
   private Observer<List<FoodItemEntity>> mObserver;

   @Mock
   private SharedPreferencesHelper mSharedPrefsHelper;

   @Mock
   private FoodItemRepository mRepository;

   private Application mApplication;

   @Before
   public void initialize() {
      MockitoAnnotations.initMocks(this);
      this.mApplication = ApplicationProvider.getApplicationContext();
      this.mGroceryItemViewModel = new GroceryItemViewModel(mApplication);
      mGroceryItemViewModel.getGroceryList().observeForever(mObserver);
   }

   @Test
   public void getGroceryList_isGroceryDay_updateListClear() {
   }
}
