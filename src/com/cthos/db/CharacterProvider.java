package com.cthos.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CharacterProvider extends ContentProvider
{
	public static final String AUTHORITY = "com.cthos.pfpt.core";
	
	private static final String TAG = "CharacterProvider";

    private static final String DATABASE_NAME = "character.db";
    // Store this somewhere in a registry or something.
    private static final int DATABASE_VERSION = 3;
    private static final String CHARACTER_TABLE_NAME = "characters";
    private static final String CLASS_TABLE_NAME = "character_classes";
    private static final String DERIVED_TRAITS_NAME = "character_dt";
    
    private static HashMap<String, String> characterProjectionMap;
    
    private static final int CHARACTER = 1;
    private static final int CHARACTER_ID = 2;
    
    private static final UriMatcher sUriMatcher;

    /**
     * This class helps open, create, and upgrade the database file.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + CHARACTER_TABLE_NAME + " ("
                    + "_id INTEGER PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "ac VARCHAR(10) DEFAULT '',"
                    + "level INTEGER DEFAULT 1,"
                    + "strength INTEGER DEFAULT 8,"
                    + "dexterity INTEGER DEFAULT 8,"
                    + "constitution INTEGER DEFAULT 8,"
                    + "intelligence INTEGER DEFAULT 8,"
                    + "wisdom INTEGER DEFAULT 8,"
                    + "charisma INTEGER DEFAULT 8"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + CHARACTER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DERIVED_TRAITS_NAME);
            onCreate(db);
        }
    }
    
    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CHARACTER_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
        case CHARACTER:
            qb.setProjectionMap(characterProjectionMap);
            break;
        case CHARACTER_ID:
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
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
        case CHARACTER:
        	return "vnd.android.cursor.dir/vnd.pfpt.character";

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != CHARACTER) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(CHARACTER_TABLE_NAME, "character", values);
        if (rowId > 0) {
            Uri charUri = ContentUris.withAppendedId(com.cthos.pfpt.core.Character.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(charUri, null);
            return charUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case CHARACTER:
            count = db.delete(CHARACTER_TABLE_NAME, where, whereArgs);
            break;
        case CHARACTER_ID:
        	long id = ContentUris.parseId(uri);
        	String[] idAr = {String.valueOf(id)};
        	count = db.delete(CHARACTER_TABLE_NAME, "_ID = ?", idAr);
        	break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
        case CHARACTER:
            count = db.update(CHARACTER_TABLE_NAME, values, where, whereArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI("com.cthos.pfpt.core", "character", CHARACTER);
        sUriMatcher.addURI("com.cthos.pfpt.core", "character/#", CHARACTER_ID);
        
        characterProjectionMap = new HashMap<String, String>();
        characterProjectionMap.put("_id", "_id");
        characterProjectionMap.put("name", "name");
        characterProjectionMap.put("level", "level");
        characterProjectionMap.put("ac", "ac");
        characterProjectionMap.put("strength", "strength");
        characterProjectionMap.put("dexterity", "dexterity");
        characterProjectionMap.put("constitution", "constitution");
        characterProjectionMap.put("wisdom", "wisdom");
        characterProjectionMap.put("intelligence", "intelligence");
        characterProjectionMap.put("charisma", "charisma");
    }
}
