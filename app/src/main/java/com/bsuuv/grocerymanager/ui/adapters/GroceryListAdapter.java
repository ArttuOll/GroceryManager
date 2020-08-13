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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.FoodItemDetail;
import com.bsuuv.grocerymanager.ui.FoodItemDetailFragment;
import com.bsuuv.grocerymanager.util.FoodItemListDifferenceCalculator;
import com.bsuuv.grocerymanager.util.PluralsProvider;
import com.bumptech.glide.Glide;

import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private List<FoodItemEntity> mGroceryItems;
    private LayoutInflater mInflater;
    private Context mContext;
    private boolean mIsWideScreen;

    public GroceryListAdapter(Context context, boolean wideScreen) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mIsWideScreen = wideScreen;
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
        return mGroceryItems == null ? 0 : mGroceryItems.size();
    }

    public void setGroceryItems(List<FoodItemEntity> newGroceryItems) {
        if (this.mGroceryItems == null) initGroceryItems(newGroceryItems);
        else updateGroceryItems(newGroceryItems);
    }

    private void initGroceryItems(List<FoodItemEntity> newGroceryItems) {
        this.mGroceryItems = newGroceryItems;
        notifyItemRangeInserted(0, newGroceryItems.size());
    }

    private void updateGroceryItems(List<FoodItemEntity> newGroceryItems) {
        DiffUtil.DiffResult migrationOperations = FoodItemListDifferenceCalculator
                .calculateMigrationOperations(mGroceryItems, newGroceryItems);
        mGroceryItems = newGroceryItems;
        migrationOperations.dispatchUpdatesTo(this);
    }

    public FoodItemEntity getFoodItemAtPosition(int position) {
        return mGroceryItems.get(position);
    }

    class GroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mFoodItemLabel, mFoodItemBrand, mFoodItemAmount;
        final GroceryListAdapter mAdapter;
        private final PluralsProvider mPluralsProvider;
        private ImageView mFoodImage;

        GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.grocerylist_food_image);
            mFoodImage.setClipToOutline(true);
            mFoodItemLabel = itemView.findViewById(R.id.grocery_item_label);
            mFoodItemBrand = itemView.findViewById(R.id.grocery_item_brand);
            mFoodItemAmount = itemView.findViewById(R.id.grocery_item_amount);
            this.mAdapter = adapter;
            this.mPluralsProvider = new PluralsProvider(mContext);
            itemView.setOnClickListener(this);
        }

        void bindTo(FoodItemEntity currentFoodItem) {
            mFoodItemLabel.setText(currentFoodItem.getLabel());
            mFoodItemBrand.setText(currentFoodItem.getBrand());
            mFoodItemAmount.setText(mPluralsProvider.getAmountString(currentFoodItem.getAmount(),
                    currentFoodItem.getUnit()));
            Glide.with(mContext).load(currentFoodItem.getImageUri()).into(mFoodImage);
        }

        @Override
        public void onClick(View v) {
            FoodItemEntity currentFoodItem = mGroceryItems.get(getAdapterPosition());
            if (mIsWideScreen) {
                showInFragment(currentFoodItem);
            } else {
                showInFoodItemDetailActivity(currentFoodItem);
            }
        }

        private void showInFragment(FoodItemEntity currentFoodItem) {
            FoodItemDetailFragment fragment = FoodItemDetailFragment
                    .newInstance(currentFoodItem.getId());

            ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_food_item_detail, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        private void showInFoodItemDetailActivity(FoodItemEntity currentFoodItem) {
            Intent toFoodItemDetail = createIntentToFoodItemDetail(currentFoodItem);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
                    mFoodImage, mFoodImage.getTransitionName()).toBundle();
            mContext.startActivity(toFoodItemDetail, bundle);
        }

        private Intent createIntentToFoodItemDetail(FoodItemEntity foodItem) {
            Intent toFoodItemDetail = new Intent(mContext, FoodItemDetail.class);
            toFoodItemDetail.putExtra(FoodItemDetailFragment.FOOD_ITEM_ID_KEY, foodItem.getId());
            return toFoodItemDetail;
        }
    }
}
