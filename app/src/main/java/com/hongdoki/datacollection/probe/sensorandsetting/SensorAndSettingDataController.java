package com.hongdoki.datacollection.probe.sensorandsetting;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.hongdoki.datacollection.database.DatabaseHelper;
import com.hongdoki.datacollection.database.SensorAndSettingTable;
import com.hongdoki.datacollection.helper.SharedPreferenceHelper;
import com.hongdoki.datacollection.util.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

public class SensorAndSettingDataController {
    private static SensorAndSettingDataController instance;
    private final SensorAndSettingSnapshot snapshot;
    private List<SensorAndSettingSnapshot> sensorAndSettingData = new ArrayList<>();

    private SensorAndSettingDataController() {
        snapshot = SensorAndSettingSnapshot.getInstance();
    }

    public static SensorAndSettingDataController getInstance() {
        if (instance == null) {
            synchronized (SensorAndSettingDataController.class) {
                if (instance == null)
                    instance = new SensorAndSettingDataController();
            }
        }
        return instance;
    }

    public void takeSnapshot() {
        try {
            snapshot.timestamp = TimestampUtil.currentStringWithMillis();
            sensorAndSettingData.add(snapshot.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void storeDataToDatabase(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        Log.i("debug0205", "sensorDataToDatabase() started size: " + sensorAndSettingData.size());
        int sessionId  = SharedPreferenceHelper.getInstance(context)
                .getAndIncrementSensorAndSettingSessionId();
        for (SensorAndSettingSnapshot snapshot : sensorAndSettingData) {
            ContentValues values = new ContentValues();
            values.put(SensorAndSettingTable.TIMESTAMP, snapshot.timestamp);
            values.put(SensorAndSettingTable.SESSION_ID, sessionId);
            values.put(SensorAndSettingTable.ACC_X, snapshot.accX);
            values.put(SensorAndSettingTable.ACC_Y, snapshot.accY);
            values.put(SensorAndSettingTable.ACC_Z, snapshot.accZ);
            values.put(SensorAndSettingTable.LINEAR_ACC_X, snapshot.linearAccX);
            values.put(SensorAndSettingTable.LINEAR_ACC_Y, snapshot.linearAccY);
            values.put(SensorAndSettingTable.LINEAR_ACC_Z, snapshot.linearAccZ);
            values.put(SensorAndSettingTable.GRAVITY_X, snapshot.gravityX);
            values.put(SensorAndSettingTable.GRAVITY_Y, snapshot.gravityY);
            values.put(SensorAndSettingTable.GRAVITY_Z, snapshot.gravityZ);
            values.put(SensorAndSettingTable.GYRO_X, snapshot.gyroX);
            values.put(SensorAndSettingTable.GYRO_Y, snapshot.gyroY);
            values.put(SensorAndSettingTable.GYRO_Z, snapshot.gyroZ);
            values.put(SensorAndSettingTable.PITCH, snapshot.pitch);
            values.put(SensorAndSettingTable.ROLL, snapshot.roll);
            values.put(SensorAndSettingTable.AZIMUTH, snapshot.azimuth);
            values.put(SensorAndSettingTable.ROTATION_SIN_X, snapshot.rotationSinX);
            values.put(SensorAndSettingTable.ROTATION_SIN_Y, snapshot.rotationSinY);
            values.put(SensorAndSettingTable.ROTATION_SIN_Z, snapshot.rotationSinZ);
            values.put(SensorAndSettingTable.ROTATION_COS, snapshot.rotationCos);
            values.put(SensorAndSettingTable.MAG_X, snapshot.magX);
            values.put(SensorAndSettingTable.MAG_Y, snapshot.magY);
            values.put(SensorAndSettingTable.MAG_Z, snapshot.magZ);
            values.put(SensorAndSettingTable.PRESSURE, snapshot.pressure);
            values.put(SensorAndSettingTable.PROXIMITY, snapshot.proximity);
            values.put(SensorAndSettingTable.LIGHT, snapshot.light);
            values.put(SensorAndSettingTable.SCREEN_ON, snapshot.screenOn);
            values.put(SensorAndSettingTable.ACC_ROTATION, snapshot.accRotation);
            values.put(SensorAndSettingTable.SCREEN_BRIGHTNESS, snapshot.screenBrightness);
            values.put(SensorAndSettingTable.VOL_ALARM, snapshot.volAlarm);
            values.put(SensorAndSettingTable.VOL_MUSIC, snapshot.volMusic);
            values.put(SensorAndSettingTable.VOL_NOTIFICATION, snapshot.volNotification);
            values.put(SensorAndSettingTable.VOL_RING, snapshot.volRing);
            values.put(SensorAndSettingTable.VOL_SYSTEM, snapshot.volSystem);
            values.put(SensorAndSettingTable.VOL_VOICE, snapshot.volVoice);
            values.put(SensorAndSettingTable.FONT_SCALE, snapshot.fontScale);
            databaseHelper.getDatabase().insert(SensorAndSettingTable.SENSOR_AND_SETTING_TABLE_NAME
                    , null, values);
        }
        Log.i("debug0205", "sensor and settingData stored size: " + sensorAndSettingData.size());
        sensorAndSettingData.clear();
    }

    public List<SensorAndSettingSnapshot> getData() {
        return sensorAndSettingData;
    }
}
