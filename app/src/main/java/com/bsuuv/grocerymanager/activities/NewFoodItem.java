package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.domain.FoodItem;

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

    private EditText labelEditText;
    private EditText brandEditText;
    private EditText amountEditText;
    private EditText infoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle("Add Food-item");

        this.labelEditText = findViewById(R.id.editText_label);
        this.brandEditText = findViewById(R.id.editText_brand);
        this.amountEditText = findViewById(R.id.editText_amount);
        this.infoEditText = findViewById(R.id.editText_info);

        setUpToggleButtons();
    }

    public void onFabClick(View view) {
        String label = labelEditText.getText().toString();
        String brand = brandEditText.getText().toString();
        String amount = amountEditText.getText().toString();
        String info = infoEditText.getText().toString();
        int frequency = getActiveToggleButton();

        Intent toConfigs = new Intent(this, Configurations.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("frequency", frequency);

        setResult(RESULT_OK, toConfigs);
        finish();
    }

    private int getActiveToggleButton() {
        if (biweeklyToggle.isChecked()) return FoodItem.Frequency.BIWEEKLY;
        else if (weeklyToggle.isChecked()) return FoodItem.Frequency.WEEKLY;
        else if (monthlyToggle.isChecked()) return FoodItem.Frequency.MONTHLY;
        else return -1;
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
}
