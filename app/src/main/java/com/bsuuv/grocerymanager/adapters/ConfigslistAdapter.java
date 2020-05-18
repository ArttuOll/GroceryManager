package com.bsuuv.grocerymanager.adapters;

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
import com.bsuuv.grocerymanager.activities.FoodItemDetail;
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
            foodItemSize = itemView.findViewById(R.id.config_item_size);
            foodItemFreq = itemView.findViewById(R.id.config_item_freq);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        void bindTo(FoodItem currentFoodItem) {
            foodItemLabel.setText(currentFoodItem.getLabel());
            foodItemBrand.setText(currentFoodItem.getBrand());
            foodItemSize.setText(currentFoodItem.getAmount());
            foodItemFreq.setText(String.valueOf(currentFoodItem.getFrequency()));
            Glide.with(mContext).load(new File(String.valueOf(currentFoodItem.getImageUri()))).into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItem currentFoodItem = mFoodItems.get(getAdapterPosition());

            Intent foodItemDetail = new Intent(mContext, FoodItemDetail.class);
            foodItemDetail.putExtra("title", currentFoodItem.getLabel());
            foodItemDetail.putExtra("brand", currentFoodItem.getBrand());
            foodItemDetail.putExtra("info", currentFoodItem.getInfo());
            foodItemDetail.putExtra("amount", currentFoodItem.getAmount());
            foodItemDetail.putExtra("uri", currentFoodItem.getImageUri());

            mContext.startActivity(foodItemDetail);
        }
    }
}
