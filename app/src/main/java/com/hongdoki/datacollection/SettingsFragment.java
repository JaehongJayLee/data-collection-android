package com.hongdoki.datacollection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String prefKeyCollectingOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        loadKeys();
    }

    private void loadKeys() {
        prefKeyCollectingOn = getString(R.string.pref_key_collecting_on);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(prefKeyCollectingOn.equals(key)){
            boolean collectingOn = sharedPreferences.getBoolean(key, false);
            DCServiceController controller =  new DCServiceController(getActivity());
            controller.onCollectingStatusChanged(collectingOn);
        }
    }
}
