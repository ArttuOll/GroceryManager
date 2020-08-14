package com.bsuuv.grocerymanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CameraUtil {

  private File mImageFile;
  private Context mContext;

  public CameraUtil(Context context) {
    this.mContext = context;
    this.mImageFile = Objects.requireNonNull(createImageFile());
  }

  private File createImageFile() {
    String imageFileName = getImageFileName();
    File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    try {
      return File.createTempFile(imageFileName, ".jpg", storageDir);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private String getImageFileName() {
    @SuppressLint("SimpleDateFormat")
    String timestamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
    return "JPEG_" + timestamp + "_";
  }

  public Intent getIntentToCaptureImage() {
    Intent toCaptureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Uri imageUri = FileProvider.getUriForFile(mContext, "com.bsuuv.android.fileprovider",
        mImageFile);
    toCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    return toCaptureImage;
  }

  public String getImagePath() {
    return Uri.parse(mImageFile.toURI().getPath()).getPath();
  }

  public boolean cameraAppExists(Intent takePictureIntent) {
    return takePictureIntent.resolveActivity(mContext.getPackageManager()) != null;
  }
}
