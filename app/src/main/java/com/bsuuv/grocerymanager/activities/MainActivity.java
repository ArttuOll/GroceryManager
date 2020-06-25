package com.bsuuv.grocerymanager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.activities.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bsuuv.grocerymanager.logic.FoodScheduler;
import com.bsuuv.grocerymanager.logic.SharedPreferencesHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Main activity of the app. Displays the grocery list and includes options menu to settings and to configure groceries.
 */
public class MainActivity extends AppCompatActivity {

    private final static String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";
    private final Context mContext = this;
    private GroceryListAdapter mAdapter;
    private List<FoodItem> mFoodItems;
    private List<FoodItem> mGroceryList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferencesHelper sharedPrefsHelper = new SharedPreferencesHelper(PreferenceManager.
                getDefaultSharedPreferences(this));

        this.mFoodItems = sharedPrefsHelper.getFoodItems();

        FoodScheduler scheduler = new FoodScheduler(sharedPrefsHelper);
        this.mGroceryList = scheduler.getGroceryList();

        setUpToolbar();

        setUpRecyclerView();

        // Manages dragging and swiping items in mRecyclerView
        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable state = Objects.requireNonNull(mRecyclerView.getLayoutManager())
                .onSaveInstanceState();
        outState.putParcelable(MAIN_RECYCLERVIEW_STATE, state);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_configure) {
            Intent toConfigs = new Intent(this, Configurations.class);
            this.startActivity(toConfigs);

            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            Intent toSettings = new Intent(this, Settings.class);
            this.startActivity(toSettings);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.main_recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new GroceryListAdapter(this, mGroceryList);
        this.mRecyclerView.setAdapter(mAdapter);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.mainActivity_actionbar_label) + " " + getCurrentDate());
    }

    private String getCurrentDate() {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(Calendar.getInstance().getTime());
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
