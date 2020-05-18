package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
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
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFoodItem extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ToggleButton mWeeklyToggle;
    private ToggleButton mBiweeklyToggle;
    private ToggleButton mMonthlyToggle;
    private View.OnClickListener mOnToggleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.togglebuton_biweekly && mBiweeklyToggle.isChecked()) {
                mWeeklyToggle.setEnabled(false);
                mMonthlyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_biweekly && !mBiweeklyToggle.isChecked()) {
                mWeeklyToggle.setEnabled(true);
                mMonthlyToggle.setEnabled(true);
            }

            if (v.getId() == R.id.togglebuton_weekly && mWeeklyToggle.isChecked()) {
                mBiweeklyToggle.setEnabled(false);
                mMonthlyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_weekly && !mWeeklyToggle.isChecked()) {
                mBiweeklyToggle.setEnabled(true);
                mMonthlyToggle.setEnabled(true);
            }

            if (v.getId() == R.id.togglebuton_monthly && mMonthlyToggle.isChecked()) {
                mBiweeklyToggle.setEnabled(false);
                mWeeklyToggle.setEnabled(false);
            } else if (v.getId() == R.id.togglebuton_monthly && !mMonthlyToggle.isChecked()) {
                mBiweeklyToggle.setEnabled(true);
                mWeeklyToggle.setEnabled(true);
            }
        }
    };

    private EditText mLabelEditText;
    private EditText mBrandEditText;
    private EditText mAmountEditText;
    private EditText mInfoEditText;
    private ImageView mFoodImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle("Add Food-item");

        this.mLabelEditText = findViewById(R.id.editText_label);
        this.mBrandEditText = findViewById(R.id.editText_brand);
        this.mAmountEditText = findViewById(R.id.editText_amount);
        this.mInfoEditText = findViewById(R.id.editText_info);
        this.mFoodImageView = findViewById(R.id.imageView_new_fooditem);

        setUpToggleButtons();
    }

    public void onFabClick(View view) {
        String label = mLabelEditText.getText().toString();
        String brand = mBrandEditText.getText().toString();
        String amount = mAmountEditText.getText().toString();
        String info = mInfoEditText.getText().toString();
        int frequency = getActiveToggleButton();

        Intent toConfigs = new Intent(this, Configurations.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("frequency", frequency);
        toConfigs.putExtra("uri", mCurrentPhotoPath);

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
                    Glide.with(this)
                            .load(new File(String.valueOf(mCurrentPhotoPath)))
                            .into(mFoodImageView);
                }
            }
        }
    }

    private int getActiveToggleButton() {
        if (mBiweeklyToggle.isChecked()) return FoodItem.Frequency.BIWEEKLY;
        else if (mWeeklyToggle.isChecked()) return FoodItem.Frequency.WEEKLY;
        else if (mMonthlyToggle.isChecked()) return FoodItem.Frequency.MONTHLY;
        else return -1;
    }

    private void setUpToggleButtons() {
        this.mWeeklyToggle = findViewById(R.id.togglebuton_weekly);
        this.mBiweeklyToggle = findViewById(R.id.togglebuton_biweekly);
        this.mMonthlyToggle = findViewById(R.id.togglebuton_monthly);

        mBiweeklyToggle.setText(R.string.togglebutton_biweekly);
        mBiweeklyToggle.setTextOff("Biweekly");
        mBiweeklyToggle.setTextOn("Biweekly");
        mBiweeklyToggle.setOnClickListener(mOnToggleButtonClickListener);

        mWeeklyToggle.setText(R.string.togglebutton_weekly);
        mWeeklyToggle.setTextOff("Weekly");
        mWeeklyToggle.setTextOn("Weekly");
        mWeeklyToggle.setOnClickListener(mOnToggleButtonClickListener);

        mMonthlyToggle.setText(R.string.togglebutton_monthly);
        mMonthlyToggle.setTextOff("Monthly");
        mMonthlyToggle.setTextOn("Monthly");
        mMonthlyToggle.setOnClickListener(mOnToggleButtonClickListener);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storegeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storegeDir);

        mCurrentPhotoPath = Uri.parse(image.toURI().getPath()).getPath();

        return image;
    }
}
