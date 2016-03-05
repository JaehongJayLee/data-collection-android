package com.hongdoki.datacollection.probe;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

import com.google.gson.Gson;
import com.hongdoki.datacollection.database.DatabaseHelper;
import com.hongdoki.datacollection.database.NetworkTrafficTable;
import com.hongdoki.datacollection.helper.SharedPreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NetworkTrafficProbe {
    private static final String TOTAL_NETWORK_SUM = "TOTAL_NETWORK_SUM";
    private static final String MOBILE_NETWORK_SUM = "MOBILE_NETWORK_SUM";
    private final Context context;
    private final SharedPreferenceHelper sharedPreferenceHelper;
    private final DatabaseHelper databaseHelper;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String INITIAL_STATE_OR_INVALID_TRAFFIC = "INITIAL_STATE_OR_INVALID_TRAFFIC";

    public NetworkTrafficProbe(Context context) {
        this.context = context;
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(context);
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public void storeTraffic() {
        createTable();
        TrafficStatsDummy trafficStatsCurrent = snapTrafficStatsCurrent();
        TrafficStatsDummy trafficStatsPast = getTrafficStatsPast();
        if (trafficStatsPast != null) {
            storeTrafficBetween(trafficStatsPast, trafficStatsCurrent);
        }else{
            checkInitialPoint();
        }
        commitTrafficStatsPast(trafficStatsCurrent);
    }

    private void createTable() {
        databaseHelper.createTable(new NetworkTrafficTable());
    }

    private TrafficStatsDummy snapTrafficStatsCurrent() {
        TrafficStatsDummy currentTrafficStats = new TrafficStatsDummy();
        currentTrafficStats.totalRxBytes = TrafficStats.getTotalRxBytes();
        currentTrafficStats.totalTxBytes = TrafficStats.getTotalTxBytes();
        currentTrafficStats.mobileRxBytes = TrafficStats.getMobileRxBytes();
        currentTrafficStats.mobileTxBytes = TrafficStats.getMobileTxBytes();

        for (ApplicationInfo applicationInfo : ApplicationInfoInstalled()) {
            currentTrafficStats.applicationUIdAndPackageName.put(applicationInfo.uid,
                    applicationInfo.packageName);
            currentTrafficStats.applicationRxBytes.put(applicationInfo.uid,
                    TrafficStats.getUidRxBytes(applicationInfo.uid));
            currentTrafficStats.applicationTxBytes.put(applicationInfo.uid,
                    TrafficStats.getUidTxBytes(applicationInfo.uid));
        }

        return currentTrafficStats;
    }

    private TrafficStatsDummy getTrafficStatsPast() {
        String json = sharedPreferenceHelper.getTrafficStatsPastJson();
        if (json.equals("")) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(json, TrafficStatsDummy.class);
        }
    }

    private List<ApplicationInfo> ApplicationInfoInstalled() {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    private void storeTrafficBetween(TrafficStatsDummy past, TrafficStatsDummy current) {
        List<Traffic> trafficList = trafficList(past, current);
        String timeStamp = dateFormat.format(new Date());
        int sessionId = sharedPreferenceHelper.getAndIncrementTrafficSessionId();
        if (isValidTrafficDataList(trafficList)) {
            for (Traffic traffic : trafficList) {
                insertTraffic(sessionId, timeStamp, traffic);
            }
        }else{
            insertTraffic(sessionId, timeStamp, initialOrInvalidTraffic());
        }
    }

    private ArrayList<Traffic> trafficList(TrafficStatsDummy past,
                                           TrafficStatsDummy current) {
        ArrayList<Traffic> trafficList = new ArrayList<>();

        trafficList.add(totalTraffic(past, current));
        trafficList.add(mobileTraffic(past, current));

        for (int uid : current.applicationUIdAndPackageName.keySet()) {
            Traffic applicationTraffic = getApplicationTrafficWithUId(uid, current, past);
            if (applicationTraffic != null) {
                trafficList.add(applicationTraffic);
            }
        }

        return trafficList;
    }

    private Traffic totalTraffic(TrafficStatsDummy past, TrafficStatsDummy current) {
        Traffic traffic = new Traffic();
        traffic.packageName = TOTAL_NETWORK_SUM;
        traffic.received = current.totalRxBytes - past.totalRxBytes;
        traffic.transmitted = current.totalTxBytes - past.totalTxBytes;
        return traffic;
    }

    private Traffic mobileTraffic(TrafficStatsDummy past, TrafficStatsDummy current) {
        Traffic traffic = new Traffic();
        traffic.packageName = MOBILE_NETWORK_SUM;
        traffic.received = current.mobileRxBytes - past.mobileRxBytes;
        traffic.transmitted = current.mobileTxBytes - past.mobileTxBytes;
        return traffic;
    }

    private Traffic getApplicationTrafficWithUId(int uid, TrafficStatsDummy current, TrafficStatsDummy past) {
        if (!past.applicationUIdAndPackageName.keySet().contains(uid)) {
            return null;
        } else {
            long trafficRx = current.applicationRxBytes.get(uid) - past.applicationRxBytes.get(uid);
            long trafficTx = current.applicationTxBytes.get(uid) - past.applicationTxBytes.get(uid);
            if (trafficRx == 0 && trafficTx == 0) {
                return null;
            } else {
                Traffic applicationTraffic = new Traffic();
                applicationTraffic.packageName = current.applicationUIdAndPackageName.get(uid);
                applicationTraffic.received = trafficRx;
                applicationTraffic.transmitted = trafficTx;
                return applicationTraffic;
            }
        }
    }

    private boolean isValidTrafficDataList(List<Traffic> trafficList) {
        if (trafficList == null) {
            return false;
        } else {
            for (Traffic traffic : trafficList) {
                if (traffic.received < 0 || traffic.transmitted < 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private void insertTraffic(int sessionId, String timeStamp, Traffic traffic) {
        databaseHelper.getDatabase().insert(NetworkTrafficTable.NETWORK_TRAFFIC_TABLE_NAME
                , null, values(sessionId, timeStamp, traffic));
    }

    private ContentValues values(int sessionId, String timeStamp, Traffic traffic) {
        ContentValues values =  new ContentValues();
        values.put(NetworkTrafficTable.TIMESTAMP, timeStamp);
        values.put(NetworkTrafficTable.SESSION_ID, sessionId);
        values.put(NetworkTrafficTable.PACKAGE_NAME, traffic.packageName);
        values.put(NetworkTrafficTable.TRANSMITTED, traffic.transmitted);
        values.put(NetworkTrafficTable.RECEIVED, traffic.received);
        return values;
    }

    private Traffic initialOrInvalidTraffic() {
        Traffic traffic = new Traffic();
        traffic.packageName = INITIAL_STATE_OR_INVALID_TRAFFIC;
        return traffic;
    }

    private void checkInitialPoint() {
        insertTraffic(sharedPreferenceHelper.getAndIncrementTrafficSessionId(),
                dateFormat.format(new Date()), initialOrInvalidTraffic());
    }

    private void commitTrafficStatsPast(TrafficStatsDummy trafficStatsCurrent) {
        Gson gson = new Gson();
        String json = gson.toJson(trafficStatsCurrent);
        sharedPreferenceHelper.commitTrafficStatsPastJson(json);
    }

    private class TrafficStatsDummy {

        public long mobileRxBytes;
        public long mobileTxBytes;
        public long totalRxBytes;
        public long totalTxBytes;
        public HashMap<Integer, String> applicationUIdAndPackageName;
        public HashMap<Integer, Long> applicationRxBytes;
        public HashMap<Integer, Long> applicationTxBytes;

        public TrafficStatsDummy() {
            applicationUIdAndPackageName = new HashMap<>();
            applicationRxBytes = new HashMap<>();
            applicationTxBytes = new HashMap<>();
        }
    }

    private class Traffic {
        String packageName;
        long received;
        long transmitted;
    }
}
