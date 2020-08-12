package com.bsuuv.grocerymanager.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.util.FoodItemCreationRequirementChecker;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.RequestValidator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.util.TimeFrame;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class NewFoodItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String IMAGE_PATH_KEY = "imagePath";
    private static final int FREQUENCY_NOT_SET = 0;
    private static final int AMOUNT_FIELD_EMPTY = 0;

    private MaterialButtonToggleGroup mToggleGroup;
    private EditText mLabelEditText, mBrandEditText, mAmountEditText, mInfoEditText,
            mFrequencyEditText;
    private ImageView mFoodImageView;
    private AutoCompleteTextView mUnitDropdown;
    private String mImagePath;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private FrequencyQuotientCalculator mFqCalc;
    private int mId;
    private double mCountdownValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food_item);
        setTitle(getString(R.string.newFoodItem_title));
        initMembers();
        setUpToggleButtons();
        manageIntent();
        if (savedInstanceState != null) recoverFoodImage(savedInstanceState);
    }

    private void initMembers() {
        this.mLabelEditText = findViewById(R.id.editText_label);
        this.mBrandEditText = findViewById(R.id.editText_brand);
        this.mAmountEditText = findViewById(R.id.editText_amount);
        this.mInfoEditText = findViewById(R.id.editText_info);
        this.mFrequencyEditText = initFrequencyEditText();
        this.mFoodImageView = findViewById(R.id.imageView_new_fooditem);
        this.mSharedPrefsHelper = new SharedPreferencesHelper(this);
        this.mFqCalc = new FrequencyQuotientCalculator(mSharedPrefsHelper);
        this.mUnitDropdown = initUnitDropdown();
        this.mToggleGroup = findViewById(R.id.freq_selection_togglegroup);
    }

    private EditText initFrequencyEditText() {
        EditText editText = findViewById(R.id.editText_freq);
        editText.setText("0");
        return editText;
    }

    private AutoCompleteTextView initUnitDropdown() {
        AutoCompleteTextView unitDropdown = findViewById(R.id.new_fooditem_unit_dropdown);
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this,
                R.layout.new_food_item_unit_dropdown_item, getResources()
                .getStringArray(R.array.units_plural));
        unitDropdown.setAdapter(dropdownAdapter);
        return unitDropdown;
    }

    private void setUpToggleButtons() {
        setUpWeekToggle();
        setUpTwoWeeksToggle();
        setUpMonthToggle();
    }

    private void setUpWeekToggle() {
        Button mWeekToggle = findViewById(R.id.togglebutton_week);
        mWeekToggle.setText(R.string.button_week);
        mWeekToggle.setOnClickListener(this);
    }

    private void setUpTwoWeeksToggle() {
        Button mTwoWeeksToggle = findViewById(R.id.togglebutton_two_weeks);
        mTwoWeeksToggle.setText(R.string.button_twoweeks);
        mTwoWeeksToggle.setOnClickListener(this);
    }

    private void setUpMonthToggle() {
        Button mMonthToggle = findViewById(R.id.togglebutton_month);
        mMonthToggle.setText(R.string.button_month);
        mMonthToggle.setOnClickListener(this);
    }

    private void manageIntent() {
        Intent fromConfigs = Objects.requireNonNull(getIntent());
        if (intentsToEditFoodItem(fromConfigs)) {
            setInputFieldValuesFromIntent(fromConfigs);
            setImageFromIntent(fromConfigs);
            setToggleButtonStatesFromIntent(fromConfigs);
            this.mId = fromConfigs.getIntExtra("id", 0);
            this.mCountdownValue = fromConfigs.getDoubleExtra("countdownValue", 0.0);
        }
    }

    private boolean intentsToEditFoodItem(Intent intent) {
        return intent.getIntExtra("requestCode", RequestValidator.NONE) ==
                RequestValidator.FOOD_ITEM_EDIT_REQUEST;
    }

    private void setInputFieldValuesFromIntent(Intent intent) {
        this.mLabelEditText.setText(intent.getStringExtra("label"));
        this.mBrandEditText.setText(intent.getStringExtra("brand"));
        this.mAmountEditText.setText(String.valueOf(intent.getIntExtra("amount", 0)));
        this.mUnitDropdown.setText(intent.getStringExtra("unit"));
        this.mInfoEditText.setText(intent.getStringExtra("info"));
        this.mFrequencyEditText.setText(String.valueOf(intent.getIntExtra("frequency",
                FREQUENCY_NOT_SET)));
    }

    private void setImageFromIntent(Intent intent) {
        this.mImagePath = intent.getStringExtra("uri");
        if (mImagePath != null) Glide.with(this).load(new File(mImagePath)).into(mFoodImageView);
    }

    private void setToggleButtonStatesFromIntent(Intent fromConfigs) {
        TimeFrame timeFrame =
                (TimeFrame) Objects.requireNonNull(fromConfigs.getSerializableExtra("time_frame"));
        switch (timeFrame) {
            case WEEK:
                mToggleGroup.check(R.id.togglebutton_week);
                break;
            case TWO_WEEKS:
                mToggleGroup.check(R.id.togglebutton_two_weeks);
                break;
            case MONTH:
                mToggleGroup.check(R.id.togglebutton_month);
                break;
        }
    }

    private void recoverFoodImage(Bundle savedInstanceState) {
        mImagePath = savedInstanceState.getString(IMAGE_PATH_KEY);
        Glide.with(this).load(new File(mImagePath)).into(mFoodImageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestValidator.imageCaptureSuccesful(requestCode, resultCode, data)) {
            populateImageView();
        }
    }

    private void populateImageView() {
        Glide.with(this).load(new File(mImagePath)).into(mFoodImageView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(IMAGE_PATH_KEY, mImagePath);
        super.onSaveInstanceState(outState);
    }

    public void onFabClick(View view) {
        String label = mLabelEditText.getText().toString();
        String brand = mBrandEditText.getText().toString();
        int amount = getAmount();
        String unit = mUnitDropdown.getText().toString();
        String info = mInfoEditText.getText().toString();
        TimeFrame timeFrame = getActiveToggleButton();
        int frequency = getFrequency();
        int groceryDaysAWeek = mSharedPrefsHelper.getGroceryDays().size();
        double frequencyQuotient = mFqCalc.getFrequencyQuotient(frequency, timeFrame,
                groceryDaysAWeek);

        if (foodItemCreationRequirementsMet(label, amount, timeFrame, frequency,
                frequencyQuotient)) {
            launchConfigurationsActivity(label, brand, amount, unit, info, timeFrame, frequency,
                    frequencyQuotient);
        }
    }

    private int getAmount() {
        String amountString = mAmountEditText.getText().toString();
        return amountString.equals("") ? AMOUNT_FIELD_EMPTY : Integer.parseInt(amountString);
    }

    private int getFrequency() {
        String frequencyString = mFrequencyEditText.getText().toString();
        return frequencyString.equals("") ? FREQUENCY_NOT_SET :
                Integer.parseInt(frequencyString);
    }

    private boolean foodItemCreationRequirementsMet(String label, int amount, TimeFrame timeFrame,
                                                    int frequency, double frequencyQuotient) {
        FoodItemCreationRequirementChecker checker =
                new FoodItemCreationRequirementChecker(mSharedPrefsHelper);
        try {
            return checker.requirementsMet(label, amount, timeFrame, frequency, frequencyQuotient);
        } catch (FoodItemCreationRequirementChecker.RequirementNotMetException e) {
            showSnackbar(e.getMessageResId());
            return false;
        }
    }

    private void launchConfigurationsActivity(String label, String brand, int amount, String unit,
                                              String info, TimeFrame timeFrame, int frequency,
                                              double frequencyQuotient) {
        Intent toConfigs = createIntentToConfigs(label, brand, amount, unit, info, timeFrame,
                frequency, frequencyQuotient);
        setResult(RESULT_OK, toConfigs);
        finish();
    }

    private void showSnackbar(int messageResourceId) {
        Snackbar.make(findViewById(R.id.fab_new_fooditem), messageResourceId, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab_new_fooditem).show();
    }

    private Intent createIntentToConfigs(String label, String brand, int amount, String unit,
                                         String info, TimeFrame timeFrame, int frequency,
                                         double frequencyQuotient) {
        Intent toConfigs = new Intent(this, ConfigurationsActivity.class);
        toConfigs.putExtra("label", label);
        toConfigs.putExtra("brand", brand);
        toConfigs.putExtra("amount", amount);
        toConfigs.putExtra("unit", unit);
        toConfigs.putExtra("info", info);
        toConfigs.putExtra("time_frame", timeFrame);
        toConfigs.putExtra("frequency", frequency);
        toConfigs.putExtra("uri", mImagePath);
        toConfigs.putExtra("id", mId);
        toConfigs.putExtra("countdownValue", mCountdownValue);
        toConfigs.putExtra("frequencyQuotient", frequencyQuotient);
        return toConfigs;
    }

    /**
     * Called when the camera icon is clicked. Launches an implicit intent to a camera app for
     * taking a picture of a food-item.
     *
     * @param view The view that has been clicked, in this case, the FAB.
     *             Default parameter required by the system.
     */
    public void onFoodImageClick(View view) {
        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraAppExists(captureImageIntent)) launchCameraApp(captureImageIntent);
    }

    private void launchCameraApp(Intent captureImageIntent) {
        File imageFile = Objects.requireNonNull(getImageFile());
        Uri imageUri = FileProvider.getUriForFile(this, "com.bsuuv.android.fileprovider",
                imageFile);
        captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(captureImageIntent, RequestValidator.REQUEST_IMAGE_CAPTURE);
    }

    private File getImageFile() {
        File imageFile = null;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            e.getMessage();
        }
        return imageFile;
    }

    private File createImageFile() throws IOException {
        String imageFileName = getImageFileName();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mImagePath = Uri.parse(image.toURI().getPath()).getPath();
        return image;
    }

    private String getImageFileName() {
        @SuppressLint("SimpleDateFormat")
        String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        return "JPEG_" + timestamp + "_";
    }

    private boolean cameraAppExists(Intent takePictureIntent) {
        return takePictureIntent.resolveActivity(getPackageManager()) != null;
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

    private TimeFrame getActiveToggleButton() {
        switch (mToggleGroup.getCheckedButtonId()) {
            case R.id.togglebutton_week:
                return TimeFrame.WEEK;
            case R.id.togglebutton_two_weeks:
                return TimeFrame.TWO_WEEKS;
            case R.id.togglebutton_month:
                return TimeFrame.MONTH;
            default:
                return TimeFrame.NULL;
        }
    }
}
