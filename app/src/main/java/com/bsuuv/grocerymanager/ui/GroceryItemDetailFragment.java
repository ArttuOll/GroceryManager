package com.bsuuv.grocerymanager.ui;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.data.viewmodel.GroceryItemViewModel;
import com.bsuuv.grocerymanager.util.ImageViewPopulater;
import com.bsuuv.grocerymanager.util.PluralsProvider;

public class GroceryItemDetailFragment extends Fragment {

    public static final String FOOD_ITEM_ID_KEY = "foodItemId";

    private PluralsProvider mPluralsProvider;
    private FoodItemEntity mFoodItem;

    public GroceryItemDetailFragment() {
    } // Required

    public static GroceryItemDetailFragment newInstance(int foodItemId) {
        GroceryItemDetailFragment fragment = new GroceryItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt(FOOD_ITEM_ID_KEY, foodItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMembers();
    }

    private void initMembers() {
        this.mPluralsProvider = new PluralsProvider(getActivity());
        this.mFoodItem = getFoodItem();
    }

    private FoodItemEntity getFoodItem() {
        GroceryItemViewModel viewModel = getViewModel();
        Bundle fragmentArgs = requireArguments();
        int foodItemId = fragmentArgs.getInt(FOOD_ITEM_ID_KEY);
        return viewModel.get(foodItemId);
    }

    private GroceryItemViewModel getViewModel() {
        Application parentActivityOwner = requireActivity().getApplication();
        return new GroceryItemViewModel(parentActivityOwner);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.food_item_detail, container, false);
        setUpImageView(rootView);
        setUpTextViews(rootView);
        return rootView;
    }

    private void setUpImageView(View rootView) {
        ImageView foodImage = rootView.findViewById(R.id.imageView_detail);
        String uri = mFoodItem.getImageUri() == null ? "" : mFoodItem.getImageUri();
        ImageViewPopulater.populateFromUri(getContext(), uri, foodImage);
    }

    private void setUpTextViews(View rootView) {
        setUpLabelTextView(rootView);
        setUpAmountTextView(rootView);
        setUpBrandTextView(rootView);
        setUpInfoTextView(rootView);
    }

    private void setUpLabelTextView(View rootView) {
        TextView label = rootView.findViewById(R.id.textview_title);
        label.setText(mFoodItem.getLabel());
    }

    private void setUpAmountTextView(View rootView) {
        TextView amountView = rootView.findViewById(R.id.textview_amount);
        int amount = mFoodItem.getAmount();
        String unit = mFoodItem.getUnit();
        amountView.setText(mPluralsProvider.getAmountString(amount, unit));
    }

    private void setUpBrandTextView(View rootView) {
        TextView brand = rootView.findViewById(R.id.textview_brand);
        brand.setText(mFoodItem.getBrand());
    }

    private void setUpInfoTextView(View rootView) {
        TextView info = rootView.findViewById(R.id.textview_info);
        info.setText(mFoodItem.getInfo());
    }
}
