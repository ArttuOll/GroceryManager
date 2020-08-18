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
import com.bsuuv.grocerymanager.data.model.FoodItem;
import com.bsuuv.grocerymanager.ui.GroceryItemDetailActivity;
import com.bsuuv.grocerymanager.ui.GroceryItemDetailFragment;
import com.bsuuv.grocerymanager.ui.MainActivity;
import com.bsuuv.grocerymanager.ui.util.FoodItemListDifferenceCalculator;
import com.bsuuv.grocerymanager.ui.util.ImageViewPopulater;
import com.bsuuv.grocerymanager.ui.util.PluralsProvider;
import java.util.List;

/**
 * Adapter that feeds grocery items in the form of {@link GroceryViewHolder}s to the
 * <code>RecyclerView</code> in {@link MainActivity}.
 *
 * @see GroceryViewHolder
 * @see MainActivity
 */
public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

  private List<? extends FoodItem> mGroceryItems;
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
  public GroceryListAdapter.GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    View itemView = mInflater.inflate(R.layout.grocerylist_item, parent, false);
    return new GroceryViewHolder(itemView, this);
  }

  @Override
  public void onBindViewHolder(@NonNull GroceryListAdapter.GroceryViewHolder holder, int position) {
    FoodItem currentFoodItem = mGroceryItems.get(position);
    holder.bindTo(currentFoodItem);
  }

  @Override
  public int getItemCount() {
    return mGroceryItems == null ? 0 : mGroceryItems.size();
  }

  /**
   * Sets the list of grocery items that functions as a data source for this adapter. If already
   * set, calculates an optimal set of update operations to migrate from the old list to the new
   * one.
   *
   * @param newGroceryItems List of grocery items that should be displayed in the
   *                        <code>RecyclerView</code> in {@link MainActivity}
   * @see MainActivity
   */
  public void setGroceryItems(List<? extends FoodItem> newGroceryItems) {
    if (this.mGroceryItems == null) {
      initGroceryItems(newGroceryItems);
    } else {
      updateGroceryItems(newGroceryItems);
    }
  }

  private void initGroceryItems(List<? extends FoodItem> newGroceryItems) {
    this.mGroceryItems = newGroceryItems;
    notifyItemRangeInserted(0, newGroceryItems.size());
  }

  private void updateGroceryItems(List<? extends FoodItem> newGroceryItems) {
    DiffUtil.DiffResult migrationOperations = FoodItemListDifferenceCalculator
        .calculateMigrationOperations(mGroceryItems, newGroceryItems);
    mGroceryItems = newGroceryItems;
    migrationOperations.dispatchUpdatesTo(this);
  }

  public FoodItem getFoodItemAtPosition(int position) {
    return mGroceryItems.get(position);
  }

  /**
   * A <code>ViewHolder</code> containing one grocery item to be displayed in the
   * <code>RecyclerView</code> in {@link MainActivity}.
   *
   * @see MainActivity
   */
  class GroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    GroceryListAdapter mAdapter;
    private TextView mFoodItemLabel, mFoodItemBrand, mFoodItemAmount;
    private PluralsProvider mPluralsProvider;
    private ImageView mFoodImage;

    GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
      super(itemView);
      itemView.setOnClickListener(this);
      initMembers(adapter);
    }

    private void initMembers(GroceryListAdapter adapter) {
      this.mFoodImage = itemView.findViewById(R.id.grocerylist_food_image);
      this.mFoodImage.setClipToOutline(true);
      this.mFoodItemLabel = itemView.findViewById(R.id.grocery_item_label);
      this.mFoodItemBrand = itemView.findViewById(R.id.grocery_item_brand);
      this.mFoodItemAmount = itemView.findViewById(R.id.grocery_item_amount);
      this.mAdapter = adapter;
      this.mPluralsProvider = new PluralsProvider(mContext);
    }

    void bindTo(FoodItem currentFoodItem) {
      setInputFieldValues(currentFoodItem);
      String uri = currentFoodItem.getImageUri() == null ? "" : currentFoodItem.getImageUri();
      ImageViewPopulater.populateFromUri(mContext, uri, mFoodImage);
    }

    private void setInputFieldValues(FoodItem currentFoodItem) {
      mFoodItemLabel.setText(currentFoodItem.getLabel());
      mFoodItemBrand.setText(currentFoodItem.getBrand());
      mFoodItemAmount.setText(mPluralsProvider.getAmountString(currentFoodItem.getAmount(),
          currentFoodItem.getUnit()));
    }

    /**
     * Called when an item in the <code>RecyclerView</code> in {@link MainActivity} is clicked.
     * Checks if the device screen is wide (>900 dp) and based on that launches {@link
     * GroceryItemDetailFragment} either in {@link MainActivity} or {@link
     * GroceryItemDetailActivity}.
     *
     * @param v Default parameter from the parent method. The <code>View</code> that was clicked.
     * @see MainActivity
     * @see GroceryItemDetailFragment
     * @see GroceryItemDetailActivity
     */
    @Override
    public void onClick(View v) {
      FoodItem currentFoodItem = mGroceryItems.get(getAdapterPosition());
      if (mIsWideScreen) {
        showInMainActivity(currentFoodItem);
      } else {
        showInFoodItemDetailActivity(currentFoodItem);
      }
    }

    private void showInMainActivity(FoodItem currentFoodItem) {
      GroceryItemDetailFragment fragment = GroceryItemDetailFragment
          .newInstance(currentFoodItem.getId());

      ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
          .replace(R.id.container_food_item_detail, fragment)
          .addToBackStack(null)
          .commit();
    }

    private void showInFoodItemDetailActivity(FoodItem currentFoodItem) {
      Intent toFoodItemDetail = createIntentToFoodItemDetail(currentFoodItem);
      Bundle bundle = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext,
          mFoodImage, mFoodImage.getTransitionName()).toBundle();
      mContext.startActivity(toFoodItemDetail, bundle);
    }

    private Intent createIntentToFoodItemDetail(FoodItem foodItem) {
      Intent toFoodItemDetail = new Intent(mContext, GroceryItemDetailActivity.class);
      toFoodItemDetail.putExtra(GroceryItemDetailFragment.FOOD_ITEM_ID_KEY, foodItem.getId());
      return toFoodItemDetail;
    }
  }
}
