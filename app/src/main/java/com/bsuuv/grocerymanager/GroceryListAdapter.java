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

import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private List<FoodItem> mFoodItems;
    private LayoutInflater mInflator;
    private Context mContext;

    public GroceryListAdapter(Context context, List<FoodItem> foodItems) {
        this.mInflator = LayoutInflater.from(context);
        this.mFoodItems = foodItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public GroceryListAdapter.GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.cardlist_item, parent, false);

        return new GroceryViewHolder(mItemView, this);
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

    class GroceryViewHolder extends RecyclerView.ViewHolder {
        public final TextView foodItemLabel;
        private ImageView mFoodImage;
        final GroceryListAdapter mAdapter;

        public GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.food_image);
            foodItemLabel = itemView.findViewById(R.id.grocery_item_label);
            this.mAdapter = adapter;
        }

        void bindTo(FoodItem currentFoodItem) {
            foodItemLabel.setText(currentFoodItem.getmTitle());
            Glide.with(mContext).load(currentFoodItem.getImageResource()).into(mFoodImage);
        }
    }
}
