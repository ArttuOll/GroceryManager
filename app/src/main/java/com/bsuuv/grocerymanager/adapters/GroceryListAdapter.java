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

import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private List<FoodItem> mFoodItems;
    private LayoutInflater mInflater;
    // Represent the activity in which this the RecyclerView of this adapter resides.
    private Context mContext;

    public GroceryListAdapter(Context context, List<FoodItem> foodItems) {
        this.mInflater = LayoutInflater.from(context);
        this.mFoodItems = foodItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public GroceryListAdapter.GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.grocerylist_item, parent, false);

        return new GroceryViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListAdapter.GroceryViewHolder holder, int position) {
        FoodItem currentFoodItem = mFoodItems.get(position);

        holder.bindTo(currentFoodItem);
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    class GroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView foodItemLabel;
        private final TextView foodItemBrand;
        private final TextView foodItemSize;
        final GroceryListAdapter mAdapter;
        private ImageView mFoodImage;

        GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.grocerylist_food_image);
            mFoodImage.setClipToOutline(true);
            foodItemLabel = itemView.findViewById(R.id.grocery_item_label);
            foodItemBrand = itemView.findViewById(R.id.grocery_item_brand);
            foodItemSize = itemView.findViewById(R.id.grocery_item_size);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        void bindTo(FoodItem currentFoodItem) {
            foodItemLabel.setText(currentFoodItem.getLabel());
            foodItemBrand.setText(currentFoodItem.getBrand());
            foodItemSize.setText(currentFoodItem.getAmount());
            Glide.with(mContext).load(currentFoodItem.getImageUri()).into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItem currentFoodItem = mFoodItems.get(getAdapterPosition());

            Intent foodItemDetail = new Intent(mContext, FoodItemDetail.class);
            foodItemDetail.putExtra("label", currentFoodItem.getLabel());
            foodItemDetail.putExtra("image_resource", currentFoodItem.getImageUri());
            foodItemDetail.putExtra("brand", currentFoodItem.getBrand());
            foodItemDetail.putExtra("info", currentFoodItem.getInfo());
            foodItemDetail.putExtra("amount", currentFoodItem.getAmount());
            mContext.startActivity(foodItemDetail);
        }
    }
}
