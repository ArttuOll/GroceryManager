package com.bsuuv.grocerymanager.logic;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.bsuuv.grocerymanager.domain.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesHelper {

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private static final String FOOD_ITEMS_KEY = "foodItems";
    private final SharedPreferences mSharedPreferences;
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new Gson();
    }

    public Set<String> getGroceryDays() {
        return mSharedPreferences.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
    }

    public List<FoodItem> getFoodItems() {
        String jsonFoodItems = mSharedPreferences.getString(FOOD_ITEMS_KEY, "");
        Type listType = new TypeToken<List<FoodItem>>() {
        }.getType();

        return gson.fromJson(jsonFoodItems, listType);
    }

    public void saveFoodItems(List<FoodItem> foodItems) {
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        String foodItemsJson = gson.toJson(foodItems);

        preferencesEditor.putString(FOOD_ITEMS_KEY, foodItemsJson);
        preferencesEditor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
