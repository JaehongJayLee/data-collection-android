package com.hongdoki.datacollection.database;

public class Column {
    public static final String SQL_DATA_TYPE_CHAR_255 = "CHAR(255)";
    public static final String SQL_DATA_TYPE_REAL = "REAL";
    public static final String SQL_DATA_TYPE_TINYINT = "TINYINT";
    public static final String SQL_DATA_TYPE_INTEGER = "INTEGER";
    public static final String SQL_DATA_TYPE_TEXT = "TEXT";
    public static final String SQL_DATA_BIT = "BIT";
    public static final String SQL_DATA_TYPE_TIMESTAMP = "TIMESTAMP";
    public static final String SQL_DATA_TYPE_BIGINT = "BIGINT";
    public final String name, type;

    public Column(final String name, final String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name + " " + type;
    }
}
