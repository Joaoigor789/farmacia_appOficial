package com.example.farmacia_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_DEVICES = "devices";
    private static final String COLUMN_ID = "device_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEVICES_TABLE = "CREATE TABLE " + TABLE_DEVICES + "("
                + COLUMN_ID + " TEXT PRIMARY KEY)";
        db.execSQL(CREATE_DEVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        onCreate(db);
    }

    // Método para verificar se o dispositivo está registrado
    public boolean isDeviceRegistered(String deviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVICES, new String[]{COLUMN_ID}, COLUMN_ID + "=?",
                new String[]{deviceId}, null, null, null);
        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        return isRegistered;
    }

    // Método para registrar um dispositivo
    public void registerDevice(String deviceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + TABLE_DEVICES + " (" + COLUMN_ID + ") VALUES ('" + deviceId + "')";
        db.execSQL(insertQuery);
    }
}
