package com.hongdoki.datacollection;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hongdoki.datacollection.probe.ApplicationProbe;
import com.hongdoki.datacollection.probe.DeviceInfoProbe;
import com.hongdoki.datacollection.probe.NetworkTrafficProbe;
import com.hongdoki.datacollection.probe.sensorandsetting.SensorAndSettingDataController;
import com.hongdoki.datacollection.probe.sensorandsetting.SensorCommitter;
import com.hongdoki.datacollection.probe.sensorandsetting.SettingCommitter;
import com.hongdoki.datacollection.util.TimeUnitUtil;

public class DataCollectionService extends Service {
    private static final long DELAY_FOR_INITIALIZATION_IN_MILLIS = 5000;

    private SensorCommitter sensorCommitter;
    private SettingCommitter settingCommitter;
    private SensorAndSettingDataController sensorDataController;
    private CountDownTimer countDownTimer;
    private Handler handler;
    private Runnable sensingRunnable;
    private boolean sensingOn = false;
    private SharedPreferences sharedPreferences;

    public DataCollectionService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    private void initialize() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sensorCommitter = SensorCommitter.getInstance(this);
        settingCommitter = SettingCommitter.getInstance(this);
        sensorDataController = SensorAndSettingDataController.getInstance();
        handler = new Handler();
        countDownTimer = countDownTimer();
        sensingRunnable = sensingRunnable();
    }

    private CountDownTimer countDownTimer() {
        return new CountDownTimer(sensingDurationInMillis(),
                TimeUnitUtil.frequencyToPeriodMillis(sensingFrequency())) {

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

    private long sensingDurationInMillis() {
        String durationKey = getString(R.string.pref_key_collecting_duration);
        String durationString = sharedPreferences.getString(durationKey,
                getString(R.string.pref_default_collecting_duration));
        long durationInSeconds = Long.parseLong(durationString);
        return TimeUnitUtil.secondToMillis(durationInSeconds);
    }

    private long sensingFrequency() {
        String frequencyKey = getString(R.string.pref_key_collecting_frequency);
        String frequencyString = sharedPreferences.getString(frequencyKey,
                getString(R.string.pref_default_collecting_frequency));
        return Long.parseLong(frequencyString);

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
        if (sensingOn) {
            finalizeSensing();
        }
        storeDeviceInfo();
        storeApplicationList();
        storeNetworkTrafficOffset();
        startSensing();
        return super.onStartCommand(intent, flags, startId);
    }

    private void storeDeviceInfo() {
        DeviceInfoProbe probe = new DeviceInfoProbe(this);
        probe.collect();
    }

    private void storeApplicationList() {
        ApplicationProbe probe = new ApplicationProbe(this);
        probe.collect();
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
