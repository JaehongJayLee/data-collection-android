package com.hongdoki.datacollection.database;

import java.util.ArrayList;
import java.util.List;

public class ApplicationTable extends Table{
    public static final String APPLICATION_TABLE_NAME = "application";
    public static final String PACKAGE_NAME = "package_name";
    public static final String PROCESS_NAME = "process_name";
    public static final String TASK_AFFINITY = "task_affinity";
    public static final String TARGET_SDK = "target_sdk";
    public static final String UNINSTALLED = "uninstalled";

    @Override
    public String getName() {
        return APPLICATION_TABLE_NAME;
    }

    @Override
    public List<Column> getColumns() {
        List<Column> columns = new ArrayList<>();
        columns.add(new UniqueColumn(PACKAGE_NAME, Column.SQL_DATA_TYPE_TEXT));
        columns.add(new Column(PROCESS_NAME, Column.SQL_DATA_TYPE_TEXT));
        columns.add(new Column(TASK_AFFINITY, Column.SQL_DATA_TYPE_TEXT));
        columns.add(new Column(TARGET_SDK, Column.SQL_DATA_TYPE_TINYINT));
        columns.add(new Column(UNINSTALLED, Column.SQL_DATA_BIT));
        return columns;
    }

}
