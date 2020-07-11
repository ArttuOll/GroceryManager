package com.bsuuv.grocerymanager;

import android.content.SharedPreferences;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An utility class to handle all interactions with <code>SharedPreferences</code>.
 */
public class SharedPreferencesHelper {

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private static final String FOOD_ITEMS_KEY = "foodItems";
    private static final String FOOD_ITEM_TRACKER_KEY = "foodItemTracker";

    private final SharedPreferences mSharedPreferences;
    private final Type mapType = new TypeToken<Map<FoodItemEntity, Double>>() {
    }.getType();
    private final Type listType = new TypeToken<List<FoodItemEntity>>() {
    }.getType();
    private Gson gson;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        this.gson = builder.create();
    }

    /**
     * Returns a list of grocery days selected by the user in Settings.
     *
     * @return A <code>Set</code> containing the grocery days as lowercase strings. If none are
     * selected, an empty <code>Set</code> is returned.
     */
    public Set<String> getGroceryDays() {
        return mSharedPreferences.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
    }

    /**
     * Returns a list of all food-items created by the user.
     *
     * @return A list containing <code>FoodItem</code>s. If none was saved before, returns an empty
     * <code>List</code>.
     */
    public List<FoodItemEntity> getFoodItems() {
        String jsonFoodItems = mSharedPreferences.getString(FOOD_ITEMS_KEY, "");

        if (jsonFoodItems.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(jsonFoodItems, listType);
        }
    }

    /**
     * Saves the list of given <code>FoodItems</code> to <code>SharedPreferences</code>.
     *
     * @param foodItems List of <code>FoodItems</code> to be saved.
     */
    public void saveFoodItems(List<FoodItemEntity> foodItems) {
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        String foodItemsJson = gson.toJson(foodItems, listType);

        preferencesEditor.putString(FOOD_ITEMS_KEY, foodItemsJson);
        preferencesEditor.apply();
    }

    /**
     * Returns the saved <code>FoodItemTracker</code> required by {@link FoodScheduler}.
     *
     * @return <code>Map</code> containing <code>FoodItem</code>s and their frequency quotients. If
     * none was saved before, returns an empty Map.
     */
    public Map<FoodItemEntity, Double> getFoodItemTracker() {
        String jsonQuotientMap = mSharedPreferences.getString(FOOD_ITEM_TRACKER_KEY, "");

        if (jsonQuotientMap.equals("")) { return new HashMap<>(); } else {
            return gson.fromJson(jsonQuotientMap, mapType);
        }
    }

    /**
     * Saves <code>FoodItemTracker</code> required by {@link FoodScheduler}.
     *
     * @param foodItemQuotientMap <code>Map</code> containing FoodItems and their frequency
     *                            quotients.
     */
    public void saveFoodItemTracker(Map<FoodItemEntity, Double> foodItemQuotientMap) {
        SharedPreferences.Editor preferencesEditor = mSharedPreferences.edit();
        String quotientMapJson = gson.toJson(foodItemQuotientMap, mapType);

        preferencesEditor.putString(FOOD_ITEM_TRACKER_KEY, quotientMapJson);
        preferencesEditor.apply();
    }

    /**
     * Clears all items from <code>SharedPreferences</code>.
     */
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
