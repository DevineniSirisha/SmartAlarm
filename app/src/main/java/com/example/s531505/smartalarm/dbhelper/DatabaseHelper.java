package com.example.s531505.smartalarm.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.s531505.smartalarm.dbhelper.model.SmartAlarm;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "smart_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create SmartAlarms table
        db.execSQL(SmartAlarm.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SmartAlarm.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertSmartAlarm(String alarmName, String alarmNotes, String destination, int radius, double lat, double lng) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SmartAlarm.COLUMN_ALARM_NAME, alarmName);
        values.put(SmartAlarm.COLUMN_ALARM_NOTES, alarmNotes);
        values.put(SmartAlarm.COLUMN_DESTINATION, destination);
        values.put(SmartAlarm.COLUMN_RADIUS, radius);
        values.put(SmartAlarm.COLUMN_LATITUDE, lat);
        values.put(SmartAlarm.COLUMN_LONGITUDE, lng);

        // insert row
        long id = db.insert(SmartAlarm.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public SmartAlarm getSmartAlarm(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SmartAlarm.TABLE_NAME,
                new String[]{SmartAlarm.COLUMN_ID, SmartAlarm.COLUMN_ALARM_NAME, SmartAlarm.COLUMN_ALARM_NOTES,
                        SmartAlarm.COLUMN_DESTINATION, SmartAlarm.COLUMN_LATITUDE, SmartAlarm.COLUMN_LONGITUDE,
                        SmartAlarm.COLUMN_RADIUS, SmartAlarm.COLUMN_TIMESTAMP},
                SmartAlarm.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        // prepare SmartAlarm object
        SmartAlarm smartAlarm = new SmartAlarm(
                cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NAME)),
                cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NOTES)),
                cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_RADIUS)),
                cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_DESTINATION)),
                cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return smartAlarm;
    }

    public List<SmartAlarm> getAllSmartAlarms() {
        List<SmartAlarm> smartAlarms = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SmartAlarm.TABLE_NAME + " ORDER BY " +
                SmartAlarm.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SmartAlarm SmartAlarm = new SmartAlarm();
                SmartAlarm.setId(cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_ID)));
                SmartAlarm.setName(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NAME)));
                SmartAlarm.setNotes(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NOTES)));
                SmartAlarm.setDestination(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_DESTINATION)));
                SmartAlarm.setLatitude(cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LATITUDE)));
                SmartAlarm.setLongitude(cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LONGITUDE)));
                SmartAlarm.setRadius(cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_RADIUS)));
                SmartAlarm.setTimestamp(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_TIMESTAMP)));

                smartAlarms.add(SmartAlarm);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return SmartAlarms list
        return smartAlarms;
    }


    public SmartAlarm getFirst() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SmartAlarm.TABLE_NAME + " ORDER BY " +
                SmartAlarm.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        SmartAlarm SmartAlarm = null;

        if (cursor.moveToFirst()) {
            SmartAlarm = new SmartAlarm();
            SmartAlarm.setId(cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_ID)));
            SmartAlarm.setName(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NAME)));
            SmartAlarm.setNotes(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_ALARM_NOTES)));
            SmartAlarm.setDestination(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_DESTINATION)));
            SmartAlarm.setLatitude(cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LATITUDE)));
            SmartAlarm.setLongitude(cursor.getDouble(cursor.getColumnIndex(SmartAlarm.COLUMN_LONGITUDE)));
            SmartAlarm.setRadius(cursor.getInt(cursor.getColumnIndex(SmartAlarm.COLUMN_RADIUS)));
            SmartAlarm.setTimestamp(cursor.getString(cursor.getColumnIndex(SmartAlarm.COLUMN_TIMESTAMP)));
        }

        // close db connection
        db.close();

        return SmartAlarm;
    }

    public int getSmartAlarmsCount() {
        String countQuery = "SELECT  * FROM " + SmartAlarm.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateSmartAlarm(SmartAlarm smartAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SmartAlarm.COLUMN_ALARM_NAME, smartAlarm.getName());
        values.put(SmartAlarm.COLUMN_ALARM_NOTES, smartAlarm.getNotes());
        values.put(SmartAlarm.COLUMN_DESTINATION, smartAlarm.getDestination());
        values.put(SmartAlarm.COLUMN_RADIUS, smartAlarm.getRadius());
        values.put(SmartAlarm.COLUMN_LATITUDE, smartAlarm.getLatitude());
        values.put(SmartAlarm.COLUMN_LONGITUDE, smartAlarm.getLongitude());

        // updating row
        return db.update(SmartAlarm.TABLE_NAME, values, SmartAlarm.COLUMN_ID + " = ?",
                new String[]{String.valueOf(smartAlarm.getId())});
    }

    public void deleteSmartAlarm(SmartAlarm smartAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SmartAlarm.TABLE_NAME, SmartAlarm.COLUMN_ID + " = ?",
                new String[]{String.valueOf(smartAlarm.getId())});
        db.close();
    }
}
