package com.cthos.pfpt;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LoadCharacter extends ListActivity
{
	 private static final String[] PROJECTION = new String[] {
         "_id",
         "name",
	 };
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
		 
		 Intent intent = getIntent();
	        if (intent.getData() == null) {
	            intent.setData(Uri.parse("content://com.cthos.pfpt.core/character"));
	        }
		 
		 Cursor cursor = managedQuery(
			intent.getData(), 
		 	PROJECTION,
		 	null,
		 	null,
		    "_id ASC"
	    );

        // Used to map notes entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.load_character_item, cursor,
                new String[] { "name" }, new int[] { android.R.id.text1 });
        setListAdapter(adapter);
	 }
	 
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		 new AlertDialog.Builder(v.getContext())
              .setMessage("Next Week: This'll do something.")
              .show();

	}
}
