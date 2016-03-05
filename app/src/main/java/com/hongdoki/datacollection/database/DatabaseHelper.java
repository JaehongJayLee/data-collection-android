package com.hongdoki.datacollection.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import com.hongdoki.datacollection.util.StringUtil;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME_DEFAULT = "data_%s";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS %s (_id INTEGER primary key autoincrement, %s);";
    private static DatabaseHelper instance;
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, databaseName(context), null, DATABASE_VERSION);
        try {
            db = getWritableDatabase();
        } catch (SQLiteException ex) {
            db = getReadableDatabase();
        }
        createTable(new SensorAndSettingTable());
    }

    public static String databaseName(Context context) {
        return String.format(DATABASE_NAME_DEFAULT, androidId(context));
    }

    private static String androidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void createTable(Table table) {
        db.execSQL(createTableSQL(table.getName(), table.getColumns()));
    }

    private String createTableSQL(String tableName, List<Column> columns) {
        return String.format(CREATE_TABLE_FORMAT, tableName, StringUtil.join(columns, ", "));
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

    public boolean isTableExist(String tableName) {
        Cursor cursor = getDatabase().rawQuery("select DISTINCT tbl_name " +
                "from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void dropAndRecreateTables(Table[] tables) {
        for(Table table: tables){
            dropAndRecreateTable(table);
        }
    }

    private void dropAndRecreateTable(Table table) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + table.getName() + ";");
            createTable(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
