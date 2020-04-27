package com.bsuuv.grocerymanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GroceryListAdapter mAdapter;
    private List<FoodItem> mFoodItems;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Groceries for " + getCurrentDate());


        this.mFoodItems = new LinkedList<>();

        this.mRecyclerView = findViewById(R.id.main_recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new GroceryListAdapter(this, mFoodItems);
        this.mRecyclerView.setAdapter(mAdapter);

        generateTestData();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                Collections.swap(mFoodItems, from, to);

                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mFoodItems.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast toast = Toast.makeText(mContext, R.string.toast_checked, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onGrocerySettingsClicked(MenuItem item) {
        Intent intent = new Intent(this, GrocerySettingsActivity.class);
        startActivity(intent);
    }

    private void generateTestData() {
        TypedArray foodImageResources = getResources().obtainTypedArray(R.array.food_images);
        String[] foodLabels = getResources().getStringArray(R.array.food_labels);
        String[] foodInfos = getResources().getStringArray(R.array.food_infos);
        String[] foodWeights = getResources().getStringArray(R.array.food_weights);
        String[] amounts = getResources().getStringArray(R.array.food_amounts);

        for (int i = 0; i < foodLabels.length; i++) {
            mFoodItems.add(new FoodItem(foodLabels[i], foodInfos[i], foodWeights[i], amounts[i], foodImageResources.getResourceId(i, 0)));
        }

        foodImageResources.recycle();
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat format = SimpleDateFormat.getDateInstance();
        String date = format.format(calendar.getTime());

        return date;
    }
}
