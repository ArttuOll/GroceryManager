package com.bsuuv.grocerymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.util.CameraUtil;
import com.bsuuv.grocerymanager.ui.util.FoodItemCreationRequirementChecker;
import com.bsuuv.grocerymanager.ui.util.ImageViewPopulater;
import com.bsuuv.grocerymanager.ui.util.RequestValidator;
import com.bsuuv.grocerymanager.util.FrequencyQuotientCalculator;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.util.TimeFrame;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import java.util.Objects;

/**
 * Activity for creating new food-items. Displays a form with input fields corresponding to
 * different properties of a food-item:
 * <ul>
 * <li>An image, with a camera icon as a placeholder. Clicking the camera icon
 *     or an image launches the devices camera app, if one is installed.</li>
 * <li>A label</li>
 * <li>Brand</li>
 * <li>Amount</li>
 * <li>Unit for the amount</li>
 * <li>Frequency, that is, how many times in the chosen time frame (see next)
 *     the user wants this?</li>
 * <li>Time frame, that is, what is the time period during which the user wants
 *     to see this food-item appear on the grocery list the amount of times specified
 *     by the frequency?</li>
 * <li>Additional information the user wants to add</li>
 * </ul>
 * Some of these input fields are not mandatory. Finally, the view contains a
 * floating action button, which launches validation of the input fields and
 * afterwards sends their data to {@link ConfigurationsActivity}. For information
 * on required fields and their validation, see {@link FoodItemCreationRequirementChecker}
 *
 * @see ConfigurationsActivity
 * @see FoodItemCreationRequirementChecker
 * @see FoodItemEntity
 */
public class NewFoodItemActivity extends AppCompatActivity implements View.OnClickListener {

  private static final String IMAGE_PATH_KEY = "imagePath";
  private static final int FREQUENCY_NOT_SET = 0;
  private static final int AMOUNT_FIELD_EMPTY = 0;

  private MaterialButtonToggleGroup mTimeFrameButtons;
  private EditText mLabelField, mBrandField, mAmountField, mInfoField,
      mFrequencyField;
  private ImageView mFoodImage;
  private AutoCompleteTextView mUnitDropdown;
  private SharedPreferencesHelper mSharedPrefsHelper;
  private String mImageUri;
  private int mEditedFoodItemId;
  private double mEditedFoodItemCountdownValue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_food_item);
    setTitle(getString(R.string.newFoodItem_title));
    initMembers();
    setUpToggleButtons();
    manageIntent();
    if (savedInstanceState != null) {
      recoverFoodImage(savedInstanceState);
    }
  }

  private void initMembers() {
    this.mImageUri = "";
    this.mLabelField = findViewById(R.id.editText_label);
    this.mBrandField = findViewById(R.id.editText_brand);
    this.mAmountField = findViewById(R.id.editText_amount);
    this.mInfoField = findViewById(R.id.editText_info);
    this.mFrequencyField = initFrequencyEditText();
    this.mFoodImage = findViewById(R.id.imageView_new_fooditem);
    this.mSharedPrefsHelper = new SharedPreferencesHelper(this);
    this.mUnitDropdown = initUnitDropdown();
    this.mTimeFrameButtons = findViewById(R.id.freq_selection_togglegroup);
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
      this.mEditedFoodItemId = fromConfigs.getIntExtra("id", 0);
      this.mEditedFoodItemCountdownValue = fromConfigs.getDoubleExtra("countdownValue", 0.0);
    }
  }

  private boolean intentsToEditFoodItem(Intent intent) {
    return intent.getIntExtra("requestCode", RequestValidator.NONE) ==
        RequestValidator.FOOD_ITEM_EDIT_REQUEST;
  }

  private void setInputFieldValuesFromIntent(Intent intent) {
    this.mLabelField.setText(intent.getStringExtra("label"));
    this.mBrandField.setText(intent.getStringExtra("brand"));
    this.mAmountField.setText(String.valueOf(intent.getIntExtra("amount", 0)));
    this.mUnitDropdown.setText(intent.getStringExtra("unit"));
    this.mInfoField.setText(intent.getStringExtra("info"));
    this.mFrequencyField.setText(String.valueOf(intent.getIntExtra("frequency",
        FREQUENCY_NOT_SET)));
  }

  private void setImageFromIntent(Intent intent) {
    String uri = intent.getStringExtra("uri");
    mImageUri = uri == null ? "" : uri;
    ImageViewPopulater.populateFromUri(this, mImageUri, mFoodImage);
  }

  private void setToggleButtonStatesFromIntent(Intent fromConfigs) {
    TimeFrame timeFrame =
        (TimeFrame) Objects.requireNonNull(fromConfigs.getSerializableExtra("time_frame"));
    switch (timeFrame) {
      case WEEK:
        mTimeFrameButtons.check(R.id.togglebutton_week);
        break;
      case TWO_WEEKS:
        mTimeFrameButtons.check(R.id.togglebutton_two_weeks);
        break;
      case MONTH:
        mTimeFrameButtons.check(R.id.togglebutton_month);
        break;
    }
  }

  private void recoverFoodImage(Bundle savedInstanceState) {
    mImageUri = savedInstanceState.getString(IMAGE_PATH_KEY);
    ImageViewPopulater.populateFromUri(this, mImageUri, mFoodImage);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (RequestValidator.imageCaptureSuccesful(requestCode, resultCode, data)) {
      ImageViewPopulater.populateFromUri(this, mImageUri, mFoodImage);
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putString(IMAGE_PATH_KEY, mImageUri);
    super.onSaveInstanceState(outState);
  }

  public void onFabClick(View view) {
    String label = mLabelField.getText().toString();
    String brand = mBrandField.getText().toString();
    String unit = mUnitDropdown.getText().toString();
    String info = mInfoField.getText().toString();
    TimeFrame timeFrame = getActiveToggleButton();
    int amount = getAmount();
    int frequency = getFrequency();
    int groceryDaysAWeek = mSharedPrefsHelper.getGroceryDays().size();
    double frequencyQuotient = calculateFreqQuotient(frequency, timeFrame, groceryDaysAWeek);

    if (foodItemCreationRequirementsMet(label, amount, timeFrame, frequency,
        frequencyQuotient)) {
      launchConfigurationsActivity(label, brand, amount, unit, info, timeFrame, frequency,
          frequencyQuotient);
    }
  }

  private TimeFrame getActiveToggleButton() {
    switch (mTimeFrameButtons.getCheckedButtonId()) {
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

  private int getAmount() {
    String amountString = mAmountField.getText().toString();
    return amountString.equals("") ? AMOUNT_FIELD_EMPTY : Integer.parseInt(amountString);
  }

  private int getFrequency() {
    String frequencyString = mFrequencyField.getText().toString();
    return frequencyString.equals("") ? FREQUENCY_NOT_SET :
        Integer.parseInt(frequencyString);
  }

  private double calculateFreqQuotient(int frequency, TimeFrame timeFrame, int groceryDaysAWeek) {
    return FrequencyQuotientCalculator.calculate(frequency, timeFrame, groceryDaysAWeek);
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

  private void showSnackbar(int messageResourceId) {
    Snackbar.make(findViewById(R.id.fab_new_fooditem), messageResourceId, Snackbar.LENGTH_LONG)
        .setAnchorView(R.id.fab_new_fooditem).show();
  }

  private void launchConfigurationsActivity(String label, String brand, int amount, String unit,
      String info, TimeFrame timeFrame, int frequency,
      double frequencyQuotient) {
    Intent toConfigs = createIntentToConfigs(label, brand, amount, unit, info, timeFrame,
        frequency, frequencyQuotient);
    setResult(RESULT_OK, toConfigs);
    finish();
  }

  private Intent createIntentToConfigs(String label, String brand, int amount, String unit,
      String info, TimeFrame timeFrame, int frequency, double frequencyQuotient) {
    Intent toConfigs = new Intent(this, ConfigurationsActivity.class);
    toConfigs.putExtra("label", label);
    toConfigs.putExtra("brand", brand);
    toConfigs.putExtra("amount", amount);
    toConfigs.putExtra("unit", unit);
    toConfigs.putExtra("info", info);
    toConfigs.putExtra("time_frame", timeFrame);
    toConfigs.putExtra("frequency", frequency);
    toConfigs.putExtra("uri", mImageUri);
    toConfigs.putExtra("id", mEditedFoodItemId);
    toConfigs.putExtra("countdownValue", mEditedFoodItemCountdownValue);
    toConfigs.putExtra("frequencyQuotient", frequencyQuotient);
    return toConfigs;
  }

  public void onImageClick(View view) {
    CameraUtil cameraUtil = new CameraUtil(this);
    this.mImageUri = cameraUtil.getImagePath();
    Intent toCaptureImage = cameraUtil.getIntentToCaptureImage();
    if (cameraUtil.cameraAppExists(toCaptureImage)) {
      launchCameraApp(toCaptureImage);
    }
  }

  private void launchCameraApp(Intent toCaptureImage) {
    startActivityForResult(toCaptureImage, RequestValidator.REQUEST_IMAGE_CAPTURE);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.togglebutton_week:
        mTimeFrameButtons.check(R.id.togglebutton_week);
        break;
      case R.id.togglebutton_two_weeks:
        mTimeFrameButtons.check(R.id.togglebutton_two_weeks);
        break;
      case R.id.togglebutton_month:
        mTimeFrameButtons.check(R.id.togglebutton_month);
        break;
    }
  }
}
