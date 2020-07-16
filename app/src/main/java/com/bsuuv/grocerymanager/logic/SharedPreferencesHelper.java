package com.bsuuv.grocerymanager.logic;

import android.content.SharedPreferences;

import com.bsuuv.grocerymanager.db.entity.FoodItemEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An utility class to handle all interactions with <code>SharedPreferences</code>.
 */
public class SharedPreferencesHelper {

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private static final String UPDATE_LIST_KEY = "updatelist";

    private final SharedPreferences mSharedPreferences;
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

    public void saveUpdateList(List<FoodItemEntity> updateList) {
        String json = gson.toJson(updateList, listType);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(UPDATE_LIST_KEY, json);
        editor.apply();
    }

    public List<FoodItemEntity> getUpdateList() {
        String json = mSharedPreferences.getString(UPDATE_LIST_KEY, "");
        if (json.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(json, listType);
        }
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
