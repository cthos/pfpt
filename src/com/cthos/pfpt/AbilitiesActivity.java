package com.cthos.pfpt;

import com.cthos.adapter.AbilitiesAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class AbilitiesActivity extends ListActivity
{
	long characterId;
	
	protected String _abilityName;
	
	protected String _maxUses;
	
	protected ContentResolver _cr;
	
	protected ContentValues _cv;
	
	protected AbilitiesAdapter _curAdapter;
	
	protected static final int ADD_ABILITY_DIALOG = 1;
	
	private static final String[] PROJECTION = new String[] {
        "_id",
        "name",
        "current_uses",
        "max_uses"
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abilities_list);
        
        long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;
        
        _initButtons();
        _loadAbilities();
    }
	
	/**
	 * Loads abilities into a SimpleCursor adapter to show them in the activity
	 * 
	 */
	protected void _loadAbilities()
	{
		Cursor cursor = managedQuery(
		Uri.parse("content://com.cthos.pfpt.core.abilityprovider/ability"),
			PROJECTION,
		 	"character_id = ?",
		 	new String[]{String.valueOf(characterId)},
		    "_id ASC"
	    );
		
		startManagingCursor(cursor);
		
		_curAdapter = new AbilitiesAdapter(this, R.layout.ability_list_item, cursor,
                new String[] { "name", "current_uses" }, new int[] {R.id.ability_item_ability_name, R.id.ability_item_abiltity_count});
        setListAdapter(_curAdapter);
        registerForContextMenu(getListView());
	}
	
	protected void _initButtons()
	{
		Button addAbilityButton = (Button) findViewById(R.id.abilities_list_add_ability);
		addAbilityButton.setOnClickListener(_handleAddAbilityClick);
	}
	
	/**
	 * Called after the addAbility dialog is closed with a
	 * positive affirmation. It likes positivity. Also, it runs
	 * a simple background process to insert a new limited-use ability.
	 * 
	 */
	protected void _handleAbilityAdded()
	{
		if (_abilityName.equals("") || _maxUses.equals("")) {
			return;
		}
		
		_cr = getContentResolver();
		_cv = new ContentValues();
		_cv.put("name", _abilityName);
		_cv.put("current_uses", _maxUses);
		_cv.put("max_uses", _maxUses);
		_cv.put("character_id", String.valueOf(characterId));
		
		new AsyncTask<Void, Void, Void> () {
			@Override
			protected Void doInBackground(Void... args) {
				_cr.insert(Uri.parse("content://com.cthos.pfpt.core.abilityprovider/ability"), _cv);
				return null;
			}
		}.execute();
		
		_curAdapter.notifyDataSetChanged();
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
			case ADD_ABILITY_DIALOG:
			default:
				dialog = _initAddAbilityDialog();
				break;
		}
		
		return dialog;
	}
	
	protected AlertDialog _initAddAbilityDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		final View custom_view = inflater.inflate(R.layout.add_ability_dialog, null);
		
		final EditText abilityName = (EditText) custom_view.findViewById(R.id.add_ability_ability_name);
		final EditText maxUses = (EditText) custom_view.findViewById(R.id.add_ability_maximum_uses);
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Bonus")
            .setView(custom_view)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					_abilityName = abilityName.getText().toString();
					_maxUses = maxUses.getText().toString();
					
					_handleAbilityAdded();
					
					dialog.cancel();
				}
			})
			.setNegativeButton("Cancel", null).create();

		return dialog;
	}
	
	private OnClickListener _handleAddAbilityClick = new OnClickListener() {
    	public void onClick(View v)
    	{
    		Activity act = (Activity) v.getContext();
    		act.showDialog(ADD_ABILITY_DIALOG);
    	}
    };
}
