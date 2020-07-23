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

    private static final String GROCERY_DAYS_KEY = "grocerydays";
    private static final String MODIFIED_LIST_KEY = "modifiedlist";
    private static final String CHECKED_ITEMS_KEY = "checkeditems";

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
     * Returns a list of grocery days selected by the user in Settings.
     *
     * @return A <code>Set</code> containing the grocery days as lowercase strings. If none are
     * selected, an empty <code>Set</code> is returned.
     */
    public Set<String> getGroceryDays() {
        return mSharedPreferences.getStringSet(GROCERY_DAYS_KEY, new HashSet<>());
    }

    public void saveModifiedList(List<FoodItemEntity> updateList) {
        String json = gson.toJson(updateList, listType);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(MODIFIED_LIST_KEY, json);
        editor.apply();
    }

    public void saveCheckedItems(List<FoodItemEntity> checkedItems) {
        String json = gson.toJson(checkedItems, listType);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CHECKED_ITEMS_KEY, json);
        editor.apply();
    }

    // TODO: muokkaa metodeja yleiskäyttöisemmiksi, esim. saveList(String key) jne...
    public List<FoodItemEntity> getModifiedList() {
        String json = mSharedPreferences.getString(MODIFIED_LIST_KEY, "");
        if (json.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(json, listType);
        }
    }

    public List<FoodItemEntity> getCheckedItems() {
        String json = mSharedPreferences.getString(CHECKED_ITEMS_KEY, "");
        if (json.equals("")) { return new ArrayList<>(); } else {
            return gson.fromJson(json, listType);
        }
    }

    public void clearUpdateList() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(MODIFIED_LIST_KEY);
        editor.apply();
    }

    public void clearCheckedItems() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(CHECKED_ITEMS_KEY);
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
}
