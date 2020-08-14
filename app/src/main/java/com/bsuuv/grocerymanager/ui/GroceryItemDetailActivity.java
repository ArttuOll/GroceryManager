package com.bsuuv.grocerymanager.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bsuuv.grocerymanager.R;

/**
 * Activity for viewing the details of a grocery-item. Delegates the details of displaying a
 * grocery-item to {@link GroceryItemDetailFragment}. This is done to avoid code duplication, since
 * {@link GroceryItemDetailFragment} is also used to display grocery-item details in two-pane views
 * on large-screen devices (see {@link MainActivity}.
 *
 * @see GroceryItemDetailFragment
 * @see MainActivity
 */
public class GroceryItemDetailActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_item_detail);
    if (savedInstanceState == null) {
      int foodItemId = getIntent().getIntExtra(GroceryItemDetailFragment.FOOD_ITEM_ID_KEY, 0);
      launchFoodItemDetailFragment(foodItemId);
    }
  }

  private void launchFoodItemDetailFragment(int foodItemId) {
    GroceryItemDetailFragment fragment =
        GroceryItemDetailFragment.newInstance(foodItemId);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.container_food_item_detail, fragment)
        .commit();
  }
}
