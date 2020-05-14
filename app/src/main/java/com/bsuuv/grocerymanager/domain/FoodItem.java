package com.bsuuv.grocerymanager.domain;

/**
 * A class representing a food-item in a meal.
 * There is currently a total of 57 nutrients in a food-item and they are grouped into maps to keep them organized.
 */
public class FoodItem {

    private final int imageResource;
    private String mLabel;
    private String mBrand;
    private String mInfo;
    private String mAmount;
    private int mFrequency;

    public FoodItem(String label, String brand, String info, String amount, int frequency, int imageResource) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mFrequency = frequency;
        this.imageResource = imageResource;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
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

    public int getImageResource() {
        return imageResource;
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