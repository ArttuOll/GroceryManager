package com.bsuuv.grocerymanager.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
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
import com.bsuuv.grocerymanager.notifications.GroceryDayReceiver;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.viewmodel.GroceryItemViewModel;

import java.util.List;
import java.util.Objects;

/**
 * Main activity of the app. Displays the grocery list and includes options
 * menu to settings and
 * to configure groceries.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    public static final int NOTIFICATION_ID = 0;

    private static final String MAIN_RECYCLERVIEW_STATE = "recyclerView_state";

    private GroceryListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mRecyclerViewPlaceHolder;
    private LinearLayoutManager mLayoutManager;
    private GroceryItemViewModel mGroceryViewModel;
    private int mNumberOfGroceryDays;
    private DateHelper mDateHelper;
    private boolean mTwoPane;
    private List<FoodItemEntity> mGroceryList;
    private SharedPreferencesHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSharedPrefsHelper =
                new SharedPreferencesHelper(this);

        this.mDateHelper = new DateHelper(this, mSharedPrefsHelper);
        this.mRecyclerViewPlaceHolder =
                findViewById(R.id.main_recyclerview_placeholder);

        this.mNumberOfGroceryDays = mSharedPrefsHelper.getGroceryDays().size();

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
            Parcelable state =
                    savedInstanceState.getParcelable(MAIN_RECYCLERVIEW_STATE);
            mLayoutManager.onRestoreInstanceState(state);
        }

        createNotificationChannel();

        scheduleGroceryDayNotification();
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

    public void createNotificationChannel() {
        NotificationManager mNotifManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel primaryChannel =
                    new NotificationChannel(PRIMARY_CHANNEL_ID,
                            getString(R.string.notifchan_primary_name),
                            NotificationManager.IMPORTANCE_DEFAULT);

            primaryChannel.enableLights(true);
            primaryChannel.setLightColor(Color.GREEN);
            primaryChannel.enableVibration(true);
            primaryChannel.setDescription(getString(R.string.notifchan_primary_description));
            Objects.requireNonNull(mNotifManager).createNotificationChannel(primaryChannel);
        }
    }

    private void scheduleGroceryDayNotification() {
        Intent notifIntent = new Intent(this, GroceryDayReceiver.class);
        final PendingIntent notifPendingIntent =
                PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, notifIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        SharedPreferences.OnSharedPreferenceChangeListener groceryDaysListener =
                (sharedPreferences, key) -> {
                    if (key.equals(SharedPreferencesHelper.GROCERY_DAYS_KEY)) {
                        long repeatInterval =
                                AlarmManager.INTERVAL_DAY * mDateHelper.timeUntilNextGroceryDay();
                        long triggerTime =
                                SystemClock.elapsedRealtime() + repeatInterval;

                        if (alarmManager != null) {
                            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval,
                                    notifPendingIntent);
                        }
                    }
                };

        SharedPreferences sharedPreferences =
                mSharedPrefsHelper.getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(groceryDaysListener);
    }

    private void setUpViewModel() {
        this.mGroceryViewModel =
                new ViewModelProvider(this).get(GroceryItemViewModel.class);
        mGroceryViewModel.getGroceryList().observe(this, groceryListItems -> {
            this.mGroceryList = groceryListItems;
            setRecyclerViewVisibility();
            mAdapter.setGroceryItems(mGroceryList);
        });
    }

    private void setRecyclerViewVisibility() {
        if (mNumberOfGroceryDays == 0) {
            setRecyclerViewAndPlaceholder(View.GONE,
                    R.string.main_no_grocery_days_set);
        } else if (!mDateHelper.isGroceryDay()) {
            setRecyclerViewAndPlaceholder(View.GONE,
                    R.string.main_not_grocery_day);
        } else if (mDateHelper.isGroceryDay()) {
            setRecyclerViewAndPlaceholder(View.VISIBLE, 0);
        }
    }

    private void setRecyclerViewAndPlaceholder(int recyclerViewVisibility,
                                               int placeholderTextResId) {
        if (recyclerViewVisibility == View.VISIBLE) {
            mRecyclerView.setVisibility(recyclerViewVisibility);
            mRecyclerViewPlaceHolder.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerViewPlaceHolder.setVisibility(View.VISIBLE);
            mRecyclerViewPlaceHolder.setText(placeholderTextResId);
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
