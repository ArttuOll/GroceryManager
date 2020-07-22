package com.bsuuv.grocerymanager.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;

import com.bsuuv.grocerymanager.R;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class Settings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
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
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                Preference preference = getPreferenceScreen().getPreference(i);

                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;

                    for (int j = 0; j < preferenceGroup.getPreferenceCount(); j++) {
                        Preference single = preferenceGroup.getPreference(j);
                        updatePreference(single);
                    }
                } else {
                    updatePreference(preference);
                }
            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private String getEntriesString(MultiSelectListPreference preference) {
            StringBuilder builder = new StringBuilder();
            Set<String> entries = preference.getValues();

            for (String entry : entries) {
                builder.append(StringUtils.capitalize(entry)).append(" ");
            }
            return builder.toString();
        }

        private void updatePreference(Preference groceryDaysPreference) {
            if (groceryDaysPreference instanceof MultiSelectListPreference) {
                MultiSelectListPreference preference =
                        (MultiSelectListPreference) groceryDaysPreference;
                groceryDaysPreference.setSummary(getEntriesString(preference));
            }
        }
    }
}