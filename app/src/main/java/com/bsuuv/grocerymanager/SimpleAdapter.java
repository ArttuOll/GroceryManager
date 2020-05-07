package com.bsuuv.grocerymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<FoodItem> mFoodItems;

    SimpleAdapter(Context context, FoodItem[] data) {
        mContext = context;
        if (data != null)
            mFoodItems = new ArrayList<>(Arrays.asList(data));
        else mFoodItems = new ArrayList<>();
    }

    public void add(FoodItem foodItem, int position) {
        position = position == -1 ? getItemCount() : position;
        mFoodItems.add(position, foodItem);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mFoodItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.configlist_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        FoodItem currentFoodItem = mFoodItems.get(position);

        Glide.with(mContext).load(currentFoodItem.getImageResource()).into(holder.mFoodImage);
        holder.foodItemLabel.setText(currentFoodItem.getLabel());
        holder.foodItemBrand.setText(currentFoodItem.getBrand());
        holder.foodItemSize.setText(currentFoodItem.mSize());
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView foodItemLabel;
        final TextView foodItemBrand;
        final TextView foodItemSize;
        ImageView mFoodImage;

        SimpleViewHolder(View view) {
            super(view);
            mFoodImage = itemView.findViewById(R.id.configlist_food_image);
            mFoodImage.setClipToOutline(true);
            foodItemLabel = itemView.findViewById(R.id.config_item_label);
            foodItemBrand = itemView.findViewById(R.id.config_item_brand);
            foodItemSize = itemView.findViewById(R.id.config_item_size);
        }
    }
}
