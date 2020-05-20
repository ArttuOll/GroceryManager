package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.adapters.ConfigslistAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_DETAILS_REQUEST = 1;
    public static final int FOOD_ITEM_EDIT_REQUEST = 2;
    private static final String FOOD_ITEMS_KEY = "foodItems";
    private List<FoodItem> mFoodItems;
    private ConfigslistAdapter mAdapter;
    private SharedPreferences mPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
        setTitle("Configurations");

        this.mFoodItems = new ArrayList<>();
        this.gson = new Gson();
        String prefFile = "com.bsuuv.grocerymanager.sharedpreferences";
        this.mPreferences = getSharedPreferences(prefFile, MODE_PRIVATE);
        //SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        //preferencesEditor.clear().apply();

        String jsonFoodItems = mPreferences.getString(FOOD_ITEMS_KEY, "");
        Type listType = new TypeToken<List<FoodItem>>() {
        }.getType();

        List<FoodItem> foodItems = gson.fromJson(jsonFoodItems, listType);
        if (foodItems != null) this.mFoodItems = foodItems;

        setUpRecyclerView();
    }

    public void onFabClick(View view) {
        Intent toNewFoodItem = new Intent(this, NewFoodItem.class);
        startActivityForResult(toNewFoodItem, FOOD_ITEM_DETAILS_REQUEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        String json = gson.toJson(mFoodItems);

        preferencesEditor.putString(FOOD_ITEMS_KEY, json);
        preferencesEditor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FOOD_ITEM_DETAILS_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FoodItem result = createFoodItemFromIntent(data);

                    int insertionPosition = getFoodItemInsertionPosition(result.getFrequency());

                    mFoodItems.add(result);
                    Collections.sort(mFoodItems, (foodItem1, foodItem2) ->
                            foodItem1.getFrequency() - foodItem2.getFrequency());

                    mAdapter.notifyItemInserted(insertionPosition);
                }
            }
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
        String amount = data.getStringExtra("amount");
        String info = data.getStringExtra("info");
        int frequency = data.getIntExtra("frequency", 0);
        String imageUri = data.getStringExtra("uri");

        return new FoodItem(label, brand, info, amount, frequency, imageUri);
    }

    private void setUpRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigslistAdapter(this, mFoodItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int getFoodItemInsertionPosition(int frequency) {
        int biweeklys = 0;
        int weeklys = 0;
        int monthlys = 0;

        for (FoodItem foodItem : mFoodItems) {
            switch (foodItem.getFrequency()) {
                case FoodItem.Frequency.BIWEEKLY:
                    biweeklys++;
                    break;
                case FoodItem.Frequency.WEEKLY:
                    weeklys++;
                    break;
                case FoodItem.Frequency.MONTHLY:
                    monthlys++;
                    break;
            }
        }

        // When a food item is inserted into the RecyclerView, it is added after all the other
        // food-items with the same frequency.
        switch (frequency) {
            case FoodItem.Frequency.BIWEEKLY:
                return biweeklys;
            case FoodItem.Frequency.WEEKLY:
                return biweeklys + weeklys;
            case FoodItem.Frequency.MONTHLY:
                return biweeklys + weeklys + monthlys;
            default:
                return 0;
        }
    }
}
