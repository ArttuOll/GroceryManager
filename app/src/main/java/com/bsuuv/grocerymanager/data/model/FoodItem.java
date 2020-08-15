package com.bsuuv.grocerymanager.data.model;

import com.bsuuv.grocerymanager.util.TimeFrame;

/**
 * Definition of a food-item on the grocery list.
 */
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

  double getCountdownValue();

}
