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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.activities.adapters.GroceryListAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;
import com.bsuuv.grocerymanager.logic.FoodScheduler;

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
    private List<FoodItem> mGroceryList;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        this.mGroceryList = new LinkedList<>();

        setUpRecyclerView();

        ItemTouchHelper helper = initializeItemTouchHelper();
        helper.attachToRecyclerView(mRecyclerView);

        FoodScheduler scheduler = new FoodScheduler(mContext);
        try {
            this.mGroceryList = scheduler.getGroceryList();
        } catch (IllegalStateException e) {
            // TODO: paikanpitäjä näkymä käyttöliittymään.
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable state = mRecyclerView.getLayoutManager().onSaveInstanceState();
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

                Collections.swap(mGroceryList, from, to);

                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mGroceryList.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast toast = Toast.makeText(mContext, R.string.toast_checked, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
