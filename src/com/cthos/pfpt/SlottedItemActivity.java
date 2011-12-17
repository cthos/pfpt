package com.cthos.pfpt;

import java.util.ArrayList;

import com.cthos.pfpt.equipment.SlottedItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;

public class SlottedItemActivity extends ListActivity
{
	private final int ITEM_BONUSES_DIALOG = 1;
	
	public static final int MENU_ITEM_ADD_SLOTTED_ITEM = 1;
	
	public long characterId;
	
	protected SlottedItem _currentItem;
	
	private static final String[] PROJECTION = new String[] {
        "_id",
        "name",
        "location"
	};
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		
		characterId = getIntent().getLongExtra("characterId", 0);
		
		Cursor cursor = managedQuery(
		Uri.parse("content://com.cthos.pfpt.core.slotteditemprovider/slotted_item"),
			PROJECTION,
		 	"character_id = ?",
		 	new String[]{String.valueOf(characterId)},
		    "location ASC"
	    );
		
		startManagingCursor(cursor);
		
		// TODO: Loading data on the UI thread is probably not ideal.
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.slotted_item_item_layout, cursor,
                new String[] { "name", "location" }, new int[] {R.id.slotted_item_list_item_name, R.id.slotted_item_list_location});
        setListAdapter(adapter);
        registerForContextMenu(getListView());
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_ADD_SLOTTED_ITEM, 0, "Add Slotted Item")
                .setShortcut('1', 'i')
                .setIcon(android.R.drawable.ic_menu_add);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		Intent i = new Intent();
		
		switch (item.getItemId()) {
			case MENU_ITEM_ADD_SLOTTED_ITEM:
				i.setClass(this, AddSlottedItemActivity.class);
				i.putExtra("characterId", characterId);
			break;
		}
		
		startActivity(i);
		return true;
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Uri contentUri = ContentUris.withAppendedId(
			Uri.parse("content://com.cthos.pfpt.core.slotteditemprovider/slotted_item"),
			id
		);
		
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(contentUri);
        ContentProvider provider = client.getLocalContentProvider();
        
        Cursor c = provider.query(contentUri, null, null, null, null);
        
        startManagingCursor(c);
        
        c.moveToFirst();
        
        _currentItem = new SlottedItem(c);
        
        showDialog(ITEM_BONUSES_DIALOG);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.slotted_item_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		long id = getListAdapter().getItemId(info.position);
        Log.d("", "id = " + id);
		
        Uri editUri = ContentUris.withAppendedId(Uri.parse("content://com.cthos.pfpt.core.slotteditemprovider/slotted_item"), id);
        
        ContentProviderClient client = getContentResolver().acquireContentProviderClient(editUri);
        ContentProvider provider = client.getLocalContentProvider();
        
		switch (item.getItemId()) {
			case R.id.delete:
				provider.delete(editUri, null, null);
				break;
		}
		
		return true;
	}
	
	protected Dialog onCreateDialog(int did)
	{
		Dialog dialog;
		
		switch (did) {
			case ITEM_BONUSES_DIALOG:
			default:
				dialog = _initSlottedItemDialog();
				break;
		}
		
		return dialog;
	}
	
	protected Dialog _initSlottedItemDialog()
	{
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.show_item_bonus_dialog);
		dialog.setTitle("Item Bonuses");
		
		ArrayAdapter<String> bonusAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1 , _currentItem.bonusTexts);
		ListView lv = (ListView) dialog.findViewById(R.id.show_item_bonuses_dialog_list);
		lv.setAdapter(bonusAdapter);
		
		Button closeButton = (Button) dialog.findViewById(R.id.show_item_dialog_close_dialog_button);
		closeButton.setOnClickListener(handleCloseBonusesDialog);
		
		return dialog;
	}
	
	private OnClickListener handleCloseBonusesDialog = new OnClickListener() {
    	public void onClick(View v)
    	{
    		removeDialog(ITEM_BONUSES_DIALOG);
    	}
    };
    
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
}
