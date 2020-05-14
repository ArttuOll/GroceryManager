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
import java.util.List;

public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_DETAILS_REQUEST = 1;
    private List<FoodItem> mFoodItems;
    private ConfigslistAdapter mAdapter;
    private RecyclerView mRecyclerView;

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

                    mFoodItems.add(new FoodItem(label, brand, info, amount, frequency, 0));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mFoodItems.isEmpty()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            sortFoodItemsByFrequency();
            // TODO: toteuta mFoodItemsin järjestely siten, että tiedä aina, mihin kohtaan uusi
            //  fooditem tuli ja voit näin käyttää asianmukaista notify-metodia.
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.config_recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigslistAdapter(this, mFoodItems);
        this.mRecyclerView.setAdapter(mAdapter);
    }

    private void sortFoodItemsByFrequency() {
        List<FoodItem> biweeklys = new ArrayList<>();
        List<FoodItem> weeklys = new ArrayList<>();
        List<FoodItem> monthlys = new ArrayList<>();

        mFoodItems.forEach(foodItem -> {
            switch (foodItem.getFrequency()) {
                case 0:
                    biweeklys.add(foodItem);
                    break;
                case 1:
                    weeklys.add(foodItem);
                    break;
                case 2:
                    monthlys.add(foodItem);
                    break;
            }
        });

        List<FoodItem> sorted = new ArrayList<>();

        sorted.addAll(biweeklys);
        sorted.addAll(weeklys);
        sorted.addAll(monthlys);

        this.mFoodItems = sorted;
    }

}
