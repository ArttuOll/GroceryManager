package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.activities.adapters.ConfigurationsListAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bsuuv.grocerymanager.logic.FoodScheduler;
import com.bsuuv.grocerymanager.logic.SharedPreferencesHelper;

import java.util.Collections;
import java.util.List;

public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_CREATE_REQUEST = 1;
    public static final int FOOD_ITEM_EDIT_REQUEST = 2;
    private List<FoodItem> mFoodItems;
    private ConfigurationsListAdapter mAdapter;
    private SharedPreferencesHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
        setTitle("Configurations");

        this.mSharedPrefsHelper = new SharedPreferencesHelper(PreferenceManager.
                getDefaultSharedPreferences(this));
        this.mFoodItems = mSharedPrefsHelper.getFoodItems();

        setUpRecyclerView();
    }

    public void onFabClick(View view) {
        Intent toNewFoodItem = new Intent(this, NewFoodItem.class);
        startActivityForResult(toNewFoodItem, FOOD_ITEM_CREATE_REQUEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mFoodItems.isEmpty()) mSharedPrefsHelper.saveFoodItems(mFoodItems);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result when NewFoodItem was launched to create a new food-item using the FAB.
        if (requestCode == FOOD_ITEM_CREATE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FoodItem result = createFoodItemFromIntent(data);

                    int insertionPosition = getFoodItemInsertionPosition(result.getTimeFrame());

                    mFoodItems.add(result);
                    Collections.sort(mFoodItems, (foodItem1, foodItem2) ->
                            foodItem1.getTimeFrame() - foodItem2.getTimeFrame());

                    mAdapter.notifyItemInserted(insertionPosition);
                }
            }
            // Result when NewFoodItem was launched to edit a food-item by clicking one in the
            // RecyclerView.
        } else if (requestCode == FOOD_ITEM_EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FoodItem result = createFoodItemFromIntent(data);

                    int editedPosition = data.getIntExtra("editPosition", 0);

                    mFoodItems.set(editedPosition, result);

                    mAdapter.notifyItemChanged(editedPosition);
                }
            }
        }


    }

    private FoodItem createFoodItemFromIntent(Intent data) {
        String label = data.getStringExtra("label");
        String brand = data.getStringExtra("brand");
        int amount = data.getIntExtra("amount", 0);
        String info = data.getStringExtra("info");
        int timeFrame = data.getIntExtra("time_frame", 0);
        int frequency = data.getIntExtra("frequency", 0);

        String imageUri = data.getStringExtra("uri");

        return new FoodItem(label, brand, info, amount, timeFrame, frequency, imageUri);
    }

    private void setUpRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigurationsListAdapter(this, mFoodItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int getFoodItemInsertionPosition(int frequency) {
        int weeks = 0;
        int twoweeks = 0;
        int months = 0;

        for (FoodItem foodItem : mFoodItems) {
            switch (foodItem.getTimeFrame()) {
                case FoodScheduler.TimeFrame.WEEK:
                    weeks++;
                    break;
                case FoodScheduler.TimeFrame.TWO_WEEKS:
                    twoweeks++;
                    break;
                case FoodScheduler.TimeFrame.MONTH:
                    months++;
                    break;
            }
        }

        // When a food item is inserted into the RecyclerView, it is added after all the other
        // food-items with the same frequency.
        switch (frequency) {
            case FoodScheduler.TimeFrame.WEEK:
                return weeks;
            case FoodScheduler.TimeFrame.TWO_WEEKS:
                return weeks + twoweeks;
            case FoodScheduler.TimeFrame.MONTH:
                return weeks + twoweeks + months;
            default:
                return 0;
        }
    }
}
