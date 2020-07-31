package com.bsuuv.grocerymanager.ui;

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
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.util.PluralsProvider;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class FoodItemDetailFragment extends Fragment {

    public static String FOOD_ITEM_ID_KEY = "foodItemId";
    private PluralsProvider mPluralsProvider;
    private FoodItemEntity mFoodItem;

    public FoodItemDetailFragment() {}

    public static FoodItemDetailFragment newInstance(int foodItemId) {
        FoodItemDetailFragment foodItemDetailFragment =
                new FoodItemDetailFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(FOOD_ITEM_ID_KEY, foodItemId);

        foodItemDetailFragment.setArguments(arguments);

        return foodItemDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPluralsProvider = new PluralsProvider(getActivity());

        GroceryItemViewModel viewModel =
                new GroceryItemViewModel(requireActivity().getApplication());
        Bundle arguments = requireArguments();
        if (arguments.containsKey(FOOD_ITEM_ID_KEY)) {
            int foodItemId = arguments.getInt(FOOD_ITEM_ID_KEY);
            mFoodItem = viewModel.get(foodItemId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.food_item_detail, container
                , false);

        setUpImageView(rootView);

        setUpTextViews(rootView);

        return rootView;
    }

    private void setUpImageView(View rootView) {
        ImageView foodImage = rootView.findViewById(R.id.imageView_detail);

        String imageUri = mFoodItem.getImageUri();
        Glide.with(this).load(imageUri).into(foodImage);
    }

    private void setUpTextViews(View rootView) {
        TextView label = rootView.findViewById(R.id.textview_title);
        TextView amount = rootView.findViewById(R.id.textview_amount);
        TextView brand = rootView.findViewById(R.id.textview_brand);
        TextView info = rootView.findViewById(R.id.textview_info);

        label.setText(mFoodItem.getLabel());

        int amountValue = mFoodItem.getAmount();
        String unit = mFoodItem.getUnit();
        amount.setText(mPluralsProvider.getAmountString(amountValue,
                Objects.requireNonNull(unit)));

        brand.setText(mFoodItem.getBrand());
        info.setText(mFoodItem.getInfo());
    }
}
