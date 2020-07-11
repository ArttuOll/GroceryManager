package com.bsuuv.grocerymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.FoodScheduler;
import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.SharedPreferencesHelper;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.adapters.ConfigurationsListAdapter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * An activity containing a list of all food-items the user has configured
 * to show in his grocery list on a certain time. Gives options to create
 * new food-items and edit existing ones.
 */
public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_EDIT_REQUEST = 2;
    private static final int FOOD_ITEM_CREATE_REQUEST = 1;
    private List<FoodItemEntity> mFoodItems;
    private ConfigurationsListAdapter mAdapter;
    private SharedPreferencesHelper mSharedPrefsHelper;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
        setTitle("Configurations");

        this.mSharedPrefsHelper = new SharedPreferencesHelper(PreferenceManager.
                getDefaultSharedPreferences(this));
        this.mFoodItems = mSharedPrefsHelper.getFoodItems();

        setUpRecyclerView();

        // Manages dragging and swiping items in mRecyclerView
        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result when NewFoodItem was launched to create a new food-item using the FAB.
        if (requestCode == FOOD_ITEM_CREATE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FoodItemEntity result = createFoodItemFromIntent(data);

                    // Position in mRecyclerView, where the new food-item is inserted.
                    int insertionPosition = getFoodItemInsertionPosition(result.getTimeFrame());

                    mFoodItems.add(result);
                    Collections.sort(mFoodItems, (foodItem1, foodItem2) ->
                            foodItem1.getTimeFrame() - foodItem2.getTimeFrame());

                    mAdapter.notifyItemInserted(insertionPosition);
                }
            }
            // Result when NewFoodItem was launched to edit a food-item by clicking one in the
            // RecyclerView.
        } else if (requestCode == FOOD_ITEM_EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FoodItemEntity result = modifyFoodItemByIntent(data);

                    int editedPosition = data.getIntExtra("editPosition", -1);

                    mFoodItems.set(editedPosition, result);

                    mAdapter.notifyItemChanged(editedPosition);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPrefsHelper.saveFoodItems(mFoodItems);
    }

    /**
     * Called when the floating action button in this activity is pressed. Launches
     * <code>NewFoodItemActivity</code> for creating a new <code>FoodItem</code>.
     *
     * @param view The view that has been clicked, in this case, the FAB.
     *             Default parameter required by the system.
     */
    public void onFabClick(View view) {
        Intent toNewFoodItem = new Intent(this, NewFoodItem.class);
        startActivityForResult(toNewFoodItem, FOOD_ITEM_CREATE_REQUEST);
    }

    // TODO: modify to use int id.
    private FoodItemEntity modifyFoodItemByIntent(Intent data) {
        String label = data.getStringExtra("label");
        String brand = data.getStringExtra("brand");
        int amount = data.getIntExtra("amount", 0);
        String unit = data.getStringExtra("unit");
        String info = data.getStringExtra("info");
        int timeFrame = data.getIntExtra("time_frame", 0);
        int frequency = data.getIntExtra("frequency", 0);
        String imageUri = data.getStringExtra("uri");
        UUID id = (UUID) data.getSerializableExtra("id");

        return new FoodItemEntity(label, brand, info, amount, unit, timeFrame, frequency,
                imageUri, id);
    }

    // TODO: modify to use int id.
    private FoodItemEntity createFoodItemFromIntent(Intent data) {
        String label = data.getStringExtra("label");
        String brand = data.getStringExtra("brand");
        int amount = data.getIntExtra("amount", 0);
        String unit = data.getStringExtra("unit");
        String info = data.getStringExtra("info");
        int timeFrame = data.getIntExtra("time_frame", 0);
        int frequency = data.getIntExtra("frequency", 0);
        String imageUri = data.getStringExtra("uri");

        return new FoodItemEntity(label, brand, info, amount, unit, timeFrame, frequency, imageUri);
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigurationsListAdapter(this, mFoodItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int getFoodItemInsertionPosition(int frequency) {
        int weeks = 0;
        int twoWeeks = 0;
        int months = 0;

        for (FoodItemEntity foodItem : mFoodItems) {
            switch (foodItem.getTimeFrame()) {
                case FoodScheduler.TimeFrame.WEEK:
                    weeks++;
                    break;
                case FoodScheduler.TimeFrame.TWO_WEEKS:
                    twoWeeks++;
                    break;
                case FoodScheduler.TimeFrame.MONTH:
                    months++;
                    break;
            }
        }

        // When a food item is inserted into the RecyclerView, it is added after all the other
        // food-items with the same frequency.
        switch (frequency) {
            case FoodScheduler.TimeFrame.WEEK:
                return weeks;
            case FoodScheduler.TimeFrame.TWO_WEEKS:
                return weeks + twoWeeks;
            case FoodScheduler.TimeFrame.MONTH:
                return weeks + twoWeeks + months;
            default:
                return 0;
        }
    }

    private ItemTouchHelper initializeItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mFoodItems.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
    }
}
