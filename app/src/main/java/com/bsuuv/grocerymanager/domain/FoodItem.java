package com.bsuuv.grocerymanager.domain;

/**
 * A class representing a food-item in the grocery list.
 */
public class FoodItem {

    private final String imageUri;
    private String mLabel;
    private String mBrand;
    private String mInfo;
    private String mAmount;
    private int mFrequency;

    public FoodItem(String label, String brand, String info, String amount, int frequency, String imageUri) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mFrequency = frequency;
        this.imageUri = imageUri;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getInfo() {
        return mInfo;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getBrand() {
        return mBrand;
    }

    public interface Frequency {
        int BIWEEKLY = 0;
        int WEEKLY = 1;
        int MONTHLY = 2;
    }
}