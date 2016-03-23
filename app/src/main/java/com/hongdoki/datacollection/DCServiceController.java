package com.hongdoki.datacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hongdoki.datacollection.util.TimeUnitUtil;

public class DCServiceController {
    private final Context context;
    private final AlarmManager alarmManager;
    private static final int DATA_COLLECTION_SERVICE_REQUEST_CODE = 0;

    public DCServiceController(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void onCollectingStatusChanged(boolean collectingOn) {
        if(collectingOn)
            setRepeatingDCService();
        else
            cancelDCService();
    }

    private void setRepeatingDCService() {
        Log.i("debug0305", "setRepeatingDCService()");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), collectingIntervalInMillis()
                , pendingIntentDCService());

    }

    private long collectingIntervalInMillis() {
        String intervalKey = context.getString(R.string.pref_key_collecting_interval);
        String defaultInterval = context.getString(R.string.pref_default_collecting_interval);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long intervalInMinute = Long.parseLong(sharedPreferences.getString(intervalKey, defaultInterval));
        return TimeUnitUtil.minuteToMillis(intervalInMinute);
    }

    private PendingIntent pendingIntentDCService() {
        Intent intent = new Intent(context, DataCollectionService.class);
        return PendingIntent.getService(context, DATA_COLLECTION_SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void cancelDCService() {
        alarmManager.cancel(pendingIntentDCService());
    }

}
