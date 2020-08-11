package com.bsuuv.grocerymanager.util;

import android.content.Intent;

import static android.app.Activity.RESULT_OK;

public class RequestValidator {

    public static final int NONE = 0;
    public static final int FOOD_ITEM_CREATE_REQUEST = 1;
    public static final int FOOD_ITEM_EDIT_REQUEST = 2;
    public static final int REQUEST_IMAGE_CAPTURE = 3;

    public static boolean foodItemCreationSuccesful(int requestCode, int resultCode,
                                                    Intent fromNewFoodItem) {
        return requestCode == FOOD_ITEM_CREATE_REQUEST &&
                resultCode == RESULT_OK &&
                fromNewFoodItem != null;
    }

    public static boolean foodItemEditSuccesful(int requestCode, int resultCode,
                                                Intent fromNewFoodItem) {
        return requestCode == FOOD_ITEM_EDIT_REQUEST &&
                resultCode == RESULT_OK &&
                fromNewFoodItem != null;
    }

    public static boolean imageCaptureSuccesful(int requestCode, int resultCode, Intent intent) {
        return requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && intent != null;
    }
}
