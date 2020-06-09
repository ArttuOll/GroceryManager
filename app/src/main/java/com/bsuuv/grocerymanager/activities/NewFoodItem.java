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
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFoodItem extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ToggleButton mWeekToggle;
    private ToggleButton mTwoWeeksToggle;
    private ToggleButton mMonthlyToggle;
    private View.OnClickListener mOnToggleButtonClickListener = view -> {
        handleWeekToggleClicks(view);

        handleTwoWeeksToggleClicks(view);

        handleMonthlyToggleClicks(view);
    };

    private EditText mLabelEditText;
    private EditText mBrandEditText;
    private EditText mAmountEditText;
    private EditText mInfoEditText;
    private EditText mFrequencyEditText;
    private ImageView mFoodImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);

        setTitle(getString(R.string.newFoodItem_title));

        this.mLabelEditText = findViewById(R.id.editText_label);
        this.mBrandEditText = findViewById(R.id.editText_brand);
        this.mAmountEditText = findViewById(R.id.editText_amount);
        this.mInfoEditText = findViewById(R.id.editText_info);
        this.mFrequencyEditText = findViewById(R.id.editText_freq);
        this.mFoodImageView = findViewById(R.id.imageView_new_fooditem);

        setUpToggleButtons();

        manageIntent();
    }

    public void onFabClick(View view) {
        String label = mLabelEditText.getText().toString();
        String brand = mBrandEditText.getText().toString();
        String amount = mAmountEditText.getText().toString();
        String info = mInfoEditText.getText().toString();
        int timeFrame = getActiveToggleButton();
        int frequency = Integer.parseInt(mFrequencyEditText.getText().toString());

        Intent toConfigs = new Intent(this, Configurations.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("time_frame", timeFrame);
        toConfigs.putExtra("frequency", frequency);

        toConfigs.putExtra("uri", mCurrentPhotoPath);

        setResult(RESULT_OK, toConfigs);
        finish();
    }

    public void onCameraIconClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there exists a program that can handle the intent.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.getMessage();
            }

            if (photoFile != null) {
                // URI for a file in which the image is saved.
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
                if (data != null) populateFoodImageView(mCurrentPhotoPath);
            }
        }
    }

    private void handleWeekToggleClicks(View v) {
        if (v.getId() == R.id.togglebutton_week && mWeekToggle.isChecked()) {
            mTwoWeeksToggle.setEnabled(false);
            mMonthlyToggle.setEnabled(false);
        } else if (v.getId() == R.id.togglebutton_week && !mWeekToggle.isChecked()) {
            mTwoWeeksToggle.setEnabled(true);
            mMonthlyToggle.setEnabled(true);
        }
    }

    private void handleTwoWeeksToggleClicks(View v) {
        if (v.getId() == R.id.togglebutton_two_weeks && mTwoWeeksToggle.isChecked()) {
            mWeekToggle.setEnabled(false);
            mMonthlyToggle.setEnabled(false);
        } else if (v.getId() == R.id.togglebutton_two_weeks && !mTwoWeeksToggle.isChecked()) {
            mWeekToggle.setEnabled(true);
            mMonthlyToggle.setEnabled(true);
        }
    }

    private void handleMonthlyToggleClicks(View v) {
        if (v.getId() == R.id.togglebutton_month && mMonthlyToggle.isChecked()) {
            mTwoWeeksToggle.setEnabled(false);
            mWeekToggle.setEnabled(false);
        } else if (v.getId() == R.id.togglebutton_month && !mMonthlyToggle.isChecked()) {
            mTwoWeeksToggle.setEnabled(true);
            mWeekToggle.setEnabled(true);
        }
    }

    private void manageIntent() {
        Intent fromConfigs = getIntent();
        if (fromConfigs != null) {
            this.mLabelEditText.setText(fromConfigs.getStringExtra("label"));
            this.mBrandEditText.setText(fromConfigs.getStringExtra("brand"));
            this.mAmountEditText.setText(fromConfigs.getStringExtra("amount"));
            this.mInfoEditText.setText(fromConfigs.getStringExtra("info"));
            this.mFrequencyEditText.setText(fromConfigs.getStringExtra("frequency"));

            mCurrentPhotoPath = fromConfigs.getStringExtra("uri");
            if (mCurrentPhotoPath != null) populateFoodImageView(mCurrentPhotoPath);

            // Based on the frequency of the food item being edited, set the toggle buttons to
            // either
            // checked or disabled.
            switch (fromConfigs.getIntExtra("time_frame", 0)) {
                case 1:
                    setWeekToggleCheckedDisableOthers();
                    break;
                case 2:
                    setTwoWeeksToggleCheckedDisableOthers();
                    break;
                case 4:
                    setMonthToggleCheckedDisableOthers();
                    break;
            }
        }
    }

    private void setWeekToggleCheckedDisableOthers() {
        this.mWeekToggle.setChecked(true);
        this.mTwoWeeksToggle.setEnabled(false);
        this.mMonthlyToggle.setEnabled(false);
    }

    private void setTwoWeeksToggleCheckedDisableOthers() {
        this.mTwoWeeksToggle.setChecked(true);
        this.mWeekToggle.setEnabled(false);
        this.mMonthlyToggle.setEnabled(false);
    }

    private void setMonthToggleCheckedDisableOthers() {
        this.mMonthlyToggle.setChecked(true);
        this.mTwoWeeksToggle.setEnabled(false);
        this.mWeekToggle.setEnabled(false);
    }

    private void populateFoodImageView(String path) {
        Glide.with(this).load(new File(path)).into(mFoodImageView);
    }

    private int getActiveToggleButton() {
        if (mWeekToggle.isChecked()) {
            return 1;
        } else if (mTwoWeeksToggle.isChecked()) {
            return 2;
        } else if (mMonthlyToggle.isChecked()) {
            return 4;
        } else {
            return -1;
        }
    }

    private void setUpToggleButtons() {
        this.mWeekToggle = findViewById(R.id.togglebutton_week);
        this.mTwoWeeksToggle = findViewById(R.id.togglebutton_two_weeks);
        this.mMonthlyToggle = findViewById(R.id.togglebutton_month);

        String twoWeeksButton = getResources().getString(R.string.button_twoweeks);
        mTwoWeeksToggle.setText(R.string.button_twoweeks);
        mTwoWeeksToggle.setTextOff(twoWeeksButton);
        mTwoWeeksToggle.setTextOn(twoWeeksButton);
        mTwoWeeksToggle.setOnClickListener(mOnToggleButtonClickListener);

        String weekButton = getResources().getString(R.string.button_week);
        mWeekToggle.setText(R.string.button_week);
        mWeekToggle.setTextOff(weekButton);
        mWeekToggle.setTextOn(weekButton);
        mWeekToggle.setOnClickListener(mOnToggleButtonClickListener);

        String monthButton = getResources().getString(R.string.button_month);
        mMonthlyToggle.setText(R.string.button_month);
        mMonthlyToggle.setTextOff(monthButton);
        mMonthlyToggle.setTextOn(monthButton);
        mMonthlyToggle.setOnClickListener(mOnToggleButtonClickListener);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";

        // The directory in which the image is saved, provided by the FileProvider defined in the
        // project.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = Uri.parse(image.toURI().getPath()).getPath();

        return image;
    }
}
