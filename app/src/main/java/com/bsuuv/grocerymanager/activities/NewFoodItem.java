package com.bsuuv.grocerymanager.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bsuuv.grocerymanager.R;

public class NewFoodItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle("Add Food-item");


    }

    public void onFabClick(View view) {
    }
}
