package com.stay4it.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stay4it.db.annotation.Column;
import com.stay4it.db.annotation.Table;
import com.stay4it.db.core.DBUtil;
import com.stay4it.db.utilities.SerializeUtil;
import com.stay4it.db.utilities.TextUtil;
import com.stay4it.db.utilities.Trace;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stay
 * @version create time：Nov 10, 2014 1:19:35 PM
 */
public class DBManager1 {
    private static DBManager1 mInstance;
    private static SQLiteOpenHelper mHelper;
    private static SQLiteDatabase mDatabase;
    private final Context context;

    private DBManager1(Context context, SQLiteOpenHelper helper) {
        this.context = context;
        mHelper = helper;
        mDatabase = mHelper.getWritableDatabase();
    }

    public static void init(Context context, SQLiteOpenHelper helper) {
        if (mInstance == null) {
            mInstance = new DBManager1(context, helper);
        }
    }

    public static DBManager1 getInstance() {
        return mInstance;
    }


    public void release() {
        mDatabase.close();
        mInstance = null;
    }

    // TODO crud
    public <T> void newOrUpdate(T t) {
        if (t.getClass().isAnnotationPresent(Table.class)) {
            Field[] fields = t.getClass().getDeclaredFields();
            ContentValues values = new ContentValues();
            try {
                Field idField = t.getClass().getDeclaredField(DBUtil.getIDColumnName(t.getClass()));
                idField.setAccessible(true);
                String idValue = (String) idField.get(t);
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        Class<?> clz = field.getType();
                        if (clz == String.class) {
                            Object value = field.get(t);
                            if (value != null) {
                                values.put(DBUtil.getColumnName(field), value.toString());
                            }
                        } else if (clz == int.class || clz == Integer.class) {
                            values.put(DBUtil.getColumnName(field), field.getInt(t));
                        } else {
                            Column column = field.getAnnotation(Column.class);
                            Column.ColumnType type = column.type();
                            if (!TextUtil.isValidate(type.name())) {
                                throw new IllegalArgumentException("you should set type to the special column:" + t.getClass().getSimpleName() + "."
                                        + field.getName());
                            }
                            if (type == Column.ColumnType.SERIALIZABLE) {
                                byte[] value = SerializeUtil.serialize(field.get(t));
                                values.put(DBUtil.getColumnName(field), value);
                            } else if (type == Column.ColumnType.TONE) {
                                Object tone = field.get(t);
                                if (tone == null) {
                                    continue;
                                }
                                if (column.autofresh()) {
//									TODO save object to related table
                                    newOrUpdate(tone);
                                }
                                if (tone.getClass().isAnnotationPresent(Table.class)) {
                                    String idName = DBUtil.getIDColumnName(tone.getClass());
                                    Field toneIdField = tone.getClass().getDeclaredField(idName);
                                    toneIdField.setAccessible(true);
                                    values.put(DBUtil.getColumnName(field), toneIdField.get(tone).toString());
                                }
                            } else if (type == Column.ColumnType.TMANY) {
                                List<Object> tmany = (List<Object>) field.get(t);
                                mDatabase.delete(DBUtil.getAssociationTableName(t.getClass(), field.getName()), "pk1=?", new String[]{idValue});
                                if (tmany != null) {
                                    ContentValues associationValues = new ContentValues();
                                    for (Object object : tmany) {
                                        if (column.autofresh()) {
                                            newOrUpdate(object);
                                        }
                                        associationValues.clear();
                                        associationValues.put(DBUtil.PK1, idValue);// company id
                                        String idName = DBUtil.getIDColumnName(object.getClass());
                                        Field tmanyIdField = object.getClass().getDeclaredField(idName);
                                        tmanyIdField.setAccessible(true);
                                        String value = tmanyIdField.get(object).toString();
                                        associationValues.put(DBUtil.PK2, value); // developer id
                                        mDatabase.replace(DBUtil.getAssociationTableName(t.getClass(), field.getName()), null, associationValues);
//										1,2    3,-1    2,3
                                    }
                                }
                            }

                            // else if (condition) {
                            //
                            // }
                            // TODO tone serializable
                            // TODO TONE get the po's id
                            // TODO Serializable serialize the object
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDatabase.replace(DBUtil.getTableName(t.getClass()), null, values);
        }
    }

    // public void newOrUpdate(Developer developer) {
    // ContentValues values = new ContentValues();
    // values.put(Developer._ID, developer.getId());
    // values.put(Developer._NAME, developer.getName());
    // values.put(Developer._AGE, developer.getAge());
    // values.put(Developer._COMPANY, developer.getCompany().getId());
    // // TODO
    // // values.put(Developer._SKILLS, developer.getSkills());
    // mDatabase.replace(DBUtil.getTableName(developer.getClass()), null,
    // values);
    // }
    //
    // public void newOrUpdate(Company company) {
    //
    // }

    // public void delete(Developer developer) {
    // mDatabase.delete(DBUtil.getTableName(developer.getClass()), Developer._ID
    // + "=?", new String[] { developer.getId() });
    // }

    public <T> void delete(T t) {
        try {
            String idName = DBUtil.getIDColumnName(t.getClass());
            Field field = t.getClass().getDeclaredField(idName);
            field.setAccessible(true);
            String id = (String) field.get(t);
            mDatabase.delete(DBUtil.getTableName(t.getClass()), idName + "=?", new String[]{id});
//			TODO delete related association data
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T queryById(Class<T> clz, String id) {
        Cursor cursor = mDatabase.rawQuery("select * from " + DBUtil.getTableName(clz) + " where " + DBUtil.getIDColumnName(clz) + "=?",
                new String[]{id});
        T t = null;
        if (cursor.moveToNext()) {
            try {
                t = clz.newInstance();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        Class<?> columnType = field.getType();
                        if (columnType == Integer.class || columnType == int.class) {
                            field.setInt(t, cursor.getInt(cursor.getColumnIndex(DBUtil.getColumnName(field))));
                        } else if (columnType == String.class) {
                            field.set(t, cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field))));
                        } else {
                            Column column = field.getAnnotation(Column.class);
                            Column.ColumnType type = column.type();
                            if (!TextUtil.isValidate(type.name())) {
                                throw new IllegalArgumentException("you should set type to the special column:" + t.getClass().getSimpleName() + "."
                                        + field.getName());
                            }
                            if (type == Column.ColumnType.SERIALIZABLE) {
                                field.set(t, SerializeUtil.deserialize(cursor.getBlob(cursor.getColumnIndex(DBUtil.getColumnName(field)))));
//								field.set(t, JsonUtil.fromJson(cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field))),field.getType()));
                            } else if (type == Column.ColumnType.TONE) {
                                String toneId = cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field)));
                                if (!TextUtil.isValidate(toneId)) {
                                    continue;
                                }
                                Trace.d("query -- tone.id:" + toneId);
                                Object tone = null;
                                if (column.autofresh()) {
                                    tone = queryById(field.getType(), toneId);
                                } else {
                                    tone = field.getType().newInstance();
                                    if (field.getType().isAnnotationPresent(Table.class)) {
                                        String idName = DBUtil.getIDColumnName(field.getType());
                                        Field idField = field.getType().getDeclaredField(idName);
                                        idField.setAccessible(true);
                                        idField.set(tone, toneId);
                                    }
                                }
                                field.set(t, tone);
                            } else if (type == Column.ColumnType.TMANY) {
                                Class relatedClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                                Cursor tmanyCursor = mDatabase.rawQuery("select * from " + DBUtil.getAssociationTableName(clz, field.getName()) + " where " + DBUtil.PK1 + "=?", new String[]{id});
                                ArrayList list = new ArrayList();
                                String tmanyId = null;
                                Object tmany = null;
                                while (tmanyCursor.moveToNext()) {
                                    tmanyId = tmanyCursor.getString(tmanyCursor.getColumnIndex(DBUtil.PK2));
                                    if (column.autofresh()) {
                                        tmany = queryById(relatedClass, tmanyId);
                                    } else {
                                        tmany = relatedClass.newInstance();
                                        String idName = DBUtil.getIDColumnName(relatedClass);
                                        Field idField = relatedClass.getDeclaredField(idName);
                                        idField.setAccessible(true);
                                        idField.set(tmany, tmanyId);
                                    }
                                    list.add(tmany);
                                }
                                if (!TextUtil.isValidate(list)) {
                                    continue;
                                }
                                field.set(t, list);
                            }
                        }
                        // TODO TONE Serializable
                        // TONE columnType.newInstance() -- set ID -- set to T
                        // (lazy load)
                        // Serializable -- deserialize to object -- set to T
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    // public Developer queryById(String id) {
    // Cursor cursor = mDatabase.rawQuery("select * from " +
    // DBUtil.getTableName(Developer.class) + " where " + Developer._ID + "=?",
    // new String[] { id });
    // Developer developer = null;
    // if (cursor.moveToNext()) {
    // developer = new Developer();
    // developer.setId(cursor.getString(cursor.getColumnIndex(Developer._ID)));
    // developer.setName(cursor.getString(cursor.getColumnIndex(Developer._NAME)));
    // // developer.setCompany(new
    // // Company(cursor.getString(cursor.getColumnIndex(Developer._ID))));
    // developer.setAge(cursor.getInt(cursor.getColumnIndex(Developer._AGE)));
    // // TODO skills
    // }
    // return developer;
    // }

}
