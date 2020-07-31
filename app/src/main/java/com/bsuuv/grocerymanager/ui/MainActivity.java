package com.bsuuv.grocerymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;

import java.util.Objects;

/**
 * Main activity of the app. Displays the grocery list and includes options menu to settings and
 * to configure groceries.
 */
public class MainActivity extends AppCompatActivity {

    private final static String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";

    private GroceryListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mRecyclerViewPlaceHolder;
    private LinearLayoutManager mLayoutManager;
    private GroceryItemViewModel mGroceryViewModel;
    private int mNumberOfGroceryDays;
    private DateHelper mDateHelper;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mDateHelper = new DateHelper(this);
        this.mRecyclerViewPlaceHolder =
                findViewById(R.id.main_recyclerview_placeholder);

        SharedPreferencesHelper sharedPrefsHelper =
                new SharedPreferencesHelper(this);
        this.mNumberOfGroceryDays = sharedPrefsHelper.getGroceryDays().size();

        setUpToolbar();

        // This layout is only available in w900dp/content_main, so it's
        // availability tells us the device orientation/screen width
        if (findViewById(R.id.container_food_item_detail) != null) {
            mTwoPane = true;
        }

        setUpRecyclerView();

        setUpViewModel();

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
        switch (item.getItemId()) {
            case R.id.action_configure:
                Intent toConfigs = new Intent(this, Configurations.class);
                this.startActivity(toConfigs);

                return true;
            case R.id.action_settings:
                Intent toSettings = new Intent(this, Settings.class);
                this.startActivity(toSettings);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpViewModel() {
        this.mGroceryViewModel = new ViewModelProvider(this).get(GroceryItemViewModel.class);
        mGroceryViewModel.getGroceryList().observe(this, groceryListItems -> {
            setRecyclerViewVisibility();
            mAdapter.setGroceryItems(groceryListItems);
        });
    }

    private void setRecyclerViewVisibility() {
        if (mDateHelper.isGroceryDay()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerViewPlaceHolder.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerViewPlaceHolder.setVisibility(View.VISIBLE);
            setPlaceholderText();
        }
    }

    private void setPlaceholderText() {
        if (mNumberOfGroceryDays == 0) {
            mRecyclerViewPlaceHolder.setText(R.string.main_no_grocery_days_set);
        } else {
            mRecyclerViewPlaceHolder.setText(R.string.main_not_grocery_day);
        }
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.main_recyclerview);
        this.mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.mAdapter = new GroceryListAdapter(this, mTwoPane);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.mainActivity_actionbar_label) + " " + mDateHelper.getCurrentDate());
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
                int swipedPosition = viewHolder.getAdapterPosition();
                mGroceryViewModel.check(mAdapter.getFoodItemAtPosition(swipedPosition));
            }
        });
    }
}
