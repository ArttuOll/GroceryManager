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
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.notifications.GroceryDayNotifier;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";

    private RecyclerView mRecyclerView;
    private GroceryListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView mRecyclerViewPlaceHolder;
    private GroceryItemViewModel mGroceryViewModel;
    private DateHelper mDateHelper;
    private List<FoodItemEntity> mGroceryList;
    private int mNumberOfGroceryDays;
    private boolean mIsWideScreen;
    private SharedPreferencesHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMembers();
        configureUi();
        setUpViewModel();
        scheduleNotification();

        if (savedInstanceState != null) recoverSavedInstanceState(savedInstanceState);
    }

    private void initMembers() {
        this.mSharedPrefsHelper = new SharedPreferencesHelper(this);
        this.mDateHelper = new DateHelper(this, mSharedPrefsHelper);
        this.mRecyclerView = findViewById(R.id.main_recyclerview);
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mAdapter = new GroceryListAdapter(this, mIsWideScreen);
        this.mRecyclerViewPlaceHolder =
                findViewById(R.id.main_recyclerview_placeholder);
        this.mNumberOfGroceryDays = mSharedPrefsHelper.getGroceryDays().size();
        this.mGroceryViewModel =
                new ViewModelProvider(this).get(GroceryItemViewModel.class);
    }

    private void configureUi() {
        setUpToolbar();
        defineLayoutForScreenSize();
        setUpRecyclerView();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.mainActivity_actionbar_label) + " "
                + mDateHelper.getCurrentDate());
    }

    private void defineLayoutForScreenSize() {
        // This layout is only available in w900dp/content_main, so it's
        // availability reveals the screen width
        if (findViewById(R.id.container_food_item_detail) != null) mIsWideScreen = true;
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);

        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);
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

    private void setUpViewModel() {
        mGroceryViewModel.getGroceryList().observe(this, groceryListItems -> {
            this.mGroceryList = groceryListItems;
            setRecyclerViewVisibility();
            mAdapter.setGroceryItems(mGroceryList);
        });
    }

    private void setRecyclerViewVisibility() {
        if (mNumberOfGroceryDays == 0) {
            toggleRecyclerViewVisibility(View.GONE, R.string.main_no_grocery_days_set);
        } else if (!mDateHelper.isGroceryDay()) {
            toggleRecyclerViewVisibility(View.GONE, R.string.main_not_grocery_day);
        } else if (mDateHelper.isGroceryDay()) {
            toggleRecyclerViewVisibility(View.VISIBLE, 0);
        }
    }

    private void toggleRecyclerViewVisibility(int visibility, int placeholderStrResourceId) {
        if (visibility == View.VISIBLE) {
            mRecyclerView.setVisibility(visibility);
            mRecyclerViewPlaceHolder.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerViewPlaceHolder.setVisibility(View.VISIBLE);
            mRecyclerViewPlaceHolder.setText(placeholderStrResourceId);
        }
    }

    private void scheduleNotification() {
        GroceryDayNotifier mNotifier = new GroceryDayNotifier(this, mSharedPrefsHelper);
        int timeUntilGroceryDay = mDateHelper.timeUntilNextGroceryDay();
        if (timeUntilGroceryDay < 8) {
            mNotifier.scheduleGroceryDayNotification(timeUntilGroceryDay);
        }
    }

    private void recoverSavedInstanceState(Bundle savedInstanceState) {
        Parcelable state = savedInstanceState.getParcelable(MAIN_RECYCLERVIEW_STATE);
        mLayoutManager.onRestoreInstanceState(state);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        saveRecyclerViewState(outState);
        super.onSaveInstanceState(outState);
    }

    private void saveRecyclerViewState(Bundle outState) {
        Parcelable state = Objects.requireNonNull(mRecyclerView.getLayoutManager())
                .onSaveInstanceState();
        outState.putParcelable(MAIN_RECYCLERVIEW_STATE, state);
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
                launchConfigurationsActivity();
                return true;
            case R.id.action_settings:
                launchSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchConfigurationsActivity() {
        Intent toConfigurationsActivity = new Intent(this,
                ConfigurationsActivity.class);
        this.startActivity(toConfigurationsActivity);
    }

    private void launchSettingsActivity() {
        Intent toSettingsActivity = new Intent(this,
                Settings.class);
        this.startActivity(toSettingsActivity);
    }
}
