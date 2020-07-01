package com.bsuuv.grocerymanager.activities;

import android.annotation.SuppressLint;
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
import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.logic.FoodScheduler;
import com.bsuuv.grocerymanager.logic.SharedPreferencesHelper;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * An <code>Activity</code> for adding new food-items into the grocery list configurations and
 * editing existing ones.
 */
public class NewFoodItem extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ToggleButton mWeekToggle;
    private ToggleButton mTwoWeeksToggle;
    private ToggleButton mMonthlyToggle;
    private EditText mLabelEditText;
    private EditText mBrandEditText;
    private EditText mAmountEditText;
    private EditText mInfoEditText;
    private EditText mFrequencyEditText;
    private ImageView mFoodImageView;
    private String mPhotoPath;
    private UUID mFoodItemId;
    private SharedPreferencesHelper mSharedPrefsHelper;

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
        this.mSharedPrefsHelper = new SharedPreferencesHelper(PreferenceManager
                .getDefaultSharedPreferences(this));

        setUpToggleButtons();

        manageIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Glide.with(this).load(new File(mPhotoPath)).into(mFoodImageView);
        }
    }

    /**
     * Called when the floating action button with a check mark is pressed. Gathers all data from
     * the activity's views and puts them into intent extras to be sent back to
     * <code>Configurations</code>.
     *
     * @param view The view that has been clicked, in this case, the FAB.
     *             Default parameter required by the system.
     */
    public void onFabClick(View view) {
        String label = mLabelEditText.getText().toString();
        String brand = mBrandEditText.getText().toString();
        int amount = Integer.parseInt(mAmountEditText.getText().toString());
        String info = mInfoEditText.getText().toString();
        int timeFrame = getActiveToggleButton();
        int frequency = Integer.parseInt(mFrequencyEditText.getText().toString());
        int groceryDaysAWeek = mSharedPrefsHelper.getGroceryDays().size();

        double frequencyQuotient = (double) frequency /
                ((double) timeFrame * (double) groceryDaysAWeek);

        if (constraintsFulfilled(groceryDaysAWeek, label, frequencyQuotient)) {
            Intent toConfigs = createIntentToConfigs(label, brand, amount, info, timeFrame,
                    frequency, mPhotoPath, mFoodItemId);

            setResult(RESULT_OK, toConfigs);
            finish();
        }
    }

    /**
     * Called when the camera icon is clicked. Launches an implicit intent to a camera app for
     * taking a picture of a food-item.
     *
     * @param view The view that has been clicked, in this case, the FAB.
     *             Default parameter required by the system.
     */
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

    private Intent createIntentToConfigs(String label, String brand, int amount, String info, int timeFrame, int frequency,
                                         String mPhotoPath, UUID mFoodItemId) {
        Intent toConfigs = new Intent(this, Configurations.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("time_frame", timeFrame);
        toConfigs.putExtra("frequency", frequency);
        toConfigs.putExtra("uri", mPhotoPath);
        toConfigs.putExtra("id", mFoodItemId);

        return toConfigs;
    }

    private boolean constraintsFulfilled(int groceryDaysAWeek, String label, double frequencyQuotient) {
        if (groceryDaysAWeek == 0) {
            Snackbar.make(findViewById(R.id.fab_new_fooditem), R.string.snackbar_no_grocery_days,
                    Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab_new_fooditem).show();
            return false;
        } else if (label.isEmpty()) {
            Snackbar.make(findViewById(R.id.fab_new_fooditem), getString(R.string.label_empty),
                    Snackbar.LENGTH_LONG).setAnchorView(R.id.fab_new_fooditem).show();
            return false;
        } else if (frequencyQuotient > 1.0) {
            Snackbar.make(findViewById(R.id.fab_new_fooditem),
                    R.string.snackbar_not_enough_grocery_days,
                    Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab_new_fooditem).show();
            return false;
        } else {
            return true;
        }
    }

    private void manageIntent() {
        Intent fromConfigs = getIntent();
        if (fromConfigs != null) {
            this.mLabelEditText.setText(fromConfigs.getStringExtra("label"));
            this.mBrandEditText.setText(fromConfigs.getStringExtra("brand"));
            this.mAmountEditText.setText(String.valueOf(fromConfigs.getIntExtra("amount", 0)));
            this.mInfoEditText.setText(fromConfigs.getStringExtra("info"));
            this.mFrequencyEditText.setText(String.valueOf(
                    fromConfigs.getIntExtra("frequency", 0)));
            this.mFoodItemId = (UUID) fromConfigs.getSerializableExtra("id");

            this.mPhotoPath = fromConfigs.getStringExtra("uri");
            if (mPhotoPath != null) {
                Glide.with(this).load(new File(mPhotoPath)).into(mFoodImageView);
            }

            // Based on the time frame of the food item being edited, set the toggle buttons to
            // either checked or disabled.
            switch (fromConfigs.getIntExtra("time_frame", 0)) {
                case FoodScheduler.TimeFrame.WEEK:
                    setToggleButtonStates(true, false, false);
                    break;
                case FoodScheduler.TimeFrame.TWO_WEEKS:
                    setToggleButtonStates(false, true, false);
                    break;
                case FoodScheduler.TimeFrame.MONTH:
                    setToggleButtonStates(false, false, true);
                    break;
            }
        }
    }

    private int getActiveToggleButton() {
        if (mWeekToggle.isChecked()) return FoodScheduler.TimeFrame.WEEK;
        else if (mTwoWeeksToggle.isChecked()) return FoodScheduler.TimeFrame.TWO_WEEKS;
        else if (mMonthlyToggle.isChecked()) return FoodScheduler.TimeFrame.MONTH;
        else return -1;
    }

    private void setUpToggleButtons() {
        this.mWeekToggle = findViewById(R.id.togglebutton_week);
        this.mTwoWeeksToggle = findViewById(R.id.togglebutton_two_weeks);
        this.mMonthlyToggle = findViewById(R.id.togglebutton_month);

        String weekButton = getResources().getString(R.string.button_week);
        mWeekToggle.setText(R.string.button_week);
        mWeekToggle.setTextOff(weekButton);
        mWeekToggle.setTextOn(weekButton);
        mWeekToggle.setOnClickListener(view -> {
            if (!mWeekToggle.isChecked()) {
                setToggleButtonStates(true, true, true);
            } else {
                setToggleButtonStates(true, false, false);
            }
        });

        String twoWeeksButton = getResources().getString(R.string.button_twoweeks);
        mTwoWeeksToggle.setText(R.string.button_twoweeks);
        mTwoWeeksToggle.setTextOff(twoWeeksButton);
        mTwoWeeksToggle.setTextOn(twoWeeksButton);
        mTwoWeeksToggle.setOnClickListener(view -> {
            if (!mTwoWeeksToggle.isChecked()) {
                setToggleButtonStates(true, true, true);
            } else {
                setToggleButtonStates(false, true, false);
            }
        });

        String monthButton = getResources().getString(R.string.button_month);
        mMonthlyToggle.setText(R.string.button_month);
        mMonthlyToggle.setTextOff(monthButton);
        mMonthlyToggle.setTextOn(monthButton);
        mMonthlyToggle.setOnClickListener(view -> {
            if (!mMonthlyToggle.isChecked()) {
                setToggleButtonStates(true, true, true);
            } else {
                setToggleButtonStates(false, false, true);
            }
        });
    }

    private void setToggleButtonStates(boolean weekButton, boolean twoWeekButton,
                                       boolean monthButton) {
        mWeekToggle.setEnabled(weekButton);
        mTwoWeeksToggle.setEnabled(twoWeekButton);
        mMonthlyToggle.setEnabled(monthButton);
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";

        // The directory in which the image is saved, provided by the FileProvider defined in the
        // project.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mPhotoPath = Uri.parse(image.toURI().getPath()).getPath();

        return image;
    }
}
