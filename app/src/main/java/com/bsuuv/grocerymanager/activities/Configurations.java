package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.adapters.ConfigslistAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_DETAILS_REQUEST = 1;
    private List<FoodItem> mFoodItems;
    private ConfigslistAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);

        this.mFoodItems = new ArrayList<>();

        setUpRecyclerView();
    }

    public void onFabClick(View view) {
        Intent toNewFoodItem = new Intent(this, NewFoodItem.class);
        startActivityForResult(toNewFoodItem, FOOD_ITEM_DETAILS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FOOD_ITEM_DETAILS_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String label = data.getStringExtra("label");
                    String brand = data.getStringExtra("brand");
                    String amount = data.getStringExtra("amount");
                    String info = data.getStringExtra("info");
                    int frequency = data.getIntExtra("frequency", 0);

                    int insertionPosition = getFoodItemInsertionPosition(frequency);

                    mFoodItems.add(new FoodItem(label, brand, info, amount, frequency, 0));
                    Collections.sort(mFoodItems, (foodItem1, foodItem2) -> foodItem1.getFrequency() - foodItem2.getFrequency());

                    mAdapter.notifyItemInserted(insertionPosition);
                }
            }
        }
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

        switch (frequency) {
            case FoodItem.Frequency.BIWEEKLY:
                return biweeklys;
            case FoodItem.Frequency.WEEKLY:
                return biweeklys + weeklys;
            case FoodItem.Frequency.MONTHLY:
                return biweeklys + weeklys + monthlys;
            default:
                return -1;
        }
    }
}
