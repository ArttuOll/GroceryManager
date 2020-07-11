package com.bsuuv.grocerymanager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * A class representing a food-item in the grocery list.
 */
@Entity
public class FoodItem {

    @ColumnInfo(name = "image_uri")
    private String mImageUri;

    @NonNull
    @ColumnInfo(name = "label")
    private String mLabel;

    @ColumnInfo(name = "brand")
    private String mBrand;

    @ColumnInfo(name = "info")
    private String mInfo;

    @ColumnInfo(name = "amount")
    private int mAmount;

    @ColumnInfo(name = "unit")
    private String mUnit;

    @ColumnInfo(name = "time_frame")
    private int mTimeFrame;

    @ColumnInfo(name = "frequency")
    private int mFrequency;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private UUID mId;

    public FoodItem(@NotNull String label, String brand, String info, int amount, String unit,
                    int timeFrame,
                    int frequency, String mImageUri) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mUnit = unit;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.mImageUri = mImageUri;
        this.mId = UUID.randomUUID();
    }

    public FoodItem(@NotNull String label, String brand, String info, int amount, String unit,
                    int timeFrame,
                    int frequency, String mImageUri, @NotNull UUID id) {
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mUnit = unit;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.mImageUri = mImageUri;
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

    public String getmImageUri() {
        return mImageUri;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return mAmount == foodItem.mAmount &&
                mTimeFrame == foodItem.mTimeFrame &&
                mFrequency == foodItem.mFrequency &&
                Objects.equals(mImageUri, foodItem.mImageUri) &&
                mLabel.equals(foodItem.mLabel) &&
                Objects.equals(mBrand, foodItem.mBrand) &&
                Objects.equals(mInfo, foodItem.mInfo) &&
                Objects.equals(mUnit, foodItem.mUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mImageUri, mLabel, mBrand, mInfo, mAmount, mUnit, mTimeFrame,
                mFrequency);
    }

    @NonNull
    @Override
    public String toString() {
        return getLabel() + " " + getBrand() + " " + getAmount() + " " + getInfo();
    }
}