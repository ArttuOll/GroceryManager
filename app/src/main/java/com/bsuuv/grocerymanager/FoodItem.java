package com.bsuuv.grocerymanager;

/**
 * A class representing a food-item in a meal.
 * There is currently a total of 57 nutrients in a food-item and they are grouped into maps to keep them organized.
 */
public class FoodItem {

    private final int imageResource;
    private String mLabel;
    private String mBrand;
    private String mInfo;
    private String mSize;
    private String mAmount;

    public FoodItem(String mTitle, String brand, String mInfo, String weight, String amount, int imageResource) {
        this.mLabel = mTitle;
        this.mBrand = brand;
        this.mInfo = mInfo;
        this.mSize = weight;
        this.mAmount = amount;
        this.imageResource = imageResource;
    }

    public String getAmount() {
        return mAmount;
    }

    public String mSize() {
        return mSize;
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
}