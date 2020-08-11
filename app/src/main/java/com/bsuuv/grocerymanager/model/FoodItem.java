package com.bsuuv.grocerymanager.model;

import com.bsuuv.grocerymanager.util.TimeFrame;

public interface FoodItem {

    int getId();

    String getImageUri();

    String getLabel();

    String getBrand();

    String getInfo();

    int getAmount();

    String getUnit();

    TimeFrame getTimeFrame();

    int getFrequency();

}
