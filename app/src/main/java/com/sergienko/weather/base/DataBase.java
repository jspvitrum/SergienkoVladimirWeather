package com.sergienko.weather.base;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "weathers";
    public static final String CONTACTS_COLUMN_TEMP = "temp";
    public static final String CONTACTS_COLUMN_PRESSURE = "pressure";
    public static final String CONTACTS_COLUMN_HUMIDITY = "humidity";
    public static final String CONTACTS_COLUMN_DATE = "dt_txt";
    public static final String CONTACTS_COLUMN_ICON = "icon";
    public static final String CONTACTS_COLUMN_SPEED = "speed";


    public DataBase(Context context, String databaseName, Object o, int i) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table weathers " +
                        "(id integer primary key, temp text, pressure text, humidity text, dt_txt text, icon text, speed text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS weathers");
        onCreate(db);
    }

    public void onDelete() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CONTACTS_TABLE_NAME, null, null);
    }

    public boolean insertContact(String temp, String pressure, String humidity, String dt_txt, String icon, String speed) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("temp", temp);
        contentValues.put("pressure", pressure);
        contentValues.put("humidity", humidity);
        contentValues.put("dt_txt", dt_txt);
        contentValues.put("icon", icon);
        contentValues.put("speed", speed);

        db.insert("weathers", null, contentValues);
        return true;
    }


    public ArrayList getAllCotacts() {
        ArrayList<HashMap<String, String>> array_list = new ArrayList<HashMap<String, String>>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from weathers", null);

        res.moveToFirst();

        while (res.isAfterLast() == false) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("temp", res.getString(res.getColumnIndex(CONTACTS_COLUMN_TEMP)));
            hashMap.put("pressure", res.getString(res.getColumnIndex(CONTACTS_COLUMN_PRESSURE)));
            hashMap.put("humidity", res.getString(res.getColumnIndex(CONTACTS_COLUMN_HUMIDITY)));
            hashMap.put("dt_txt", res.getString(res.getColumnIndex(CONTACTS_COLUMN_DATE)));
            hashMap.put("icon", res.getString(res.getColumnIndex(CONTACTS_COLUMN_ICON)));
            hashMap.put("speed", res.getString(res.getColumnIndex(CONTACTS_COLUMN_SPEED)));
            array_list.add(hashMap);
            res.moveToNext();

        }
        return array_list;
    }
}
