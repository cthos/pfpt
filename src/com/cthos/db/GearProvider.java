package com.cthos.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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
        	count = db.delete(GEAR_TABLE_NAME, "_id = ?", idAr);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
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
        sUriMatcher.addURI("com.cthos.pfpt.core.slotteditemprovider", "gear/#", GEAR_ID);
        
        gearProjectionMap = new HashMap<String, String>();
        gearProjectionMap.put("_id", "_id");
        gearProjectionMap.put("name", "name");
        gearProjectionMap.put("quantity", "quantity");
        gearProjectionMap.put("character_id", "character_id");
    }
}
