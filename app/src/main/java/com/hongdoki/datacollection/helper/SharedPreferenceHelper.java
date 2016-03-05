package com.hongdoki.datacollection.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private static final String SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE_NAME";
    private static final String REPEAT_SENSING_KEY = "REPEAT_SENSING";
    private static final String TRAFFIC_STATS_PAST_JSON_KEY = "TRAFFIC_STATS_PAST_JSON";
    private static final String TRAFFIC_SESSION_ID_KEY = "TRAFFIC_SESSION_ID";
    private static final String SENSOR_AND_SETTING_SESSION_ID = "SENSOR_AND_SETTING_SESSION_ID";
    private static SharedPreferenceHelper instance;
    private final SharedPreferences mPrefs;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public SharedPreferenceHelper(Context context) {
        this.context = context;
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SharedPreferenceHelper.class) {
                if (instance == null) {
                    instance = new SharedPreferenceHelper(context);
                }
            }
        }
        return instance;
    }

    public void commitRepeatSensing(boolean b) {
        editor.putBoolean(REPEAT_SENSING_KEY, b).commit();
    }

    public boolean getRepeatSensing() {
        return mPrefs.getBoolean(REPEAT_SENSING_KEY, false);
    }

    public void commitTrafficStatsPastJson(String json) {
        editor.putString(TRAFFIC_STATS_PAST_JSON_KEY, json).commit();
    }

    public String getTrafficStatsPastJson() {
        return mPrefs.getString(TRAFFIC_STATS_PAST_JSON_KEY, "");
    }

    public int getAndIncrementTrafficSessionId() {
        int index = mPrefs.getInt(TRAFFIC_SESSION_ID_KEY, 0);
        editor.putInt(TRAFFIC_SESSION_ID_KEY, index + 1).commit();
        return index;
    }

    public int getAndIncrementSensorAndSettingSessionId() {
        int index = mPrefs.getInt(SENSOR_AND_SETTING_SESSION_ID, 0);
        editor.putInt(SENSOR_AND_SETTING_SESSION_ID, index + 1).commit();
        return index;
    }

}
