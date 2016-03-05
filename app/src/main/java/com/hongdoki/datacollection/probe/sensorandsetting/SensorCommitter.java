package com.hongdoki.datacollection.probe.sensorandsetting;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorCommitter implements SensorEventListener {

    private static final int SAMPLING_PERIOD = SensorManager.SENSOR_DELAY_GAME;
    private static SensorCommitter instance;
    private final SensorAndSettingSnapshot sensorSnapshot;
    private final Context context;
    private SensorManager sensorManager;
    private float[] gravityForOrientation;
    private float[] geomagneticForOrientation;

    private SensorCommitter(Context context) {
        this.context = context;
        sensorSnapshot = SensorAndSettingSnapshot.getInstance();
    }

    public static SensorCommitter getInstance(Context context) {
        if (instance == null) {
            synchronized (SensorCommitter.class) {
                if (instance == null) {
                    instance = new SensorCommitter(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorSnapshot.accX = event.values[0];
                sensorSnapshot.accY = event.values[1];
                sensorSnapshot.accZ = event.values[2];
                break;
            case Sensor.TYPE_GRAVITY:
                gravityForOrientation = event.values;
                sensorSnapshot.gravityX = event.values[0];
                sensorSnapshot.gravityY = event.values[1];
                sensorSnapshot.gravityZ = event.values[2];
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                sensorSnapshot.linearAccX = event.values[0];
                sensorSnapshot.linearAccY = event.values[1];
                sensorSnapshot.linearAccZ = event.values[2];
                break;
            case Sensor.TYPE_GYROSCOPE:
                sensorSnapshot.gyroX = event.values[0];
                sensorSnapshot.gyroY = event.values[1];
                sensorSnapshot.gyroZ = event.values[2];
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                sensorSnapshot.rotationSinX = event.values[0];
                sensorSnapshot.rotationSinY = event.values[1];
                sensorSnapshot.rotationSinZ = event.values[2];
                sensorSnapshot.rotationCos = event.values[3];
                onOrientationChanged();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagneticForOrientation = event.values;
                sensorSnapshot.magX = event.values[0];
                sensorSnapshot.magY = event.values[1];
                sensorSnapshot.magZ = event.values[2];
                break;
            case Sensor.TYPE_PRESSURE:
                sensorSnapshot.pressure = event.values[0];
                break;
            case Sensor.TYPE_LIGHT:
                sensorSnapshot.light = event.values[0];
                break;
            case Sensor.TYPE_PROXIMITY:
                if(event.values[0]>0)
                    sensorSnapshot.proximity = SensorAndSettingSnapshot.PROXIMITY_MAX;
                else
                    sensorSnapshot.proximity = SensorAndSettingSnapshot.PROXIMITY_MIN;
                break;
        }
    }

    private void onOrientationChanged() {
        if (gravityForOrientation != null && geomagneticForOrientation != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I,
                    gravityForOrientation, geomagneticForOrientation);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                sensorSnapshot.azimuth = orientation[0];
                sensorSnapshot.pitch = orientation[1];
                sensorSnapshot.roll = orientation[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void register() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SAMPLING_PERIOD);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SAMPLING_PERIOD);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }
}
