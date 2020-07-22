package com.bsuuv.grocerymanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.R;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * An <code>Activity</code> for adding new food-items into the grocery list configurations and
 * editing existing ones.
 */
public class NewFoodItem extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String GROCERY_DAYS_KEY = "grocerydays";

    private MaterialButtonToggleGroup mToggleGroup;
    private EditText mLabelEditText;
    private EditText mBrandEditText;
    private EditText mAmountEditText;
    private EditText mInfoEditText;
    private EditText mFrequencyEditText;
    private ImageView mFoodImageView;
    private AutoCompleteTextView mUnitDropdown;
    private String mPhotoPath;
    private SharedPreferences mSharedPrefs;
    private int mId;
    private double mCountdownValue;

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

        this.mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.mUnitDropdown = findViewById(R.id.new_fooditem_unit_dropdown);
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this,
                R.layout.new_food_item_unit_dropdown_item,
                getResources().getStringArray(R.array.units_plural));
        mUnitDropdown.setAdapter(dropdownAdapter);

        setUpToggleButtons();

        manageIntent();

        if (savedInstanceState != null) {
            mPhotoPath = savedInstanceState.getString("photoPath");
            Glide.with(this).load(new File(mPhotoPath)).into(mFoodImageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Glide.with(this).load(new File(mPhotoPath)).into(mFoodImageView);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("photoPath", mPhotoPath);

        super.onSaveInstanceState(outState);
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
        String unit = mUnitDropdown.getText().toString();
        String info = mInfoEditText.getText().toString();
        int timeFrame = getActiveToggleButton();
        int frequency = Integer.parseInt(mFrequencyEditText.getText().toString());
        int groceryDaysAWeek = mSharedPrefs.getStringSet(GROCERY_DAYS_KEY, new HashSet<>()).size();

        double frequencyQuotient = (double) frequency /
                ((double) timeFrame * (double) groceryDaysAWeek);

        if (constraintsFulfilled(groceryDaysAWeek, label, frequencyQuotient)) {
            Intent toConfigs = createIntentToConfigs(label, brand, amount, unit, info, timeFrame,
                    frequency, mPhotoPath);

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
    public void onFoodImageClick(View view) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.togglebutton_week:
                mToggleGroup.check(R.id.togglebutton_week);
                break;
            case R.id.togglebutton_two_weeks:
                mToggleGroup.check(R.id.togglebutton_two_weeks);
                break;
            case R.id.togglebutton_month:
                mToggleGroup.check(R.id.togglebutton_month);
                break;
        }
    }

    private Intent createIntentToConfigs(String label, String brand, int amount, String unit,
                                         String info, int timeFrame, int frequency,
                                         String mPhotoPath) {
        Intent toConfigs = new Intent(this, Configurations.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("unit", unit);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("time_frame", timeFrame);
        toConfigs.putExtra("frequency", frequency);
        toConfigs.putExtra("uri", mPhotoPath);
        toConfigs.putExtra("id", mId);
        toConfigs.putExtra("countdownValue", mCountdownValue);

        return toConfigs;
    }

    private boolean constraintsFulfilled(int groceryDaysAWeek, String label,
                                         double frequencyQuotient) {
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
            this.mUnitDropdown.setText(fromConfigs.getStringExtra("unit"));
            this.mInfoEditText.setText(fromConfigs.getStringExtra("info"));
            this.mFrequencyEditText.setText(String.valueOf(
                    fromConfigs.getIntExtra("frequency", 0)));
            this.mId = fromConfigs.getIntExtra("id", 0);
            this.mCountdownValue = fromConfigs.getDoubleExtra("countdownValue", 0.0);

            this.mPhotoPath = fromConfigs.getStringExtra("uri");
            if (mPhotoPath != null) {
                Glide.with(this).load(new File(mPhotoPath)).into(mFoodImageView);
            }

            // Based on the time frame of the food item being edited, set the toggle buttons to
            // either checked or disabled.
            switch (fromConfigs.getIntExtra("time_frame", 0)) {
                case TimeFrame.WEEK:
                    mToggleGroup.check(R.id.togglebutton_week);
                    break;
                case TimeFrame.TWO_WEEKS:
                    mToggleGroup.check(R.id.togglebutton_two_weeks);
                    break;
                case TimeFrame.MONTH:
                    mToggleGroup.check(R.id.togglebutton_month);
                    break;
            }
        }
    }

    private int getActiveToggleButton() {
        switch (mToggleGroup.getCheckedButtonId()) {
            case R.id.togglebutton_week:
                return TimeFrame.WEEK;
            case R.id.togglebutton_two_weeks:
                return TimeFrame.TWO_WEEKS;
            case R.id.togglebutton_month:
                return TimeFrame.MONTH;
            default:
                return -1;
        }
    }

    private void setUpToggleButtons() {
        this.mToggleGroup = findViewById(R.id.freq_selection_togglegroup);
        Button mWeekToggle = findViewById(R.id.togglebutton_week);
        Button mTwoWeeksToggle = findViewById(R.id.togglebutton_two_weeks);
        Button mMonthToggle = findViewById(R.id.togglebutton_month);

        mWeekToggle.setText(R.string.button_week);
        mWeekToggle.setOnClickListener(this);

        mTwoWeeksToggle.setText(R.string.button_twoweeks);
        mTwoWeeksToggle.setOnClickListener(this);

        mMonthToggle.setText(R.string.button_month);
        mMonthToggle.setOnClickListener(this);
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

    private interface TimeFrame {
        int WEEK = 1;
        int TWO_WEEKS = 2;
        int MONTH = 4;
    }
}
