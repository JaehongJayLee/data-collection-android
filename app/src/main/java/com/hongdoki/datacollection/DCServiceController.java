package com.hongdoki.datacollection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
                System.currentTimeMillis(), DataCollectionService.SENSING_INTERVAL_IN_MILLIS
                , pendingIntentDCService());

    }

    private PendingIntent pendingIntentDCService() {
        Intent intent = new Intent(context, DataCollectionService.class);
        return PendingIntent.getService(context, DATA_COLLECTION_SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void cancelDCService() {
        alarmManager.cancel(pendingIntentDCService());
    }

}
