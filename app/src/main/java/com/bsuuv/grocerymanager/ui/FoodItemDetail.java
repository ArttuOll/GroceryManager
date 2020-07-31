package com.bsuuv.grocerymanager.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.util.PluralsProvider;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class FoodItemDetail extends AppCompatActivity {

    private PluralsProvider mPluralsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);
        this.mPluralsProvider = new PluralsProvider(this);


        if (savedInstanceState == null) {
            int selectedFoodItem =
                    getIntent().getIntExtra(FoodItemDetailFragment.FOOD_ITEM_ID_KEY, 0);

            FoodItemDetailFragment fragment =
                    FoodItemDetailFragment.newInstance(selectedFoodItem);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_food_item_detail, fragment)
                    .commit();
        }
    }

    private void setUpImageView() {
        ImageView foodImage = findViewById(R.id.imageView_detail);

        String imageUri = getIntent().getStringExtra("image_resource");
        Glide.with(this).load(imageUri).into(foodImage);
    }

    private void setUpTextViews() {
        TextView title = findViewById(R.id.textview_title);
        TextView amount = findViewById(R.id.textview_amount);
        TextView brand = findViewById(R.id.textview_brand);
        TextView info = findViewById(R.id.textview_info);

        title.setText(getIntent().getStringExtra("label"));

        int amountValue = getIntent().getIntExtra("amount", 0);
        String unit = getIntent().getStringExtra("unit");
        amount.setText(mPluralsProvider.getAmountString(amountValue, Objects.requireNonNull(unit)));

        brand.setText(getIntent().getStringExtra("brand"));
        info.setText(getIntent().getStringExtra("info"));
    }
}
