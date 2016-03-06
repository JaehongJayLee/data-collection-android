package com.hongdoki.datacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.hongdoki.datacollection.helper.SharedPreferenceHelper;

public class MainActivity extends AppCompatActivity {

    private static final int DATA_COLLECTION_SERVICE_REQUEST_CODE = 0;
    private Switch sensingSwitch;
    private AlarmManager alarmManager;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private LinearLayout sensingOnOffLayout;

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
        sensingSwitch = (Switch) findViewById(R.id.switch_sensing);
        sensingOnOffLayout = (LinearLayout) findViewById(R.id.ll_toggle);
    }

    private void setContent() {
        sensingSwitch.setChecked(sharedPreferenceHelper.getRepeatSensing());
    }

    private void setOnClickListener() {
        sensingOnOffLayout.setOnClickListener(sensingOnOffLayoutClickListener);
    }

    View.OnClickListener sensingOnOffLayoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleSensing();
        }
    };

    private void toggleSensing() {
        if (sharedPreferenceHelper.getRepeatSensing()) {
            sharedPreferenceHelper.commitRepeatSensing(false);
            sensingSwitch.setChecked(false);
            cancelSensingService();
        } else {
            sharedPreferenceHelper.commitRepeatSensing(true);
            sensingSwitch.setChecked(true);
            setRepeatingSensingService();
        }
    }


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
