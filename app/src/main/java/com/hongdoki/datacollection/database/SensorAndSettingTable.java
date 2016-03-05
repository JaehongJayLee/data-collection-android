package com.hongdoki.datacollection.database;

import java.util.ArrayList;
import java.util.List;

public class SensorAndSettingTable extends Table{
    public static final String SENSOR_AND_SETTING_TABLE_NAME = "sensor_and_setting";
    public static final String TIMESTAMP = "timestamp";
    public static final String SESSION_ID = "session_id";
    public static final String ACC_X = "acc_x";
    public static final String ACC_Y = "acc_y";
    public static final String ACC_Z = "acc_z";
    public static final String GRAVITY_X = "gravity_x";
    public static final String GRAVITY_Y = "gravity_y";
    public static final String GRAVITY_Z = "gravity_z";
    public static final String LINEAR_ACC_X = "linear_acc_x";
    public static final String LINEAR_ACC_Y = "linear_acc_y";
    public static final String LINEAR_ACC_Z = "linear_acc_z";
    public static final String GYRO_X = "gyro_x";
    public static final String GYRO_Y = "gyro_y";
    public static final String GYRO_Z = "gyro_z";
    public static final String PITCH = "pitch";
    public static final String ROLL = "roll";
    public static final String AZIMUTH = "azimuth";
    public static final String ROTATION_SIN_X = "rotation_sin_x";
    public static final String ROTATION_SIN_Y = "rotation_sin_y";
    public static final String ROTATION_SIN_Z = "rotation_sin_z";
    public static final String ROTATION_COS = "rotation_cos";
    public static final String MAG_X = "mag_x";
    public static final String MAG_Y = "mag_y";
    public static final String MAG_Z = "mag_z";
    public static final String PRESSURE = "pressure";
    public static final String PROXIMITY = "proximity";
    public static final String LIGHT = "light";
    public static final String SCREEN_ON = "screen_on";
    public static final String ACC_ROTATION = "acc_rotation";
    public static final String SCREEN_BRIGHTNESS = "screen_brightness";
    public static final String VOL_ALARM = "vol_alarm";
    public static final String VOL_MUSIC = "vol_music";
    public static final String VOL_NOTIFICATION = "vol_notification";
    public static final String VOL_RING = "vol_ring";
    public static final String VOL_SYSTEM = "vol_system";
    public static final String VOL_VOICE = "vol_voice";
    public static final String FONT_SCALE = "font_scale";

    @Override
    public String getName() {
        return SENSOR_AND_SETTING_TABLE_NAME;
    }

    @Override
    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(TIMESTAMP, Column.SQL_DATA_TYPE_TIMESTAMP));
        columns.add(new Column(SESSION_ID, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(ACC_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ACC_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ACC_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GRAVITY_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GRAVITY_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GRAVITY_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(LINEAR_ACC_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(LINEAR_ACC_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(LINEAR_ACC_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GYRO_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GYRO_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(GYRO_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(PITCH, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ROLL, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(AZIMUTH, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ROTATION_SIN_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ROTATION_SIN_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ROTATION_SIN_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(ROTATION_COS, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(MAG_X, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(MAG_Y, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(MAG_Z, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(PRESSURE, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(PROXIMITY, Column.SQL_DATA_TYPE_TINYINT));
        columns.add(new Column(LIGHT, Column.SQL_DATA_TYPE_REAL));
        columns.add(new Column(SCREEN_ON, Column.SQL_DATA_TYPE_TINYINT));
        columns.add(new Column(ACC_ROTATION, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(SCREEN_BRIGHTNESS, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_ALARM, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_MUSIC, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_NOTIFICATION, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_RING, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_SYSTEM, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(VOL_VOICE, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(FONT_SCALE, Column.SQL_DATA_TYPE_REAL));
        return columns;
    }
}
