package com.bsuuv.grocerymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.bsuuv.grocerymanager.ui.adapters.ConfigurationsListAdapter;
import com.bsuuv.grocerymanager.viewmodel.FoodItemViewModel;

import java.util.Objects;

/**
 * An activity containing a list of all food-items the user has configured
 * to show in his grocery list on a certain time. Gives options to create
 * new food-items and edit existing ones.
 */
public class Configurations extends AppCompatActivity {

    public static final int FOOD_ITEM_EDIT_REQUEST = 2;
    private static final int FOOD_ITEM_CREATE_REQUEST = 1;
    private ConfigurationsListAdapter mAdapter;
    private FoodItemViewModel mFoodItemViewModel;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
        setTitle("Configurations");

        setUpRecyclerView();

        setUpViewModel();

        // Manages dragging and swiping items in mRecyclerView
        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent fromNewFoodItem) {
        super.onActivityResult(requestCode, resultCode, fromNewFoodItem);

        // Result when NewFoodItem was launched to create a new food-item using the FAB.
        if (requestCode == FOOD_ITEM_CREATE_REQUEST && resultCode == RESULT_OK && fromNewFoodItem != null) {
            FoodItemEntity result = createFoodItemFromIntent(fromNewFoodItem);
            mFoodItemViewModel.insert(result);
            // Result when NewFoodItem was launched to edit a food-item by clicking one in the
            // RecyclerView.
        } else if (requestCode == FOOD_ITEM_EDIT_REQUEST) {
            if (resultCode == RESULT_OK && fromNewFoodItem != null) {
                FoodItemEntity result = updateFoodItemByIntent(fromNewFoodItem);
                mFoodItemViewModel.update(result);
            }
        }
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

    private FoodItemEntity updateFoodItemByIntent(Intent fromNewFoodItem) {
        String label = fromNewFoodItem.getStringExtra("label");
        String brand = fromNewFoodItem.getStringExtra("brand");
        int amount = fromNewFoodItem.getIntExtra("amount", 0);
        String unit = fromNewFoodItem.getStringExtra("unit");
        String info = fromNewFoodItem.getStringExtra("info");
        int timeFrame = fromNewFoodItem.getIntExtra("time_frame", 0);
        int frequency = fromNewFoodItem.getIntExtra("frequency", 0);
        String imageUri = fromNewFoodItem.getStringExtra("uri");
        int id = fromNewFoodItem.getIntExtra("id", 0);
        double countdownValue = fromNewFoodItem.getDoubleExtra("countdownValue", 0);

        return new FoodItemEntity(id, Objects.requireNonNull(label), brand, info, amount, unit,
                timeFrame, frequency, countdownValue, imageUri);
    }

    private FoodItemEntity createFoodItemFromIntent(Intent fromNewFoodItem) {
        String label = fromNewFoodItem.getStringExtra("label");
        String brand = fromNewFoodItem.getStringExtra("brand");
        int amount = fromNewFoodItem.getIntExtra("amount", 0);
        String unit = fromNewFoodItem.getStringExtra("unit");
        String info = fromNewFoodItem.getStringExtra("info");
        int timeFrame = fromNewFoodItem.getIntExtra("time_frame", 0);
        int frequency = fromNewFoodItem.getIntExtra("frequency", 0);
        String imageUri = fromNewFoodItem.getStringExtra("uri");

        return new FoodItemEntity(Objects.requireNonNull(label), brand, info, amount, unit,
                timeFrame, frequency, imageUri);
    }

    private void setUpRecyclerView() {
        this.mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.mAdapter = new ConfigurationsListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
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
                int deletedPosition = viewHolder.getAdapterPosition();
                mFoodItemViewModel.delete(mAdapter.getFoodItemAtPosition(deletedPosition));
            }
        });
    }

    private void setUpViewModel() {
        this.mFoodItemViewModel = new ViewModelProvider(this).get(FoodItemViewModel.class);
        mFoodItemViewModel.getFoodItems().observe(this,
                foodItemEntities -> {
                    mAdapter.setFoodItems(foodItemEntities);
                    mAdapter.notifyDataSetChanged();
                });
    }
}
