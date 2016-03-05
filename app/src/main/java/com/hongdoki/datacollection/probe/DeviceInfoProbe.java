package com.hongdoki.datacollection.probe;

import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.hongdoki.datacollection.database.DatabaseHelper;
import com.hongdoki.datacollection.database.DeviceInfoTable;

public class DeviceInfoProbe {
    private final Context context;
    private final DatabaseHelper databaseHelper;

    public DeviceInfoProbe(Context context) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void collect() {
        databaseHelper.createTable(new DeviceInfoTable());
        databaseHelper.getDatabase().insert(DeviceInfoTable.DEVICE_INFO_TABLE_NAME
        ,null, values());

    }

    private ContentValues values() {
        ContentValues values = new ContentValues();
        values.put(DeviceInfoTable.ANDROID_ID,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        values.put(DeviceInfoTable.BRAND, Build.BRAND);
        values.put(DeviceInfoTable.MODEL, Build.MODEL);
        values.put(DeviceInfoTable.BUILD_NUMBER,
                Build.PRODUCT + "-" + Build.TYPE
                + " " + Build.VERSION.RELEASE
                + " " + Build.ID
                + " " + Build.VERSION.INCREMENTAL
                + " " + Build.TAGS);
        values.put(DeviceInfoTable.FIRMWARE_VERSION, Build.VERSION.RELEASE);
        values.put(DeviceInfoTable.SDK, Build.VERSION.SDK_INT);
        return values;
    }
}
