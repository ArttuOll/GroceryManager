package com.bsuuv.grocerymanager.util;

import androidx.recyclerview.widget.DiffUtil;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import java.util.List;

public class FoodItemListDifferenceCalculator {

  public static DiffUtil.DiffResult calculateMigrationOperations(List<FoodItemEntity> oldFoodItems
      , List<FoodItemEntity> newFoodItems) {
    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
      @Override
      public int getOldListSize() {
        return oldFoodItems.size();
      }

      @Override
      public int getNewListSize() {
        return newFoodItems.size();
      }

      @Override
      public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFoodItems.get(oldItemPosition).getId() ==
            newFoodItems.get(newItemPosition).getId();
      }

      @Override
      public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        FoodItemEntity oldFoodItem = oldFoodItems.get(oldItemPosition);
        FoodItemEntity newFoodItem = newFoodItems.get(newItemPosition);
        return idAndVisibleMembersEqual(oldFoodItem, newFoodItem);
      }
    });
  }

  private static boolean idAndVisibleMembersEqual(FoodItemEntity oldFoodItem,
      FoodItemEntity newFoodItem) {
    return oldFoodItem.getId() == newFoodItem.getId() &&
        oldFoodItem.getAmount() == newFoodItem.getAmount() &&
        oldFoodItem.getBrand().equals(newFoodItem.getBrand()) &&
        oldFoodItem.getFrequency() == newFoodItem.getFrequency() &&
        oldFoodItem.getImageUri().equals(newFoodItem.getImageUri()) &&
        oldFoodItem.getInfo().equals(newFoodItem.getInfo()) &&
        oldFoodItem.getLabel().equals(newFoodItem.getLabel()) &&
        oldFoodItem.getTimeFrame() == newFoodItem.getTimeFrame() &&
        oldFoodItem.getUnit().equals(newFoodItem.getUnit());

  }
}
