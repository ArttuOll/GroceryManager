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
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.NewFoodItemActivity;
import com.bsuuv.grocerymanager.util.FoodItemListDifferenceCalculator;
import com.bsuuv.grocerymanager.util.ImageViewPopulater;
import com.bsuuv.grocerymanager.util.PluralsProvider;
import com.bsuuv.grocerymanager.util.RequestValidator;
import java.util.List;

public class ConfigurationsListAdapter extends
    RecyclerView.Adapter<ConfigurationsListAdapter.ConfigsViewHolder> {

  private List<FoodItemEntity> mFoodItems;
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
    FoodItemEntity currentFoodItem = mFoodItems.get(position);
    holder.bindTo(currentFoodItem);
  }

  @Override
  public int getItemCount() {
    return mFoodItems == null ? 0 : mFoodItems.size();
  }

  public FoodItemEntity getFoodItemAtPosition(int position) {
    return mFoodItems.get(position);
  }

  public void setFoodItems(List<FoodItemEntity> newFoodItems) {
    if (this.mFoodItems == null) {
      initFoodItems(newFoodItems);
    } else {
      updateFoodItems(newFoodItems);
    }
  }

  private void initFoodItems(List<FoodItemEntity> newFoodItems) {
    this.mFoodItems = newFoodItems;
    notifyItemRangeInserted(0, newFoodItems.size());
  }

  private void updateFoodItems(List<FoodItemEntity> newFoodItems) {
    DiffUtil.DiffResult migrationOperations = FoodItemListDifferenceCalculator
        .calculateMigrationOperations(mFoodItems, newFoodItems);
    mFoodItems = newFoodItems;
    migrationOperations.dispatchUpdatesTo(this);
  }

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

    void bindTo(FoodItemEntity currentFoodItem) {
      setInputFieldValues(currentFoodItem);
      String uri = currentFoodItem.getImageUri() != null ? currentFoodItem.getImageUri() : "";
      ImageViewPopulater.populateFromUri(mContext, uri, mFoodImage);
    }

    private void setInputFieldValues(FoodItemEntity currentFoodItem) {
      mFoodItemLabel.setText(currentFoodItem.getLabel());
      mFoodItemBrand.setText(currentFoodItem.getBrand());
      mFoodItemAmount.setText(mPluralsProvider.getAmountString(currentFoodItem.getAmount(),
          currentFoodItem.getUnit()));
      mSchedule.setText(mPluralsProvider.getScheduleString(currentFoodItem.getFrequency(),
          currentFoodItem.getTimeFrame()));
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
      FoodItemEntity currentFoodItem = mFoodItems.get(getAdapterPosition());
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

    private String getUri(FoodItemEntity foodItem) {
      return (foodItem.getImageUri() != null) ? foodItem.getImageUri() : "";
    }
  }
}
