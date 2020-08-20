package com.bsuuv.grocerymanager.ui.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.data.model.FoodItem;
import com.bsuuv.grocerymanager.ui.ConfigurationsActivity;
import com.bsuuv.grocerymanager.ui.MainActivity;
import com.bsuuv.grocerymanager.ui.NewFoodItemActivity;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter.GroceryViewHolder;
import com.bsuuv.grocerymanager.ui.util.FoodItemListDifferenceCalculator;
import com.bsuuv.grocerymanager.ui.util.ImageViewPopulater;
import com.bsuuv.grocerymanager.ui.util.PluralsProvider;
import com.bsuuv.grocerymanager.ui.util.RequestValidator;
import java.util.List;

/**
 * Adapter that feeds food-items in the form of {@link ConfigsViewHolder}s to the
 * <code>RecyclerView</code> in {@link ConfigurationsActivity}.
 *
 * @see GroceryViewHolder
 * @see MainActivity
 */
public class ConfigurationsListAdapter extends
    RecyclerView.Adapter<ConfigurationsListAdapter.ConfigsViewHolder> {

  private List<? extends FoodItem> mFoodItems;
  private LayoutInflater mInflater;
  private Context mContext;

  public ConfigurationsListAdapter(Context context) {
    this.mInflater = LayoutInflater.from(context);
    this.mContext = context;
  }

  @NonNull
  @Override
  public ConfigurationsListAdapter.ConfigsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View itemView = mInflater.inflate(R.layout.configlist_item, parent, false);
    return new ConfigurationsListAdapter.ConfigsViewHolder(itemView, this);
  }

  @Override
  public void onBindViewHolder(@NonNull ConfigurationsListAdapter.ConfigsViewHolder holder,
      int position) {
    FoodItem currentFoodItem = mFoodItems.get(position);
    holder.bindTo(currentFoodItem);
  }

  @Override
  public int getItemCount() {
    return mFoodItems == null ? 0 : mFoodItems.size();
  }

  public FoodItem getFoodItemAtPosition(int position) {
    return mFoodItems.get(position);
  }

  /**
   * Sets the list of food-items that functions as a data source for this adapter. If already set,
   * calculates an optimal set of update operations to migrate from the old list to the new one.
   *
   * @param newFoodItems List of food-items that should be displayed in the
   *                     <code>RecyclerView</code> in {@link ConfigurationsActivity}
   * @see ConfigurationsActivity
   */
  public void setFoodItems(List<? extends FoodItem> newFoodItems) {
    if (this.mFoodItems == null) {
      initFoodItems(newFoodItems);
    } else {
      updateFoodItems(newFoodItems);
    }
  }

  private void initFoodItems(List<? extends FoodItem> newFoodItems) {
    this.mFoodItems = newFoodItems;
    notifyItemRangeInserted(0, newFoodItems.size());
  }

  private void updateFoodItems(List<? extends FoodItem> newFoodItems) {
    DiffUtil.DiffResult migrationOperations = FoodItemListDifferenceCalculator
        .calculateMigrationOperations(mFoodItems, newFoodItems);
    mFoodItems = newFoodItems;
    migrationOperations.dispatchUpdatesTo(this);
  }

  /**
   * A <code>ViewHolder</code> containing one food-item to be displayed in the
   * <code>RecyclerView</code> in {@link ConfigurationsActivity}.
   *
   * @see ConfigurationsActivity
   */
  class ConfigsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ConfigurationsListAdapter mAdapter;
    private TextView mFoodItemLabel, mFoodItemBrand, mFoodItemAmount, mSchedule;
    private ImageView mFoodImage;
    private PluralsProvider mPluralsProvider;

    ConfigsViewHolder(View itemView, ConfigurationsListAdapter adapter) {
      super(itemView);
      itemView.setOnClickListener(this);
      initMembers(adapter);
    }

    private void initMembers(ConfigurationsListAdapter adapter) {
      this.mFoodImage = itemView.findViewById(R.id.configlist_food_image);
      this.mFoodImage.setClipToOutline(true);
      this.mFoodItemLabel = itemView.findViewById(R.id.config_item_label);
      this.mFoodItemBrand = itemView.findViewById(R.id.config_item_brand);
      this.mFoodItemAmount = itemView.findViewById(R.id.config_item_amount);
      this.mSchedule = itemView.findViewById(R.id.config_item_schedule);
      this.mAdapter = adapter;
      this.mPluralsProvider = new PluralsProvider(mContext);
    }

    void bindTo(FoodItem currentFoodItem) {
      setInputFieldValues(currentFoodItem);
      String uri = currentFoodItem.getImageUri() != null ? currentFoodItem.getImageUri() : "";
      ImageViewPopulater.populateFromUri(mContext, uri, mFoodImage);
    }

    private void setInputFieldValues(FoodItem currentFoodItem) {
      mFoodItemLabel.setText(currentFoodItem.getLabel());
      setFoodItemBrandText(currentFoodItem.getBrand());
      mFoodItemAmount.setText(mPluralsProvider.getAmountString(currentFoodItem.getAmount(),
          currentFoodItem.getUnit()));
      mSchedule.setText(mPluralsProvider.getScheduleString(currentFoodItem.getFrequency(),
          currentFoodItem.getTimeFrame()));
    }

    private void setFoodItemBrandText(String brand) {
      if (brand.equals("")) {
        mFoodItemBrand.setVisibility(View.GONE);
      } else {
        mFoodItemBrand.setText(brand);
      }
    }

    @Override
    public void onClick(View v) {
      Intent toNewFoodItem = createIntentToNewFoodItem();
      launchConfigurationsActivity(toNewFoodItem);
    }

    private void launchConfigurationsActivity(Intent toNewFoodItem) {
      Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, mFoodImage,
          mFoodImage.getTransitionName()).toBundle();
      Activity configurations = (Activity) mContext;
      configurations.startActivityForResult(toNewFoodItem,
          RequestValidator.FOOD_ITEM_EDIT_REQUEST, bundle);
    }

    private Intent createIntentToNewFoodItem() {
      FoodItem currentFoodItem = mFoodItems.get(getAdapterPosition());
      Intent toNewFoodItem = new Intent(mContext, NewFoodItemActivity.class);
      toNewFoodItem.putExtra("label", currentFoodItem.getLabel());
      toNewFoodItem.putExtra("brand", currentFoodItem.getBrand());
      toNewFoodItem.putExtra("info", currentFoodItem.getInfo());
      toNewFoodItem.putExtra("amount", currentFoodItem.getAmount());
      toNewFoodItem.putExtra("unit", currentFoodItem.getUnit());
      toNewFoodItem.putExtra("time_frame", currentFoodItem.getTimeFrame());
      toNewFoodItem.putExtra("frequency", currentFoodItem.getFrequency());
      toNewFoodItem.putExtra("id", currentFoodItem.getId());
      toNewFoodItem.putExtra("editPosition", getAdapterPosition());
      toNewFoodItem.putExtra("countdownValue", currentFoodItem.getCountdownValue());
      toNewFoodItem.putExtra("requestCode", RequestValidator.FOOD_ITEM_EDIT_REQUEST);
      toNewFoodItem.putExtra("uri", getUri(currentFoodItem));
      return toNewFoodItem;
    }

    private String getUri(FoodItem foodItem) {
      return (foodItem.getImageUri() != null) ? foodItem.getImageUri() : "";
    }
  }
}
