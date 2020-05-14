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

                    mFoodItems.add(new FoodItem(label, brand, info, amount, frequency, 0));
                    Collections.sort(mFoodItems, (foodItem1, foodItem2) -> foodItem1.getFrequency() - foodItem2.getFrequency());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mFoodItems.isEmpty()) {
            // TODO: toteuta mFoodItemsin järjestely siten, että tiedä aina, mihin kohtaan uusi
            //  fooditem tuli ja voit näin käyttää asianmukaista notify-metodia.
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setUpRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigslistAdapter(this, mFoodItems);
        mRecyclerView.setAdapter(mAdapter);
    }
}
