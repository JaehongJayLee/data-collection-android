package com.hongdoki.datacollection.database;

import java.util.ArrayList;
import java.util.List;

public class NetworkTrafficTable extends Table{
    public static final String NETWORK_TRAFFIC_TABLE_NAME = "network_traffic";
    public static final String SESSION_ID = "session_id";
    public static final String TIMESTAMP = "timestamp";
    public static final String PACKAGE_NAME = "package_name";
    public static final String TRANSMITTED = "transmitted";
    public static final String RECEIVED= "received";

    @Override
    public String getName() {
        return NETWORK_TRAFFIC_TABLE_NAME;
    }

    @Override
    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(TIMESTAMP, Column.SQL_DATA_TYPE_TIMESTAMP));
        columns.add(new Column(SESSION_ID, Column.SQL_DATA_TYPE_INTEGER));
        columns.add(new Column(PACKAGE_NAME, Column.SQL_DATA_TYPE_TEXT));
        columns.add(new Column(TRANSMITTED, Column.SQL_DATA_TYPE_BIGINT));
        columns.add(new Column(RECEIVED, Column.SQL_DATA_TYPE_BIGINT));
        return columns;
    }
}
