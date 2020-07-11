package com.bsuuv.grocerymanager.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bsuuv.grocerymanager.model.FoodItem;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A class representing a food-item in the grocery list.
 */
@Entity
public class FoodItemEntity implements FoodItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;
    @ColumnInfo(name = "image_uri")
    private String mImageUri;
    @PrimaryKey
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

    @Ignore
    public FoodItemEntity(int id, @NotNull String label, String brand, String info, int amount,
                          String unit,
                          int timeFrame,
                          int frequency, String mImageUri) {
        this.mId = id;
        this.mLabel = label;
        this.mBrand = brand;
        this.mInfo = info;
        this.mAmount = amount;
        this.mUnit = unit;
        this.mTimeFrame = timeFrame;
        this.mFrequency = frequency;
        this.mImageUri = mImageUri;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
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
        return mImageUri;
    }

    public String getBrand() {
        return mBrand;
    }

    public String getUnit() {
        return mUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItemEntity foodItem = (FoodItemEntity) o;
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