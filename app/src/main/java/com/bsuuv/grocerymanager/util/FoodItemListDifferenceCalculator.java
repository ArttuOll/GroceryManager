package com.bsuuv.grocerymanager.util;

import androidx.recyclerview.widget.DiffUtil;
import com.bsuuv.grocerymanager.data.model.FoodItem;
import java.util.List;

/**
 * Utility class for mutating one list of <code>FoodItem</code>s to another efficiently. This class
 * is used in this app's <code>RecyclerView Adapter</code>s in order to do as little re-rendering of
 * items as possible when updating the
 * <code>RecyclerView</code> contents.
 */
public class FoodItemListDifferenceCalculator {

  /**
   * Calculates an optimal set of update-operations to migrate from the old list to the new one.
   * @param oldFoodItems List to migrate from
   * @param newFoodItems List to migrate to
   * @return <code>DiffUtil.DiffResult</code>-object containing the update-operations.
   */
  public static DiffUtil.DiffResult calculateMigrationOperations(
      List<? extends FoodItem> oldFoodItems
      , List<? extends FoodItem> newFoodItems) {
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
        FoodItem oldFoodItem = oldFoodItems.get(oldItemPosition);
        FoodItem newFoodItem = newFoodItems.get(newItemPosition);
        return idAndVisibleMembersEqual(oldFoodItem, newFoodItem);
      }
    });
  }

  private static boolean idAndVisibleMembersEqual(FoodItem oldFoodItem,
      FoodItem newFoodItem) {
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
