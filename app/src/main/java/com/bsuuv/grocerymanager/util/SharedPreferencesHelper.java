package com.bsuuv.grocerymanager.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

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

    public static final String GROCERY_DAYS_KEY = "grocerydays";

    private final SharedPreferences mSharedPreferences;
    private final Type listType = new TypeToken<List<FoodItemEntity>>() {
    }.getType();
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
    }


    /**
     * Returns a list of grocery days selected by the user in SettingsActivity.
     *
     * @return A <code>Set</code> containing the grocery days as lowercase strings. If none are
     * selected, an empty <code>Set</code> is returned.
     */
    public Set<String> getGroceryDays() {
        return mSharedPreferences.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
    }

    public void saveList(List<FoodItemEntity> foodItems, String key) {
        String json = gson.toJson(foodItems, listType);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public List<FoodItemEntity> getList(String key) {
        String json = mSharedPreferences.getString(key, "");
        if (json.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(json, listType);
        }
    }

    public void clearList(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Clears all items from <code>SharedPreferences</code>.
     */
    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
}
