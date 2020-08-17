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

/**
 * Utility class responsible for operations related to capturing and saving an image.
 */
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

  /**
   * @return Intent configured for capturing an image using the device's camera app
   */
  public Intent getIntentToCaptureImage() {
    Intent toCaptureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Uri imageUri = FileProvider.getUriForFile(mContext, "com.bsuuv.android.fileprovider",
        mImageFile);
    toCaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    return toCaptureImage;
  }

  /**
   * @return String URI of the last taken image
   */
  public String getImagePath() {
    return Uri.parse(mImageFile.toURI().getPath()).getPath();
  }

  /**
   * @param toCaptureImage Intent configured to launch the device's camera app
   * @return boolean telling if a camera app has been installed on the device
   */
  public boolean cameraAppExists(Intent toCaptureImage) {
    return toCaptureImage.resolveActivity(mContext.getPackageManager()) != null;
  }
}
