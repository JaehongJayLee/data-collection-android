package com.hongdoki.datacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCE_NAME = "SHARED_PREFERENCE";
    private static final String REPEAT_SENSING_KEY = "REPEAT_SENSING";
    private static final int LC_SERVICE_REQUEST_CODE = 0;
    private ToggleButton sensingToggle;
    private SharedPreferences sharedPreference;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        bindViews();
        setContent();
        setOnClickListener();
    }

    private void initialize() {
        sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private void bindViews() {
        sensingToggle = (ToggleButton) findViewById(R.id.tb_sensing);
    }

    private void setContent() {
        sensingToggle.setChecked(sharedPreference.getBoolean(REPEAT_SENSING_KEY, false));
    }

    private void setOnClickListener() {
        sensingToggle.setOnCheckedChangeListener(sensingToggleCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener sensingToggleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                sharedPreference.edit().putBoolean(REPEAT_SENSING_KEY, true).commit();
                setRepeatingSensingService();
            } else {
                sharedPreference.edit().putBoolean(REPEAT_SENSING_KEY, false).commit();
                cancelSensingService();
            }
        }
    };

    private void setRepeatingSensingService() {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), DataCollectionService.SENSING_INTERVAL_IN_MILLIS
                , pendingIntentSensing());

    }

    private PendingIntent pendingIntentSensing() {
        Intent intent = new Intent(this, DataCollectionService.class);
        return PendingIntent.getService(this, LC_SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void cancelSensingService() {
        alarmManager.cancel(pendingIntentSensing());
    }

}
