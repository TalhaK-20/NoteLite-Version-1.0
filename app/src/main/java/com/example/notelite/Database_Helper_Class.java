package com.example.notelite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Helper_Class extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NoteLite.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SECURITY_KEY = "security_key";

    public Database_Helper_Class(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SECURITY_KEY + " TEXT)";
        db.execSQL(createTable);

        // Insert a default user
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, "1111111111111");
        contentValues.put(COLUMN_NAME, "abc");
        contentValues.put(COLUMN_SECURITY_KEY, "11");
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean validateUserName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NAME},
                COLUMN_NAME + "=?",
                new String[]{name}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean validateUserID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{id}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean validateUserSecurityKey(String securityKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_SECURITY_KEY},
                COLUMN_SECURITY_KEY + "=?",
                new String[]{securityKey}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public boolean validateUser(String name, String id, String securityKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID},
                COLUMN_NAME + "=? AND " + COLUMN_ID + "=? AND " + COLUMN_SECURITY_KEY + "=?",
                new String[]{name, id, securityKey}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
