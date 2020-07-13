package com.bsuuv.grocerymanager.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.Configurations;
import com.bsuuv.grocerymanager.ui.NewFoodItem;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Connects food-item data (list of food-items created by the user) to <code>RecyclerView</code> in
 * Configurations.
 */
public class ConfigurationsListAdapter extends RecyclerView.Adapter<ConfigurationsListAdapter.ConfigsViewHolder> {

    private List<FoodItemEntity> mFoodItems;
    private LayoutInflater mInflater;
    // Represents the activity in which this the RecyclerView of this adapter resides.
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
        if (mFoodItems == null) { return 0; } else return mFoodItems.size();
    }

    public FoodItemEntity getFoodItemAtPosition(int position) {
        return mFoodItems.get(position);
    }

    public void setFoodItems(List<FoodItemEntity> foodItemEntities) {
        this.mFoodItems = foodItemEntities;
    }

    /**
     * Contains a single item displayed in Configurations <code>RecyclerView</code>.
     */
    class ConfigsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ConfigurationsListAdapter mAdapter;
        private final TextView mFoodItemLabel;
        private final TextView mFoodItemBrand;
        private final TextView mFoodItemAmount;
        private final TextView mSchedule;
        private final ImageView mFoodImage;


        ConfigsViewHolder(View itemView, ConfigurationsListAdapter adapter) {
            super(itemView);

            mFoodImage = itemView.findViewById(R.id.configlist_food_image);
            mFoodImage.setClipToOutline(true);
            mFoodItemLabel = itemView.findViewById(R.id.config_item_label);
            mFoodItemBrand = itemView.findViewById(R.id.config_item_brand);
            mFoodItemAmount = itemView.findViewById(R.id.config_item_amount);
            mSchedule = itemView.findViewById(R.id.config_item_schedule);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        /**
         * Set values to all views inside a single item in Configurations RecyclerView.
         *
         * @param currentFoodItem The Configurations RecyclerView item that is to be displayed next.
         */
        void bindTo(FoodItemEntity currentFoodItem) {
            mFoodItemLabel.setText(currentFoodItem.getLabel());
            mFoodItemBrand.setText(currentFoodItem.getBrand());
            mFoodItemAmount.setText(String.format("%s %s", currentFoodItem.getAmount(),
                    currentFoodItem.getUnit()));
            mSchedule.setText(getScheduleString(currentFoodItem.getFrequency(),
                    currentFoodItem.getTimeFrame()));
            Glide.with(mContext).load(new File(String.valueOf(currentFoodItem.getImageUri())))
                    .into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItemEntity currentFoodItem = mFoodItems.get(getAdapterPosition());

            Intent toNewFoodItem = new Intent(mContext, NewFoodItem.class);
            toNewFoodItem.putExtra("label", currentFoodItem.getLabel());
            toNewFoodItem.putExtra("brand", currentFoodItem.getBrand());
            toNewFoodItem.putExtra("info", currentFoodItem.getInfo());
            toNewFoodItem.putExtra("amount", currentFoodItem.getAmount());
            toNewFoodItem.putExtra("unit", currentFoodItem.getUnit());
            toNewFoodItem.putExtra("time_frame", currentFoodItem.getTimeFrame());
            toNewFoodItem.putExtra("frequency", currentFoodItem.getFrequency());
            toNewFoodItem.putExtra("id", currentFoodItem.getId());
            toNewFoodItem.putExtra("editPosition", getAdapterPosition());

            String uri = (currentFoodItem.getImageUri() != null) ?
                    currentFoodItem.getImageUri() : "";
            toNewFoodItem.putExtra("uri", uri);

            Activity configurations = (Activity) mContext;
            configurations.startActivityForResult(toNewFoodItem,
                    Configurations.FOOD_ITEM_EDIT_REQUEST);
        }

        private String getScheduleString(int frequency, int timeFrame) {
            switch (timeFrame) {
                case 1:
                    return mContext.getResources().getQuantityString(
                            R.plurals.times_a_week, frequency, frequency);
                case 2:
                    return mContext.getResources().getQuantityString(
                            R.plurals.times_in_two_weeks, frequency, frequency);
                case 4:
                    return mContext.getResources().getQuantityString(
                            R.plurals.times_in_a_month, frequency, frequency);
                default:
                    return "";
            }
        }
    }
}
