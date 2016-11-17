package com.stay4it;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stay4it.db.core.DBUtil;
import com.stay4it.model.Company;
import com.stay4it.model.Developer;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "stay4it.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBUtil.createTable(db, Developer.class);
        DBUtil.createTable(db, Company.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DBUtil.dropTable(db, Developer.class);
        DBUtil.dropTable(db, Company.class);
    }

}