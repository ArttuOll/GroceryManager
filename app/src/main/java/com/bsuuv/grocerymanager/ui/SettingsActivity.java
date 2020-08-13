package com.bsuuv.grocerymanager.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.bsuuv.grocerymanager.R;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        launchSettingsFragment();
        setActionBar();
    }

    private void launchSettingsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements
            SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            setGroceryDaysSummary();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            setGroceryDaysSummary();
        }

        private void setGroceryDaysSummary() {
            MultiSelectListPreference groceryDays = Objects.requireNonNull(findPreference(
                    "grocerydays"));
            groceryDays.setSummary(getEntriesString(groceryDays));
        }

        private String getEntriesString(MultiSelectListPreference preference) {
            Set<String> entries = preference.getValues();
            return buildEntriesString(entries);
        }

        private String buildEntriesString(Set<String> entries) {
            StringBuilder builder = new StringBuilder();
            for (String entry : entries) {
                builder.append(StringUtils.capitalize(entry)).append(" ");
            }
            return builder.toString();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}