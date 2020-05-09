package com.bsuuv.grocerymanager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bsuuv.grocerymanager.R;

public class NewFoodItem extends AppCompatActivity {

    private ToggleButton weeklyToggle;
    private ToggleButton biweeklyToggle;
    private ToggleButton monthlyToggle;
    private View.OnClickListener mOnToggleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.togglebuton_biweekly && biweeklyToggle.isChecked()) {
                weeklyToggle.setEnabled(false);
                monthlyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_biweekly && !biweeklyToggle.isChecked()) {
                weeklyToggle.setEnabled(true);
                monthlyToggle.setEnabled(true);
            }

            if (v.getId() == R.id.togglebuton_weekly && weeklyToggle.isChecked()) {
                biweeklyToggle.setEnabled(false);
                monthlyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_weekly && !weeklyToggle.isChecked()) {
                biweeklyToggle.setEnabled(true);
                monthlyToggle.setEnabled(true);
            }

            if (v.getId() == R.id.togglebuton_monthly && monthlyToggle.isChecked()) {
                biweeklyToggle.setEnabled(false);
                weeklyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_monthly && !monthlyToggle.isChecked()) {
                biweeklyToggle.setEnabled(true);
                weeklyToggle.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle("Add Food-item");

        setUpToggleButtons();
    }

    private void setUpToggleButtons() {
        this.weeklyToggle = findViewById(R.id.togglebuton_weekly);
        this.biweeklyToggle = findViewById(R.id.togglebuton_biweekly);
        this.monthlyToggle = findViewById(R.id.togglebuton_monthly);

        biweeklyToggle.setText(R.string.togglebutton_biweekly);
        biweeklyToggle.setTextOff("Biweekly");
        biweeklyToggle.setTextOn("Biweekly");
        biweeklyToggle.setOnClickListener(mOnToggleButtonClickListener);

        weeklyToggle.setText(R.string.togglebutton_weekly);
        weeklyToggle.setTextOff("Weekly");
        weeklyToggle.setTextOn("Weekly");
        weeklyToggle.setOnClickListener(mOnToggleButtonClickListener);

        monthlyToggle.setText(R.string.togglebutton_monthly);
        monthlyToggle.setTextOff("Monthly");
        monthlyToggle.setTextOn("Monthly");
        monthlyToggle.setOnClickListener(mOnToggleButtonClickListener);
    }

    public void onFabClick(View view) {
    }
}
