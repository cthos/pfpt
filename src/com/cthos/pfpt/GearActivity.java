package com.cthos.pfpt;

import com.cthos.adapter.GearAdapter;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;

public class GearActivity extends ListActivity
{
	public static final int MENU_ITEM_ADD_GEAR = 1;
	
	public static final int ADD_GEAR_DIALOG = 1;
	
	public long characterId;
	
	protected ContentResolver _cr;
	protected ContentValues _cv;
	protected Cursor _gearCursor;
	protected GearAdapter _gearAdapter;
	
	protected String itemName;
	protected String quantity;
	
	private static final String[] PROJECTION = new String[] {
        "_id",
        "name",
        "quantity"
	};
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		
		characterId = getIntent().getLongExtra("characterId", 0);
		
		_loadGear();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.abilities_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		long id = getListAdapter().getItemId(info.position);
        Log.d("", "id = " + id);
		
        // TODO: Move this to async task if it winds up being a problem.
        Uri editUri = ContentUris.withAppendedId(Uri.parse("content://com.cthos.pfpt.core.gearprovider/gear"), id);
        
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(editUri);
        ContentProvider provider = client.getLocalContentProvider();
        
		switch (item.getItemId()) {
			case R.id.delete:
				provider.delete(editUri, null, null);
				onGearListChanged();
				break;
		}
		
		return true;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_ADD_GEAR, 0, "Add Gear")
                .setShortcut('1', 'i')
                .setIcon(android.R.drawable.ic_menu_add);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		Intent i = new Intent();
		
		switch (item.getItemId()) {
			case MENU_ITEM_ADD_GEAR:
	    		showDialog(ADD_GEAR_DIALOG);
	    		break;
		}
		
		return true;
    }
	
	private void onGearListChanged()
	{
		_gearCursor.requery();
		_gearAdapter.notifyDataSetChanged();
		findViewById(android.R.id.list).invalidate();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        Intent backIntent = new Intent().setClass(this, ViewCharacter.class);
	        backIntent.putExtra("characterId", characterId);
	        
	        startActivity(backIntent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	protected void _loadGear()
	{
		_gearCursor = managedQuery(
			Uri.parse("content://com.cthos.pfpt.core.gearprovider/gear"),
			PROJECTION,
		 	"character_id = ?",
		 	new String[]{String.valueOf(characterId)},
		    "_id ASC"
	    );
		
		startManagingCursor(_gearCursor);
		
		_gearAdapter = new GearAdapter(this, R.layout.gear_list_item, _gearCursor,
                new String[] { "name", "quantity" }, new int[] {R.id.gear_item_name, R.id.gear_item_quantity});
        setListAdapter(_gearAdapter);
        registerForContextMenu(getListView());
	}
	
	/**
	 * Handled when the dialog is set to be summoned, and tells
	 * the application to do setup required for the
	 * Add Bonus dialog.
	 * 
	 * @param int did
	 */
	protected Dialog onCreateDialog(int did)
	{
		AlertDialog dialog;
		
		switch (did) {
			case ADD_GEAR_DIALOG:
			default:
				dialog = _initAddGearDialog();
				break;
		}
		
		return dialog;
	}
	
	protected void _handleGearAdded()
	{
		if (itemName.equals("") || quantity.equals("")) {
			return;
		}
		
		_cr = getContentResolver();
		_cv = new ContentValues();
		_cv.put("name", itemName);
		_cv.put("quantity", quantity);
		_cv.put("character_id", String.valueOf(characterId));
		
		new AsyncTask<Void, Void, Void> () {
			@Override
			protected Void doInBackground(Void... args) {
				_cr.insert(Uri.parse("content://com.cthos.pfpt.core.gearprovider/gear"), _cv);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void v)
			{
				onGearListChanged();
			}

			
		}.execute();
	}
	
	protected AlertDialog _initAddGearDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		final View custom_view = inflater.inflate(R.layout.add_gear_dialog, null);
		
		final EditText itemNameText = (EditText) custom_view.findViewById(R.id.add_gear_item_name);
		final EditText quantityText = (EditText) custom_view.findViewById(R.id.add_gear_quantity);
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Misc Gear")
            .setView(custom_view)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					itemName = itemNameText.getText().toString();
					quantity = quantityText.getText().toString();
					
					_handleGearAdded();
					
					dialog.cancel();
				}
			})
			.setNegativeButton("Cancel", null).create();

		return dialog;
	}
}
