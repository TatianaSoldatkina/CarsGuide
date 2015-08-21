package com.carsguide.rest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.carsguide.rest.contract.CarContract;


public class ProviderDbHelper extends SQLiteOpenHelper {

    public final String TAG = getClass().getSimpleName();

    //Name of the database file
    private static final String DATABASE_NAME = "carsguide.db";
    private static final int DATABASE_VERSION = 5;

    public ProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE USERS TABLE
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + CarContract.TABLE_NAME + " (");
        sqlBuilder.append(CarContract.ID + " INTEGER, ");
        sqlBuilder.append(CarContract.CAR_TITLE + " TEXT, ");
        sqlBuilder.append(CarContract.CAR_IMAGE_URL + " TEXT, ");
        sqlBuilder.append(CarContract.IS_FAVORITE + " INTEGER, ");
        sqlBuilder.append(CarContract.CAR_URL + " TEXT");

        sqlBuilder.append(");");
        String sql = sqlBuilder.toString();
        Log.i(TAG, "Creating DB table with string: '" + sql + "'");
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CarContract.TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllRecordsFromTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }

}
