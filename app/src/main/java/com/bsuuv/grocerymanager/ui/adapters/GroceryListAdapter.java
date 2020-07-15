package com.bsuuv.grocerymanager.ui.adapters;

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
import com.bsuuv.grocerymanager.ui.FoodItemDetail;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Connects food-item data (list of food-items created by the user) to <code>RecyclerView</code> in
 * MainActivity.
 */
public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private List<FoodItemEntity> mGroceryItems;
    private LayoutInflater mInflater;
    // Represents the activity in which this the RecyclerView of this adapter resides.
    private Context mContext;

    public GroceryListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
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
        FoodItemEntity currentFoodItem = mGroceryItems.get(position);

        holder.bindTo(currentFoodItem);
    }

    @Override
    public int getItemCount() {
        if (mGroceryItems == null) return 0;
        return mGroceryItems.size();
    }

    public void setGroceryItems(List<FoodItemEntity> groceryListItems) {
        this.mGroceryItems = groceryListItems;
    }

    /**
     * Contains a single item displayed in MainActivity <code>RecyclerView</code>.
     */
    class GroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView foodItemLabel;
        private final TextView foodItemBrand;
        private final TextView foodItemAmount;
        final GroceryListAdapter mAdapter;
        private ImageView mFoodImage;

        GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.grocerylist_food_image);
            mFoodImage.setClipToOutline(true);
            foodItemLabel = itemView.findViewById(R.id.grocery_item_label);
            foodItemBrand = itemView.findViewById(R.id.grocery_item_brand);
            foodItemAmount = itemView.findViewById(R.id.grocery_item_amount);
            this.mAdapter = adapter;

            itemView.setOnClickListener(this);
        }

        /**
         * Set values to all views inside a single item in MainActivity RecyclerView.
         *
         * @param currentFoodItem The MainActivity RecyclerView item that is to be displayed next.
         */
        void bindTo(FoodItemEntity currentFoodItem) {
            foodItemLabel.setText(currentFoodItem.getLabel());
            foodItemBrand.setText(currentFoodItem.getBrand());
            foodItemAmount.setText(String.valueOf(currentFoodItem.getAmount()));
            Glide.with(mContext).load(currentFoodItem.getImageUri()).into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItemEntity currentFoodItem = mGroceryItems.get(getAdapterPosition());

            Intent foodItemDetail = new Intent(mContext, FoodItemDetail.class);
            foodItemDetail.putExtra("label", currentFoodItem.getLabel());
            foodItemDetail.putExtra("image_resource", currentFoodItem.getImageUri());
            foodItemDetail.putExtra("brand", currentFoodItem.getBrand());
            foodItemDetail.putExtra("info", currentFoodItem.getInfo());
            foodItemDetail.putExtra("amount", currentFoodItem.getAmount());
            foodItemDetail.putExtra("unit", currentFoodItem.getUnit());
            mContext.startActivity(foodItemDetail);
        }
    }
}
