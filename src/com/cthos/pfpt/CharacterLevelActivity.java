package com.cthos.pfpt;

import java.util.ArrayList;

import com.cthos.pfpt.equipment.SlottedItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class CharacterLevelActivity extends ListActivity
{
	private final int MENU_ITEM_ADD_CLASS_LEVELS = 1;
	
	private final int ADD_CLASS_LEVEL_DIALOG = 1;
	
	protected long characterId;
	
	protected String className;
	
	protected String numLevels;
	protected SimpleCursorAdapter adpt;
	
	protected ContentValues val;
	protected ContentResolver cr;
	
	protected Cursor classLevelCursor;
	
	private static final String[] PROJECTION = new String[] {
        "_id",
        "class_name",
        "num_levels"
	};
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		characterId = getIntent().getLongExtra("characterId", 0);
		
		classLevelCursor = managedQuery(
		Uri.parse("content://com.cthos.pfpt.core.characterclasslevelprovider/character_class_level"),
			PROJECTION,
		 	"character_id = ?",
		 	new String[]{String.valueOf(characterId)},
		    null
	    );
		
		startManagingCursor(classLevelCursor);
		
		// TODO: Loading data on the UI thread is probably not ideal.
		adpt = new SimpleCursorAdapter(this, R.layout.slotted_item_item_layout, classLevelCursor,
                new String[] { "class_name", "num_levels" }, new int[] {R.id.slotted_item_list_item_name, R.id.slotted_item_list_location});
        setListAdapter(adpt);
		
		registerForContextMenu(getListView());
	}
	
	protected Dialog onCreateDialog(int did)
	{
		AlertDialog dialog;
		
		switch (did) {
			case ADD_CLASS_LEVEL_DIALOG:
			default:
				dialog = _initAddCharacterLevelDialog();
				break;
		}
		
		return dialog;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_ADD_CLASS_LEVELS, 0, "Add Class")
                .setShortcut('1', 'a')
                .setIcon(android.R.drawable.ic_menu_add);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		Intent i = new Intent();
		
		switch (item.getItemId()) {
			case MENU_ITEM_ADD_CLASS_LEVELS:
	    		showDialog(ADD_CLASS_LEVEL_DIALOG);
			break;
		}
		
		return true;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.class_level_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		long id = getListAdapter().getItemId(info.position);
		
        Uri editUri = ContentUris.withAppendedId(Uri.parse("content://com.cthos.pfpt.core.characterclasslevelprovider/character_class_level"), id);
        
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(editUri);
        ContentProvider provider = client.getLocalContentProvider();
        
		switch (item.getItemId()) {
			case R.id.delete:
				provider.delete(editUri, null, null);
				break;
		}
		
		return true;
	}
	
	/**
	 * Handles building out the add bonus type modal.
	 * 
	 * @return AlertDialog
	 */
	protected AlertDialog _initAddCharacterLevelDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		final View custom_view = inflater.inflate(R.layout.add_class_level_dialog, null);
		
		ArrayAdapter<CharSequence> classLevelsAdapter = ArrayAdapter.createFromResource(
                this, R.array.character_class_array, android.R.layout.simple_spinner_item);
		classLevelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		final Spinner classNameSpinner = (Spinner) custom_view.findViewById(R.id.add_class_level_class_spinner);
		classNameSpinner.setAdapter(classLevelsAdapter);
		
		classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	className = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
		
		ArrayAdapter<CharSequence> howManyAdapter = ArrayAdapter.createFromResource(
                this, R.array.character_levels_array, android.R.layout.simple_spinner_item);
		howManyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		final Spinner howManyLevelsSpinner = (Spinner) custom_view.findViewById(R.id.add_class_level_num_levels_spinner);
		howManyLevelsSpinner.setAdapter(howManyAdapter);
		
		howManyLevelsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	numLevels = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Class Level")
                              .setView(custom_view)
                              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									handleLevelAdded();
									
									dialog.cancel();
								}
							})
							.setNegativeButton("Cancel", null).create();
		
		return dialog;
	}
	
	protected void notifyNewClass()
	{
		classLevelCursor.requery();
		adpt.notifyDataSetChanged();
	}
	
	public void handleLevelAdded()
	{
		val = new ContentValues();
		val.put("class_name", className);
		val.put("num_levels", numLevels);
		val.put("character_id", characterId);
		
		cr = getContentResolver();
		new addClassLevelTask().execute(val);
	}
	
	private class addClassLevelTask extends AsyncTask<ContentValues, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(ContentValues... val) {
			cr.insert(Uri.parse("content://com.cthos.pfpt.core.characterclasslevelprovider/character_class_level"), val[0]);
			return true;
		}
		
		@Override
    	protected void onPostExecute(Boolean success)
    	{
			notifyNewClass();
    	}
	}
}
