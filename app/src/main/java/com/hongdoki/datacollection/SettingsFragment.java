package com.hongdoki.datacollection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.hongdoki.datacollection.helper.DCServiceController;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String collectingOnKey;
    private String frequencyKey;
    private String durationKey;
    private String intervalKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setDefaultValues();
        loadKeys();
        initializeSummary();
    }

    private void setDefaultValues() {
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_general, false);
    }

    private void loadKeys() {
        collectingOnKey = getString(R.string.pref_key_collecting_on);
        frequencyKey = getString(R.string.pref_key_collecting_frequency);
        durationKey = getString(R.string.pref_key_collecting_duration);
        intervalKey = getString(R.string.pref_key_collecting_interval);
    }

    private void initializeSummary() {
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(getActivity());
        setSummary(sharedPreferences, frequencyKey);
        setSummary(sharedPreferences, durationKey);
        setSummary(sharedPreferences, intervalKey);
    }

    private void setSummary(SharedPreferences sharedPreferences, String key) {
        getPreferenceScreen().findPreference(key).setSummary(summaryString(sharedPreferences, key));
    }

    private String summaryString(SharedPreferences sharedPreferences, String key) {
        return getString(summaryStringId(key), sharedPreferences.getString(key, null));
    }

    private int summaryStringId(String key) {
        if(key.equals(frequencyKey)){
            return R.string.pref_summary_frequency;
        }else if(key.equals(durationKey)){
            return R.string.pref_summary_duration;
        }else if(key.equals(intervalKey)){
            return R.string.pref_summary_interval;
        }else {
            return 0;
        }
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
        if (collectingOnKey.equals(key)) {
            boolean collectingOn = sharedPreferences.getBoolean(key, false);
            DCServiceController controller = new DCServiceController(getActivity());
            controller.onCollectingStatusChanged(collectingOn);
        } else{
            setSummary(sharedPreferences, key);
        }
    }
}
