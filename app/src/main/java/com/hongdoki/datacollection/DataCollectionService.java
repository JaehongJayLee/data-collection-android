package com.hongdoki.datacollection;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hongdoki.datacollection.probe.NetworkTrafficProbe;
import com.hongdoki.datacollection.probe.sensorandsetting.SensorAndSettingDataController;
import com.hongdoki.datacollection.probe.sensorandsetting.SensorCommitter;
import com.hongdoki.datacollection.probe.sensorandsetting.SettingCommitter;
import com.hongdoki.datacollection.util.TimeUnitUtil;

public class DataCollectionService extends Service {
    public static final long SENSING_INTERVAL_IN_MILLIS = 1 * 60 * 1000;
    private static final long SENSING_DURATION_IN_MILLIS = 5 * 1000;
    private static final long SENSING_FREQUENCY_IN_HZ = 50;
    private static final long DELAY_FOR_INITIALIZATION_IN_MILLIS = 5000;

    private SensorCommitter sensorCommitter;
    private SettingCommitter settingCommitter;
    private SensorAndSettingDataController sensorDataController;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private Runnable sensingRunnable;
    private boolean sensingOn = false;

    public DataCollectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize() {
        sensorCommitter = SensorCommitter.getInstance(this);
        settingCommitter = SettingCommitter.getInstance(this);
        sensorDataController = SensorAndSettingDataController.getInstance();
        handler = new Handler();
        countDownTimer = countDownTimer();
        sensingRunnable = sensingRunnable();
    }

    private CountDownTimer countDownTimer() {
        return new CountDownTimer(SENSING_DURATION_IN_MILLIS,
                TimeUnitUtil.frequencyToPeriodMillis(SENSING_FREQUENCY_IN_HZ)) {

            @Override
            public void onTick(long millisUntilFinished) {
                sensorDataController.takeSnapshot();
            }

            @Override
            public void onFinish() {
                finalizeSensing();
            }
        };
    }

    private void finalizeSensing() {
        stopSensing();
        storeData();
    }

    private void stopSensing() {
        handler.removeCallbacks(sensingRunnable);
        countDownTimer.cancel();
        sensorCommitter.unregister();
        settingCommitter.unregister();
        sensingOn = false;
    }

    private void storeData() {
        sensorDataController.storeDataToDatabase(this);
    }

    private Runnable sensingRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("debug0305", "DataCollectionService onStartCommand()");
        storeNetworkTrafficOffset();
        if (sensingOn) {
            finalizeSensing();
        }
        startSensing();
        return super.onStartCommand(intent, flags, startId);
    }

    private void storeNetworkTrafficOffset() {
        NetworkTrafficProbe probe = new NetworkTrafficProbe(this);
        probe.storeTraffic();
    }

    private void startSensing() {
        sensingOn = true;
        sensorCommitter.register();
        settingCommitter.register();
        handler.postDelayed(sensingRunnable, DELAY_FOR_INITIALIZATION_IN_MILLIS);
    }

    @Override
    public void onDestroy() {
        finalizeSensing();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
