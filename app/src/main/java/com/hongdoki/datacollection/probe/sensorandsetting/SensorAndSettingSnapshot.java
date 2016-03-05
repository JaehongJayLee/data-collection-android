package com.hongdoki.datacollection.probe.sensorandsetting;

public class SensorAndSettingSnapshot implements Cloneable {
    public static final byte DEFAULT_VALUE = -1;
    public static final int PROXIMITY_MIN = 0;
    public static final int PROXIMITY_MAX = 1;
    private static SensorAndSettingSnapshot instance;

    public String timestamp = "";
    public float accX = Float.MIN_VALUE, accY = Float.MIN_VALUE, accZ = Float.MIN_VALUE;
    public float gravityX = Float.MIN_VALUE, gravityY = Float.MIN_VALUE, gravityZ = Float.MIN_VALUE;
    public float linearAccX = Float.MIN_VALUE, linearAccY = Float.MIN_VALUE, linearAccZ = Float.MIN_VALUE;

    public float gyroX = Float.MIN_VALUE, gyroY = Float.MIN_VALUE, gyroZ = Float.MIN_VALUE;

    public float pitch = Float.MIN_VALUE, roll = Float.MIN_VALUE, azimuth = Float.MIN_VALUE;
    public float rotationSinX = Float.MIN_VALUE, rotationSinY = Float.MIN_VALUE,
    rotationSinZ = Float.MIN_VALUE, rotationCos = Float.MIN_VALUE;

    public float magX = Float.MIN_VALUE, magY = Float.MIN_VALUE, magZ = Float.MIN_VALUE;

    public float pressure = Float.MIN_VALUE;
    public float light = Float.MIN_VALUE;
    public int proximity = DEFAULT_VALUE;

    public byte screenOn = DEFAULT_VALUE;
    public int accRotation = DEFAULT_VALUE;
    public int screenBrightness = DEFAULT_VALUE;
    public int volAlarm = DEFAULT_VALUE, volMusic = DEFAULT_VALUE, volNotification = DEFAULT_VALUE
            , volRing = DEFAULT_VALUE, volSystem = DEFAULT_VALUE,volVoice = DEFAULT_VALUE;
    public float fontScale = Float.MIN_VALUE;

    public static SensorAndSettingSnapshot getInstance() {
        if (instance == null) {
            synchronized (SensorAndSettingSnapshot.class) {
                if (instance == null) {
                    instance = new SensorAndSettingSnapshot();
                }
            }
        }
        return instance;
    }

    @Override
    protected SensorAndSettingSnapshot clone() throws CloneNotSupportedException {
        return (SensorAndSettingSnapshot) super.clone();
    }
}