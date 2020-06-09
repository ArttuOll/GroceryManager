package com.bsuuv.grocerymanager.activities.adapters;

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
import com.bsuuv.grocerymanager.activities.Configurations;
import com.bsuuv.grocerymanager.activities.NewFoodItem;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ConfigslistAdapter extends RecyclerView.Adapter<ConfigslistAdapter.ConfigsViewHolder> {

    private List<FoodItem> mFoodItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public ConfigslistAdapter(Context context, List<FoodItem> foodItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mFoodItems = foodItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ConfigslistAdapter.ConfigsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.configlist_item, parent, false);

        return new ConfigslistAdapter.ConfigsViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigslistAdapter.ConfigsViewHolder holder, int position) {
        FoodItem currentFoodItem = mFoodItems.get(position);

        holder.bindTo(currentFoodItem);
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    class ConfigsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ConfigslistAdapter mAdapter;
        private final TextView foodItemLabel;
        private final TextView foodItemBrand;
        private final TextView foodItemSize;
        private final TextView foodItemFreq;
        private final ImageView mFoodImage;

        ConfigsViewHolder(View itemView, ConfigslistAdapter adapter) {
            super(itemView);

            mFoodImage = itemView.findViewById(R.id.configlist_food_image);
            mFoodImage.setClipToOutline(true);
            foodItemLabel = itemView.findViewById(R.id.config_item_label);
            foodItemBrand = itemView.findViewById(R.id.config_item_brand);
            foodItemSize = itemView.findViewById(R.id.config_item_amount);
            foodItemFreq = itemView.findViewById(R.id.config_item_freq);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        void bindTo(FoodItem currentFoodItem) {
            foodItemLabel.setText(currentFoodItem.getLabel());
            foodItemBrand.setText(currentFoodItem.getBrand());
            foodItemSize.setText(currentFoodItem.getAmount());
            foodItemFreq.setText(String.valueOf(currentFoodItem.getTimeFrame()));
            Glide.with(mContext).load(new File(String.valueOf(currentFoodItem.getImageUri()))).into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItem currentFoodItem = mFoodItems.get(getAdapterPosition());

            Intent toNewFoodItem = new Intent(mContext, NewFoodItem.class);
            toNewFoodItem.putExtra("label", currentFoodItem.getLabel());
            toNewFoodItem.putExtra("brand", currentFoodItem.getBrand());
            toNewFoodItem.putExtra("info", currentFoodItem.getInfo());
            toNewFoodItem.putExtra("amount", currentFoodItem.getAmount());
            toNewFoodItem.putExtra("time_frame", currentFoodItem.getTimeFrame());
            toNewFoodItem.putExtra("frequency", currentFoodItem.getFrequency());
            toNewFoodItem.putExtra("editPosition", getAdapterPosition());

            String uri = (currentFoodItem.getImageUri() != null) ? currentFoodItem.getImageUri()
                    : "";
            toNewFoodItem.putExtra("uri", uri);

            Activity configurations = (Activity) mContext;
            configurations.startActivityForResult(toNewFoodItem,
                    Configurations.FOOD_ITEM_EDIT_REQUEST);
        }
    }
}
