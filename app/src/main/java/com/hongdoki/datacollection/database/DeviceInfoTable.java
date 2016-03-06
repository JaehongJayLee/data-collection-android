package com.hongdoki.datacollection.database;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoTable extends Table{
    public static final String DEVICE_INFO_TABLE_NAME = "device";
    public static final String ANDROID_ID = "android_id";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String BUILD_NUMBER = "build_number";
    public static final String FIRMWARE_VERSION = "firmware_version";
    public static final String SDK = "sdk";

    @Override
    public String getName() {
        return DEVICE_INFO_TABLE_NAME;
    }

    @Override
    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(new UniqueColumn(ANDROID_ID, Column.SQL_DATA_TYPE_CHAR_255));
        columns.add(new Column(BRAND, Column.SQL_DATA_TYPE_CHAR_255));
        columns.add(new Column(MODEL, Column.SQL_DATA_TYPE_CHAR_255));
        columns.add(new Column(BUILD_NUMBER, Column.SQL_DATA_TYPE_TEXT));
        columns.add(new Column(FIRMWARE_VERSION, Column.SQL_DATA_TYPE_CHAR_255));
        columns.add(new Column(SDK, Column.SQL_DATA_TYPE_INTEGER));
        return columns;
    }
}
