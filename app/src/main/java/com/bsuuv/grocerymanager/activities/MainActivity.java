package com.bsuuv.grocerymanager.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";
    private RecyclerView mRecyclerView;
    private GroceryListAdapter mAdapter;
    private List<FoodItem> mFoodItems;
    private Context mContext = this;

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState
                    .getParcelable(MAIN_RECYCLERVIEW_STATE));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        this.mFoodItems = new LinkedList<>();

        setUpRecyclerView();

        // generateTestData();

        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(MAIN_RECYCLERVIEW_STATE, state);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_configure) {
            Intent configs = new Intent(this, Configurations.class);
            this.startActivity(configs);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.main_recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new GroceryListAdapter(this, mFoodItems);
        this.mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.mainActivity_actionbar_label) + " " + getCurrentDate());
    }

    private void generateTestData() {
        TypedArray foodImageResources = getResources().obtainTypedArray(R.array.food_images);
        String[] foodLabels = getResources().getStringArray(R.array.food_labels);
        String[] foodBrands = getResources().getStringArray(R.array.food_brands);
        String[] foodInfos = getResources().getStringArray(R.array.food_infos);
        String[] foodWeights = getResources().getStringArray(R.array.food_weights);
        String[] amounts = getResources().getStringArray(R.array.food_amounts);

        for (int i = 0; i < foodLabels.length; i++) {
            mFoodItems.add(new FoodItem(foodLabels[i], foodBrands[i], foodInfos[i], foodWeights[i],
                    amounts[i], foodImageResources.getResourceId(i, 0)));
        }

        foodImageResources.recycle();
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(calendar.getTime());
    }

    private ItemTouchHelper initializeItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
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
    }
}
