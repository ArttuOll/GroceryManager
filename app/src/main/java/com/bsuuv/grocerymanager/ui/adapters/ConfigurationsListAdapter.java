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
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.ConfigurationsActivity;
import com.bsuuv.grocerymanager.ui.NewFoodItem;
import com.bsuuv.grocerymanager.util.FoodItemListDifferenceCalculator;
import com.bsuuv.grocerymanager.util.PluralsProvider;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ConfigurationsListAdapter extends RecyclerView.Adapter<ConfigurationsListAdapter.ConfigsViewHolder> {

    private List<FoodItemEntity> mFoodItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public ConfigurationsListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public ConfigurationsListAdapter.ConfigsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            this.mFoodItems = newFoodItems;
            notifyItemRangeInserted(0, newFoodItems.size());
        } else {
            DiffUtil.DiffResult migrationOperations = FoodItemListDifferenceCalculator
                    .calculateMigrationOperations(mFoodItems, newFoodItems);
            mFoodItems = newFoodItems;
            migrationOperations.dispatchUpdatesTo(this);
        }
    }

    class ConfigsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ConfigurationsListAdapter mAdapter;
        private final TextView mFoodItemLabel, mFoodItemBrand, mFoodItemAmount, mSchedule;
        private final ImageView mFoodImage;
        private final PluralsProvider mPluralsProvider;

        ConfigsViewHolder(View itemView, ConfigurationsListAdapter adapter) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.configlist_food_image);
            mFoodImage.setClipToOutline(true);
            mFoodItemLabel = itemView.findViewById(R.id.config_item_label);
            mFoodItemBrand = itemView.findViewById(R.id.config_item_brand);
            mFoodItemAmount = itemView.findViewById(R.id.config_item_amount);
            mSchedule = itemView.findViewById(R.id.config_item_schedule);
            this.mAdapter = adapter;
            this.mPluralsProvider = new PluralsProvider(mContext);
            itemView.setOnClickListener(this);
        }

        void bindTo(FoodItemEntity currentFoodItem) {
            mFoodItemLabel.setText(currentFoodItem.getLabel());
            mFoodItemBrand.setText(currentFoodItem.getBrand());
            mFoodItemAmount.setText(mPluralsProvider.getAmountString(currentFoodItem.getAmount(),
                    currentFoodItem.getUnit()));
            mSchedule.setText(mPluralsProvider.getScheduleString(currentFoodItem.getFrequency(),
                    currentFoodItem.getTimeFrame()));
            Glide.with(mContext).load(new File(String.valueOf(currentFoodItem.getImageUri())))
                    .into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItemEntity currentFoodItem = mFoodItems.get(getAdapterPosition());
            Intent toNewFoodItem = createIntentToNewFoodItem(currentFoodItem);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                    mFoodImage, mFoodImage.getTransitionName()).toBundle();

            Activity configurations = (Activity) mContext;
            configurations.startActivityForResult(toNewFoodItem,
                    ConfigurationsActivity.FOOD_ITEM_EDIT_REQUEST, bundle);
        }

        private Intent createIntentToNewFoodItem(FoodItemEntity foodItem) {
            Intent toNewFoodItem = new Intent(mContext, NewFoodItem.class);
            toNewFoodItem.putExtra("label", foodItem.getLabel());
            toNewFoodItem.putExtra("brand", foodItem.getBrand());
            toNewFoodItem.putExtra("info", foodItem.getInfo());
            toNewFoodItem.putExtra("amount", foodItem.getAmount());
            toNewFoodItem.putExtra("unit", foodItem.getUnit());
            toNewFoodItem.putExtra("time_frame", foodItem.getTimeFrame());
            toNewFoodItem.putExtra("frequency", foodItem.getFrequency());
            toNewFoodItem.putExtra("id", foodItem.getId());
            toNewFoodItem.putExtra("editPosition", getAdapterPosition());
            toNewFoodItem.putExtra("countdownValue", foodItem.getCountdownValue());

            String uri = (foodItem.getImageUri() != null) ? foodItem.getImageUri() : "";
            toNewFoodItem.putExtra("uri", uri);

            return toNewFoodItem;
        }
    }
}
