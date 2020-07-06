package com.bsuuv.grocerymanager.domain;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a food-item in the grocery list.
 */
public class FoodItem {

    private String imageUri;
    private String mLabel;
    private String mBrand;
    private String mInfo;
    private int mAmount;
    private String mUnit;
    private int mTimeFrame;
    private int mFrequency;
    private UUID mId;

    public FoodItem(String label, String brand, String info, int amount, String unit, int timeFrame,
                    int frequency, String imageUri) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mUnit = unit;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.imageUri = imageUri;
        this.mId = UUID.randomUUID();
    }

    public FoodItem(String label, String brand, String info, int amount, String unit, int timeFrame,
                    int frequency, String imageUri, UUID id) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mUnit = unit;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.imageUri = imageUri;
        this.mId = id;
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

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return mAmount == foodItem.mAmount &&
                mTimeFrame == foodItem.mTimeFrame &&
                mFrequency == foodItem.mFrequency &&
                Objects.equals(imageUri, foodItem.imageUri) &&
                mLabel.equals(foodItem.mLabel) &&
                Objects.equals(mBrand, foodItem.mBrand) &&
                Objects.equals(mInfo, foodItem.mInfo) &&
                Objects.equals(mUnit, foodItem.mUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUri, mLabel, mBrand, mInfo, mAmount, mUnit, mTimeFrame,
                mFrequency);
    }

    @NonNull
    @Override
    public String toString() {
        return getLabel() + " " + getBrand() + " " + getAmount() + " " + getInfo();
    }
}