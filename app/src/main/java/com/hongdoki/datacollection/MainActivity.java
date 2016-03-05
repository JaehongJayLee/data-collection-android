package com.hongdoki.datacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.hongdoki.datacollection.helper.SharedPreferenceHelper;

public class MainActivity extends AppCompatActivity {

    private static final int DATA_COLLECTION_SERVICE_REQUEST_CODE = 0;
    private ToggleButton sensingToggle;
    private AlarmManager alarmManager;
    private SharedPreferenceHelper sharedPreferenceHelper;

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
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private void bindViews() {
        sensingToggle = (ToggleButton) findViewById(R.id.tb_sensing);
    }

    private void setContent() {
        sensingToggle.setChecked(sharedPreferenceHelper.getRepeatSensing());
    }

    private void setOnClickListener() {
        sensingToggle.setOnCheckedChangeListener(sensingToggleCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener sensingToggleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                sharedPreferenceHelper.commitRepeatSensing(true);
                setRepeatingSensingService();
            } else {
                sharedPreferenceHelper.commitRepeatSensing(false);
                cancelSensingService();
            }
        }
    };

    private void setRepeatingSensingService() {
        Log.i("debug0305", "setRepeatingSensingService()");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), DataCollectionService.SENSING_INTERVAL_IN_MILLIS
                , pendingIntentSensing());

    }

    private PendingIntent pendingIntentSensing() {
        Intent intent = new Intent(this, DataCollectionService.class);
        return PendingIntent.getService(this, DATA_COLLECTION_SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void cancelSensingService() {
        alarmManager.cancel(pendingIntentSensing());
    }

}
