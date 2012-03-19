package com.cthos.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class GearProvider extends ContentProvider
{
	public static final String AUTHORITY = "com.cthos.pfpt.core";
	
	private static final String TAG = "GearProvider";

    private static final String DATABASE_NAME = "gear.db";
    private static final String GEAR_TABLE_NAME = "gear";
    
    private static final int GEAR = 1;
    private static final int GEAR_ID = 2;
    
    private static HashMap<String, String> gearProjectionMap;
    
    private static final UriMatcher sUriMatcher;
    private DatabaseHelper mOpenHelper;
    
    @Override
	public boolean onCreate() {
    	mOpenHelper = new DatabaseHelper(getContext());
        return true;
	}
    
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case GEAR:
            count = db.delete(GEAR_TABLE_NAME, where, whereArgs);
            break;
        case GEAR_ID:
        	long id = ContentUris.parseId(uri);
        	String[] idAr = {String.valueOf(id)};
        	count = db.delete(GEAR_TABLE_NAME, "_ID = ?", idAr);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
        case GEAR:
        	return "vnd.android.cursor.dir/vnd.pfpt.gear";
        case GEAR_ID:
        	return "vnd.android.cursor.item/vnd.pfpt.gear";
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != GEAR) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(GEAR_TABLE_NAME, "character_id", values);
        if (rowId > 0) {
            Uri charUri = ContentUris.withAppendedId(
        		Uri.parse("content:com.cthos.pfpt.core.gearprovider/gear"),
        		rowId
            );
            getContext().getContentResolver().notifyChange(charUri, null);
            return charUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(GEAR_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
        case GEAR:
            qb.setProjectionMap(gearProjectionMap);
            break;
        case GEAR_ID:
        	selection = "_ID = ?";
        	long id = ContentUris.parseId(uri);
        	selectionArgs = new String[] {String.valueOf(id)};
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = "_id ASC";
        } else {
            orderBy = sortOrder;
        }

        // Get the database and run the query
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs)
	{
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case GEAR:
            count = db.update(GEAR_TABLE_NAME, values, where, whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}
	
	/**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, CharacterProvider.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + GEAR_TABLE_NAME + " ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "quantity VARCHAR(255) DEFAULT '0',"
                    + "character_id VARCHAR(255)"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + GEAR_TABLE_NAME);
            onCreate(db);
        }
    }
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI("com.cthos.pfpt.core.gearprovider", "gear", GEAR);
        sUriMatcher.addURI("com.cthos.pfpt.core.gearprovider", "gear/#", GEAR_ID);
        
        gearProjectionMap = new HashMap<String, String>();
        gearProjectionMap.put("_id", "_id");
        gearProjectionMap.put("name", "name");
        gearProjectionMap.put("quantity", "quantity");
        gearProjectionMap.put("character_id", "character_id");
    }
}
