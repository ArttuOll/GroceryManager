package com.bsuuv.grocerymanager.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bsuuv.grocerymanager.R;

public class FoodItemDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);
        if (savedInstanceState == null) {
            int foodItemId = getIntent().getIntExtra(FoodItemDetailFragment.FOOD_ITEM_ID_KEY, 0);
            launchFoodItemDetailFragment(foodItemId);
        }
    }

    private void launchFoodItemDetailFragment(int foodItemId) {
        FoodItemDetailFragment fragment =
                FoodItemDetailFragment.newInstance(foodItemId);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_food_item_detail, fragment)
                .commit();
    }
}
