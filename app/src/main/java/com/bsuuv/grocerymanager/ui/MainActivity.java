package com.bsuuv.grocerymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.util.GroceryDayInspector;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

/**
 * Main activity of the app. Displays the grocery list and includes options menu to settings and
 * to configure groceries.
 */
public class MainActivity extends AppCompatActivity {

    private final static String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";

    private GroceryListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GroceryItemViewModel mGroceryViewModel;
    private int mNumberOfGroceryDays;
    private GroceryDayInspector mInspector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();

        setUpRecyclerView();
        this.mRecyclerViewPlaceHolder = findViewById(R.id.main_recyclerview_placeholder);

        SharedPreferencesHelper sharedPrefsHelper = new SharedPreferencesHelper(this);
        this.mNumberOfGroceryDays = sharedPrefsHelper.getGroceryDays().size();
        this.mInspector = new GroceryDayInspector(this);

        this.mGroceryViewModel = new ViewModelProvider(this).get(GroceryItemViewModel.class);
        mGroceryViewModel.getGroceryList().observe(this, groceryListItems -> {
            mAdapter.setGroceryItems(groceryListItems);
            mAdapter.notifyDataSetChanged();
        });

        // Manages dragging and swiping items in mRecyclerView
        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);

        // Restore activity state after configuration change
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(MAIN_RECYCLERVIEW_STATE);
            mLayoutManager.onRestoreInstanceState(state);
        }
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
        this.mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new GroceryListAdapter(this);
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
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int deletedPosition = viewHolder.getAdapterPosition();
                mGroceryViewModel.delete(mAdapter.getFoodItemAtPosition(deletedPosition));
            }
        });
    }
}
