package com.bsuuv.grocerymanager.logic;

import android.content.SharedPreferences;

import com.bsuuv.grocerymanager.domain.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private static final String FOOD_ITEMS_KEY = "foodItems";
    private static final String FOOD_ITEM_TRACKER_KEY = "foodItemTracker";
    private final SharedPreferences mSharedPreferences;
    private Gson gson;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
        this.gson = new Gson();
    }

    public Set<String> getGroceryDays() {
        return mSharedPreferences.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
    }

    public List<FoodItem> getFoodItems() {
        String jsonFoodItems = mSharedPreferences.getString(FOOD_ITEMS_KEY, "");
        Type listType = new TypeToken<List<FoodItem>>() {
        }.getType();

        if (jsonFoodItems.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(jsonFoodItems, listType);
        }
    }

    public void saveFoodItems(List<FoodItem> foodItems) {
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        String foodItemsJson = gson.toJson(foodItems);

        preferencesEditor.putString(FOOD_ITEMS_KEY, foodItemsJson);
        preferencesEditor.apply();
    }

    public Map<FoodItem, Double> getFoodItemTracker() {
        String jsonQuotientMap = mSharedPreferences.getString(FOOD_ITEM_TRACKER_KEY, "");
        Type mapType = new TypeToken<Map<FoodItem, Double>>() {
        }.getType();

        if (jsonQuotientMap.equals("")) { return new HashMap<>(); } else {
            return gson.fromJson(jsonQuotientMap, mapType);
        }
    }

    public void saveFoodItemTracker(Map<FoodItem, Double> foodItemQuotientMap) {
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        // TODO: selvitä, miten Jsoniin saadaan tallentumaan avaimeksi koko olio, ei vain sen nimi.
        // TODO: selvitä, tallennetaanko sanakirjan avaimiksi edes kokonaisia olioita vai
        //  ainostaan niiden nimet?
        String quotientMapJson = gson.toJson(foodItemQuotientMap);

        preferencesEditor.putString(FOOD_ITEM_TRACKER_KEY, quotientMapJson);
        preferencesEditor.apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
