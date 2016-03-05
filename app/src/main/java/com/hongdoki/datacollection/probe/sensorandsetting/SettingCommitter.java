package com.hongdoki.datacollection.probe.sensorandsetting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;

public class SettingCommitter {


    private static final byte SCREEN_OFF = 0;
    private static final byte SCREEN_ON = 1;
    private static SettingCommitter instance;
    private final Context context;
    private final SensorAndSettingSnapshot snapshot;
    private final SettingsContentObserver settingsContentObserver;
    private BroadcastReceiver screenReceiver;
    private AudioManager audioManager;

    public SettingCommitter(Context context) {
        this.context = context;
        snapshot = SensorAndSettingSnapshot.getInstance();
        screenReceiver = screenReceiver();
        settingsContentObserver = new SettingsContentObserver(new Handler());
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private BroadcastReceiver screenReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    snapshot.screenOn = SCREEN_OFF;
                } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    snapshot.screenOn = SCREEN_ON;
                }
            }
        };
    }

    public static SettingCommitter getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingCommitter.class) {
                if (instance == null) {
                    instance = new SettingCommitter(context);
                }
            }
        }
        return instance;
    }

    public void register() {
        initializeSnapshot();
        context.registerReceiver(screenReceiver, filterScreenOnOff());
        context.getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, settingsContentObserver);
    }

    private IntentFilter filterScreenOnOff() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        return filter;
    }

    private void initializeSnapshot() {
        snapshot.screenOn = currentScreenON();
        commitCurrentSettingsWithoutScreenOn();
    }

    private byte currentScreenON() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn() ? SCREEN_ON : SCREEN_OFF;
    }

    private void commitCurrentSettingsWithoutScreenOn() {
        snapshot.accRotation = currentValue(Settings.System.ACCELEROMETER_ROTATION);
        snapshot.screenBrightness = currentValue(Settings.System.SCREEN_BRIGHTNESS);
        snapshot.volAlarm = currentVolume(AudioManager.STREAM_ALARM);
        snapshot.volMusic = currentVolume(AudioManager.STREAM_MUSIC);
        snapshot.volNotification = currentVolume(AudioManager.STREAM_NOTIFICATION);
        snapshot.volRing = currentVolume(AudioManager.STREAM_RING);
        snapshot.volSystem = currentVolume(AudioManager.STREAM_SYSTEM);
        snapshot.volVoice = currentVolume(AudioManager.STREAM_VOICE_CALL);
        snapshot.fontScale = context.getResources().getConfiguration().fontScale;
    }

    private int currentValue(String name) {
        return Settings.System.getInt(
                context.getContentResolver(), name, SensorAndSettingSnapshot.DEFAULT_VALUE);
    }

    private int currentVolume(int streamType) {
        return audioManager.getStreamVolume(streamType);
    }


    public void unregister() {
        context.unregisterReceiver(screenReceiver);
        context.getContentResolver().unregisterContentObserver(settingsContentObserver);
    }

    private class SettingsContentObserver extends ContentObserver {

        public SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            commitCurrentSettingsWithoutScreenOn();
        }
    }

}
