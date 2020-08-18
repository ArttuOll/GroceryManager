package com.bsuuv.grocerymanager.ui.util;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import com.bsuuv.grocerymanager.ui.NewFoodItemActivity;

/**
 * Utility class for intepreting results of different kinds of <code>Activity</code>-related
 * requests.
 */
public class RequestValidator {

  public static final int NONE = 0;
  public static final int FOOD_ITEM_CREATE_REQUEST = 1;
  public static final int FOOD_ITEM_EDIT_REQUEST = 2;
  public static final int REQUEST_IMAGE_CAPTURE = 3;

  /**
   * @param requestCode     Request code used to launch {@link NewFoodItemActivity}
   * @param resultCode      Request code received from {@link NewFoodItemActivity}
   * @param fromNewFoodItem Intent received from {@link NewFoodItemActivity}
   * @return Boolean telling whether the food-item creation was successful or not
   */
  public static boolean foodItemCreationSuccesful(int requestCode, int resultCode,
      Intent fromNewFoodItem) {
    return requestCode == FOOD_ITEM_CREATE_REQUEST &&
        resultCode == RESULT_OK &&
        fromNewFoodItem != null;
  }

  /**
   * @param requestCode     Request code used to launch {@link NewFoodItemActivity}
   * @param resultCode      Request code received from {@link NewFoodItemActivity}
   * @param fromNewFoodItem Intent received from {@link NewFoodItemActivity}
   * @return Boolean telling whether the food-item edit was successful or not
   */
  public static boolean foodItemEditSuccesful(int requestCode, int resultCode,
      Intent fromNewFoodItem) {
    return requestCode == FOOD_ITEM_EDIT_REQUEST &&
        resultCode == RESULT_OK &&
        fromNewFoodItem != null;
  }

  /**
   * @param requestCode   Request code used to launch {@link NewFoodItemActivity}
   * @param resultCode    Request code received from {@link NewFoodItemActivity}
   * @param fromCameraApp Intent received from the device's camera app
   * @return Boolean telling whether the image capture was successful or not
   */
  public static boolean imageCaptureSuccesful(int requestCode, int resultCode,
      Intent fromCameraApp) {
    return requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && fromCameraApp != null;
  }
}
