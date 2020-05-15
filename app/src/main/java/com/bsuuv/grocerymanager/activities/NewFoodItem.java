package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.domain.FoodItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFoodItem extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    private ImageView foodImageView;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle("Add Food-item");

        this.labelEditText = findViewById(R.id.editText_label);
        this.brandEditText = findViewById(R.id.editText_brand);
        this.amountEditText = findViewById(R.id.editText_amount);
        this.infoEditText = findViewById(R.id.editText_info);
        this.foodImageView = findViewById(R.id.imageView_new_fooditem);

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

    public void onCameraIconClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.getMessage();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.bsuuv.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bitmap foodImage = BitmapFactory.decodeFile(currentPhotoPath);
                    foodImageView.setImageBitmap(foodImage);
                }
            }
        }
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

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storegeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storegeDir);

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }
}
