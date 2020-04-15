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

    public FoodItem(String mTitle, String mInfo, String weight, int imageResource) {
        this.mTitle = mTitle;
        this.mInfo = mInfo;
        this.mWeight = weight;
        this.imageResource = imageResource;
    }

    private String getmWeight() {
        return mWeight;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmInfo() {
        return mInfo;
    }

    public int getImageResource() {
        return imageResource;
    }
}