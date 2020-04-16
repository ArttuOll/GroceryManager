package com.bsuuv.grocerymanager;

/**
 * A class representing a food-item in a meal.
 * There is currently a total of 57 nutrients in a food-item and they are grouped into maps to keep them organized.
 */
public class FoodItem {

    private final int imageResource;
    private String mTitle;
    private String mInfo;
    private String mWeight;
    private String mAmount;

    public FoodItem(String mTitle, String mInfo, String weight, String amount, int imageResource) {
        this.mTitle = mTitle;
        this.mInfo = mInfo;
        this.mWeight = weight;
        this.mAmount = amount;
        this.imageResource = imageResource;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getInfo() {
        return mInfo;
    }

    public int getImageResource() {
        return imageResource;
    }
}