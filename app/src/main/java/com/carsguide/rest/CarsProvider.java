package com.carsguide.rest;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.carsguide.rest.contract.CarContract;

public class CarsProvider extends ContentProvider {

    private static final int CARS = 1;
    private static final int CAR_ID = 2;

    /**
     * The MIME type of a directory of events
     */
    private static final String CONTENT_TYPE = "carsguide.android.cursor.dir/carsguide.car";

    /**
     * The MIME type of a single event
     */
    private static final String CONTENT_ITEM_TYPE = "carsguide.android.cursor.item/carsguide.shot";

    private ProviderDbHelper dbHelper;
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CarContract.AUTHORITY, CarContract.TABLE_NAME, CARS);
        uriMatcher.addURI(CarContract.AUTHORITY, CarContract.TABLE_NAME + "/#", CAR_ID);
        this.dbHelper = new ProviderDbHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CAR_ID:
                long id = Long.parseLong(uri.getPathSegments().get(1));
                selection = appendRowId(selection, id);
                cursor = db.query(CarContract.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                cursor = db.query(CarContract.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
        }


        // Tell the cursor what uri to watch, so it knows when its
        // source data changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CARS:
                return CONTENT_TYPE;
            case CAR_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Validate the requested uri
        if (uriMatcher.match(uri) != CARS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Insert into database
        long id = db.insertOrThrow(CarContract.TABLE_NAME, null, values);

        // Notify any watchers of the change
        Uri newUri = ContentUris.withAppendedId(CarContract.CONTENT_URI, id);

        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Validate the requested uri
        if (uriMatcher.match(uri) != CARS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int rowsDeleted = db.delete(CarContract.TABLE_NAME,
                selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Validate the requested uri
        if (uriMatcher.match(uri) != CAR_ID) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String recordId = Long.toString(ContentUris.parseId(uri));
        int affected = db.update(CarContract.TABLE_NAME, values, CarContract.ID
                + "="
                + recordId
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : ""), selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    /**
     * Append an id test to a SQL selection expression
     */
    private String appendRowId(String selection, long id) {
        return CarContract.ID
                + "="
                + id
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : "");
    }
}