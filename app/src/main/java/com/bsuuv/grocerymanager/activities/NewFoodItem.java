package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bsuuv.grocerymanager.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFoodItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText mLabelEditText;
    private EditText mBrandEditText;
    private EditText mAmountEditText;
    private EditText mInfoEditText;
    private ImageView mFoodImageView;
    private Spinner mFreqSpinner;
    private String mCurrentPhotoPath;
    private int mFreq;

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
        this.mFreqSpinner = findViewById(R.id.frequency_spinner);

        setUpFreqSpinner();

        manageIntent();
    }

    public void onFabClick(View view) {
        String label = mLabelEditText.getText().toString();
        String brand = mBrandEditText.getText().toString();
        String amount = mAmountEditText.getText().toString();
        String info = mInfoEditText.getText().toString();
        int frequency = mFreq;

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String intString = parent.getItemAtPosition(position).toString();
        mFreq = Integer.parseInt(intString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null) populateFoodImageView(mCurrentPhotoPath);
            }
        }
    }

    private void manageIntent() {
        Intent fromConfigs = getIntent();
        if (fromConfigs != null) {
            this.mLabelEditText.setText(fromConfigs.getStringExtra("label"));
            this.mBrandEditText.setText(fromConfigs.getStringExtra("brand"));
            this.mAmountEditText.setText(fromConfigs.getStringExtra("amount"));
            this.mInfoEditText.setText(fromConfigs.getStringExtra("info"));

            mCurrentPhotoPath = fromConfigs.getStringExtra("uri");
            if (mCurrentPhotoPath != null) populateFoodImageView(mCurrentPhotoPath);

            int freqSpinnerPos = fromConfigs.getIntExtra("freq", 0) - 1;
            this.mFreqSpinner.setSelection(freqSpinnerPos);
        }
    }

    private void populateFoodImageView(String path) {
        Glide.with(this).load(new File(path)).into(mFoodImageView);
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

    private void setUpFreqSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFreqSpinner.setAdapter(adapter);
        mFreqSpinner.setOnItemSelectedListener(this);
    }
}
