package com.bsuuv.grocerymanager.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bsuuv.grocerymanager.R;
import com.bsuuv.grocerymanager.adapters.SimpleAdapter;
import com.bsuuv.grocerymanager.adapters.SimpleSectionedRecyclerViewAdapter;
import com.bsuuv.grocerymanager.domain.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class Configurations extends AppCompatActivity {

    private FoodItem[] mFoodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);

        generateTestData();

        setUpRecyclerView();
    }

    public void onFabClick(View view) {
        Intent intent = new Intent(this, NewFoodItem.class);
        this.startActivity(intent);
    }

    private void generateTestData() {
        TypedArray foodImageResources = getResources().obtainTypedArray(R.array.food_images);
        String[] foodLabels = getResources().getStringArray(R.array.food_labels);
        String[] foodBrands = getResources().getStringArray(R.array.food_brands);
        String[] foodInfos = getResources().getStringArray(R.array.food_infos);
        String[] foodWeights = getResources().getStringArray(R.array.food_weights);
        String[] amounts = getResources().getStringArray(R.array.food_amounts);

        this.mFoodItems = new FoodItem[foodLabels.length];

        for (int i = 0; i < foodLabels.length; i++) {
            mFoodItems[i] = new FoodItem(foodLabels[i], foodBrands[i], foodInfos[i], foodWeights[i],
                    amounts[i], foodImageResources.getResourceId(i, 0));
        }

        foodImageResources.recycle();
    }

    private void setUpRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.config_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SimpleAdapter mAdapter = new SimpleAdapter(this, mFoodItems);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Biweekly"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(1, "Weekly"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(3, "Monthly"));

        SimpleSectionedRecyclerViewAdapter.Section[] sectionsArray =
                new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        //2. ja 3. parametri ovat otsakkeiden layout ja textview layoutin sisällä
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                new SimpleSectionedRecyclerViewAdapter(this, R.layout.recyclerview_section, R.id.section_text, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(sectionsArray));

        mRecyclerView.setAdapter(mSectionedAdapter);
    }
}
