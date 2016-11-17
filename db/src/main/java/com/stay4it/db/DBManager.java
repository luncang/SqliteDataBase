package com.stay4it.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stay4it.db.core.BaseDao;

import java.util.HashMap;

/**
 * @author Stay
 * @version create time：Nov 10, 2014 1:19:35 PM
 */
public class DBManager {
    private static DBManager mInstance;
    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    private HashMap<String, BaseDao> mCachedDaos;
    private Context context;

    private DBManager(Context context, SQLiteOpenHelper helper) {
        this.context = context;
        mHelper = helper;
        mDatabase = mHelper.getWritableDatabase();
        mCachedDaos = new HashMap<>();
    }

    public static void init(Context context, SQLiteOpenHelper helper) {
        if (mInstance == null) {
            mInstance = new DBManager(context, helper);
        }
    }

    public static DBManager getInstance() {
        return mInstance;
    }

    public void release() {
        mDatabase.close();
        mInstance = null;
    }

    public <T> BaseDao<T> getDao(Class<T> clz) {
        if (mCachedDaos.containsKey(clz.getSimpleName())) {
            return mCachedDaos.get(clz.getSimpleName());
        } else {
            BaseDao<T> dao = new BaseDao<T>(context, clz, mDatabase);
            mCachedDaos.put(clz.getSimpleName(), dao);
            return dao;
        }
    }


}
