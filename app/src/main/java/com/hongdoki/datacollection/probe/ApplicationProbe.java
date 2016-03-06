package com.hongdoki.datacollection.probe;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.hongdoki.datacollection.database.ApplicationTable;
import com.hongdoki.datacollection.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationProbe {
    private final Context context;
    private final PackageManager packageManager;
    private final DatabaseHelper databaseHelper;

    public ApplicationProbe(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void collect() {
        createTable();
        insertApplications();
    }
    private void createTable() {
        databaseHelper.createTable(new ApplicationTable());
    }

    private void insertApplications() {
        List<ApplicationInfo> allApplications = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<ApplicationInfo> installedApplications = new ArrayList<>(packageManager.getInstalledApplications(0));
        List<ApplicationInfo> uninstalledApplications = getUninstalledApps(allApplications, installedApplications);

        for (ApplicationInfo info : installedApplications) {
            databaseHelper.getDatabase().insertWithOnConflict(ApplicationTable.APPLICATION_TABLE_NAME,
                    null, values(info, false), SQLiteDatabase.CONFLICT_IGNORE);
        }
        for (ApplicationInfo info : uninstalledApplications) {
            databaseHelper.getDatabase().insertWithOnConflict(ApplicationTable.APPLICATION_TABLE_NAME,
                    null, values(info, true), SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    private static ArrayList<ApplicationInfo> getUninstalledApps(List<ApplicationInfo> allApplications, List<ApplicationInfo> installedApps) {
        Set<String> installedAppPackageNames = getInstalledAppPackageNames(installedApps);
        ArrayList<ApplicationInfo> uninstalledApps = new ArrayList<>();
        for (ApplicationInfo info : allApplications) {
            if (!installedAppPackageNames.contains(info.packageName)) {
                uninstalledApps.add(info);
            }
        }
        return uninstalledApps;
    }

    private static Set<String> getInstalledAppPackageNames(List<ApplicationInfo> installedApps) {
        HashSet<String> installedAppPackageNames = new HashSet<>();
        for (ApplicationInfo info : installedApps) {
            installedAppPackageNames.add(info.packageName);
        }
        return installedAppPackageNames;
    }

    private ContentValues values(ApplicationInfo info, boolean deleted) {
        ContentValues values = new ContentValues();
        values.put(ApplicationTable.PACKAGE_NAME, info.packageName);
        values.put(ApplicationTable.PROCESS_NAME, info.processName);
        values.put(ApplicationTable.TARGET_SDK, info.targetSdkVersion);
        values.put(ApplicationTable.TASK_AFFINITY, info.taskAffinity);
        values.put(ApplicationTable.UNINSTALLED, deleted ? 1 : 0);
        return values;
    }
}
