package com.bsuuv.grocerymanager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FoodItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);

        ImageView foodImage = findViewById(R.id.imageView_detail);
        Glide.with(this).load(getIntent()
                .getIntExtra("image_resource", 0)).into(foodImage);

        TextView title = findViewById(R.id.textview_title);
        TextView amount = findViewById(R.id.textview_amount);
        TextView info = findViewById(R.id.textview_info);

        title.setText(getIntent().getStringExtra("title"));
        amount.setText(getIntent().getStringExtra("amount"));
        info.setText(getIntent().getStringExtra("info"));
    }
}
