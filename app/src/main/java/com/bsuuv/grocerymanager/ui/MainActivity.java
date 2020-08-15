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
import com.bsuuv.grocerymanager.data.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.data.viewmodel.GroceryItemViewModel;
import com.bsuuv.grocerymanager.notifications.GroceryDayNotifier;
import com.bsuuv.grocerymanager.ui.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.util.DateHelper;
import com.bsuuv.grocerymanager.util.RecyclerViewUtil;
import com.bsuuv.grocerymanager.util.SharedPreferencesHelper;
import java.util.List;
import java.util.Objects;

/**
 * The entry point and opening view of the app. Displays the grocery list or a placeholder when it's
 * not grocery day. The grocery items can be deleted by swiping them left or right. Contains an
 * options menu for navigating to {@link SettingsActivity} and {@link ConfigurationsActivity} and an
 * app bar that collapses when browsing the grocery-items. On wide-screen devices the layout of this
 * activity is split into two panes, the other one showing the grocery list, the other showing
 * details of a selected grocery item in {@link GroceryItemDetailFragment}.
 * <p>
 * The grocery list is displayed in a <code>RecyclerView</code>, the {@link GroceryListAdapter} of
 * which receives its data from a {@link GroceryItemViewModel}.
 * <p>
 * When this activity is created, a notification is scheduled through {@link GroceryDayNotifier} to
 * notify user on the grocery day.
 *
 * @see SettingsActivity
 * @see ConfigurationsActivity
 * @see GroceryItemDetailFragment
 * @see GroceryListAdapter
 * @see GroceryItemViewModel
 */
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
  private SharedPreferencesHelper mSharedPrefsHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initMembers();
    configureUi();
    setUpViewModel();
    scheduleNotification();

    if (savedInstanceState != null) {
      recoverSavedInstanceState(savedInstanceState);
    }
  }

  private void initMembers() {
    this.mSharedPrefsHelper = new SharedPreferencesHelper(this);
    this.mDateHelper = new DateHelper(this, mSharedPrefsHelper);
    this.mRecyclerView = findViewById(R.id.main_recyclerview);
    this.mLayoutManager = new LinearLayoutManager(this);
    this.mAdapter = initAdapter();
    this.mRecyclerViewPlaceHolder =
        findViewById(R.id.main_recyclerview_placeholder);
    this.mNumberOfGroceryDays = mSharedPrefsHelper.getGroceryDays().size();
    this.mGroceryViewModel =
        new ViewModelProvider(this).get(GroceryItemViewModel.class);
  }

  private GroceryListAdapter initAdapter() {
    boolean mIsWideScreen = defineScreenSize();
    return new GroceryListAdapter(this, mIsWideScreen);
  }

  private boolean defineScreenSize() {
    // This layout is only available in w900dp/content_main, so it's
    // availability reveals the screen width
    return findViewById(R.id.container_food_item_detail) != null;
  }

  private void configureUi() {
    setUpToolbar();
    setUpRecyclerView();
  }

  private void setUpToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    setTitle(getString(R.string.mainActivity_actionbar_label) + " " + mDateHelper.getCurrentDate());
  }

  private void setUpRecyclerView() {
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    setRecyclerViewVisibility();
    ItemTouchHelper helper = initializeItemTouchHelper();
    helper.attachToRecyclerView(mRecyclerView);
  }

  private void setRecyclerViewVisibility() {
    if (mNumberOfGroceryDays == 0) {
      RecyclerViewUtil.toggleRecyclerViewVisibility(mRecyclerView, mRecyclerViewPlaceHolder
          , View.GONE, R.string.main_no_grocery_days_set);
    } else if (!mDateHelper.isGroceryDay()) {
      RecyclerViewUtil.toggleRecyclerViewVisibility(mRecyclerView, mRecyclerViewPlaceHolder
          , View.GONE, R.string.main_not_grocery_day);
    } else if (mDateHelper.isGroceryDay()) {
      RecyclerViewUtil.toggleRecyclerViewVisibility(mRecyclerView, mRecyclerViewPlaceHolder
          , View.VISIBLE, 0);
    }
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
        mGroceryViewModel.deleteFromGroceryList(mAdapter.getFoodItemAtPosition(swipedPosition));
      }
    });
  }

  private void setUpViewModel() {
    if (mDateHelper.isGroceryDay()) {
      mGroceryViewModel.getGroceryList().observe(this, groceryListItems -> {
        this.mGroceryList = groceryListItems;
        mAdapter.setGroceryItems(mGroceryList);
      });
    }
  }

  private void scheduleNotification() {
    GroceryDayNotifier mNotifier = new GroceryDayNotifier(this, mSharedPrefsHelper);
    int timeUntilGroceryDay = mDateHelper.timeUntilNextGroceryDay();
    if (timeUntilGroceryDay < DateHelper.NO_GROCERY_DAYS_SET) {
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
        SettingsActivity.class);
    this.startActivity(toSettingsActivity);
  }
}
