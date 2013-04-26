package com.cthos.pfpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActiveEffectsActivity extends ListActivity
{
	protected static final int MENU_ITEM_NEXT_ROUND = 1;
	protected static final int MENU_ITEM_CLEAR_ALL = 2;
	
	protected static final int ADD_EFFECT_DIALOG = 1;
	
	protected long characterId;
	
	protected SimpleCursorAdapter _adapter;
	protected Cursor _cursor;
	
	private static final String[] PROJECTION = new String[] {
        "_id",
        "name",
        "duration"
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_effects_list);
		
		long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;
        
        _initButtons();
        _initList();
	}

    protected void _initButtons()
	{
		Button addButton = (Button) findViewById(R.id.active_effects_add_effect);
		addButton.setOnClickListener(handleAddClicked);
	}
	
	protected void _initList()
	{	
		_cursor = managedQuery(
		Uri.parse("content://com.cthos.pfpt.core.activeeffectprovider/effect"),
			PROJECTION,
		 	"character_id = ?",
		 	new String[]{String.valueOf(characterId)},
		    "duration DESC"
	    );
		
		startManagingCursor(_cursor);
		
		// TODO: Loading data on the UI thread is probably not ideal.
		_adapter = new SimpleCursorAdapter(this, R.layout.active_effect_list_item, _cursor,
                new String[] { "name", "duration" }, new int[] {R.id.active_effect_item_name, R.id.active_effect_item_duration});
        setListAdapter(_adapter);
        registerForContextMenu(getListView());
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_NEXT_ROUND, 0, "Next Round")
                .setShortcut('1', 'n')
                .setIcon(android.R.drawable.ic_menu_add);
        menu.add(0, MENU_ITEM_CLEAR_ALL, 1, "Clear All")
                .setShortcut('2', 'c')
                .setIcon(android.R.drawable.ic_menu_delete);
        
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		Intent i = new Intent();
		
		switch (item.getItemId()) {
			case MENU_ITEM_NEXT_ROUND:
				_nextRound();
	    		break;
			case MENU_ITEM_CLEAR_ALL:
				_clearAllEffects();
				break;
		}
		
		return true;
    }

	/**
	 * Gathers all of the active effects on a given character and then
	 * decrements the duration by one. If the duration is 0 or less, remove
	 * it.
	 *
	 */
	protected void _nextRound()
	{
		ContentResolver cr = getContentResolver();

		Uri effectUri = Uri.parse("content://com.cthos.pfpt.core.activeeffectprovider/effect");
		String[] characterIdWhere = new String[] {String.valueOf(characterId)};

		Cursor result = cr.query(
				effectUri,
				new String[] {"_id", "duration"},
				"character_id = ?",
				characterIdWhere,
				null
		);

		result.moveToFirst();
		do {
			int id = result.getInt(result.getColumnIndex("_id"));
			String duration = result.getString(result.getColumnIndex("duration"));

			int nDuration = Integer.parseInt(duration);
			nDuration -= 1;

			if (nDuration > 0) {
				ContentValues cv = new ContentValues();
				cv.put("duration", String.valueOf(nDuration));

				cr.update(
					effectUri,
					cv,
					"character_id = ?",
					characterIdWhere
				);
			} else {
				cr.delete(effectUri, "character_id = ?", characterIdWhere);
			}
		} while (result.moveToNext());

		result.close();

		_refresh();
	}
	
	protected void _clearAllEffects()
	{
		ContentResolver cr = getContentResolver();
		
		cr.delete(
			Uri.parse("content://com.cthos.pfpt.core.activeeffectprovider/effect"),
			"character_id = ?",
			new String[] {String.valueOf(characterId)}
		);
		
		_refresh();
	}
	
	/**
	 * Requeries the cursor, notifies the adapter that the data
	 * set has changed, and then informs the view that it needs to be 
	 * refreshed.
	 * 
	 * @return void
	 */
	protected void _refresh()
	{
		_cursor.requery();
		_adapter.notifyDataSetChanged();
		findViewById(android.R.id.list).invalidate();
	}
	
	private OnClickListener handleAddClicked = new OnClickListener() {
    	public void onClick(View v)
    	{
    		Activity a = (Activity) v.getContext();
    		
    		Intent i = new Intent();
    		i.setClass(a, AddEffectsActivity.class);
    		i.putExtra("characterId", characterId);
    		a.startActivity(i);
    	}
    };
}
