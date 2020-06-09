package com.bsuuv.grocerymanager.domain;

/**
 * A class representing a food-item in the grocery list.
 */
public class FoodItem {

    private final String imageUri;
    private String mLabel;
    private String mBrand;
    private String mInfo;
    private int mAmount;
    private int mTimeFrame;
    private int mFrequency;

    public FoodItem(String label, String brand, String info, int amount, int timeFrame,
                    int frequency, String imageUri) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.imageUri = imageUri;
    }

    public int getTimeFrame() {
        return mTimeFrame;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public int getAmount() {
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
}