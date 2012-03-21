package com.cthos.pfpt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActiveEffectsActivity extends ListActivity
{
	protected static final int MENU_ITEM_NEXT_ROUND = 1;
	
	protected static final int ADD_EFFECT_DIALOG = 1;
	
	protected long characterId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_effects_list);
		
		long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;
	}
	
	public void _initButtons()
	{
		Button addButton = (Button) findViewById(R.id.active_effects_add_effect);
		addButton.setOnClickListener(handleAddClicked);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_NEXT_ROUND, 0, "Next Round")
                .setShortcut('1', 'n')
                .setIcon(android.R.drawable.ic_menu_add);
        
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
		}
		
		return true;
    }
	
	protected Dialog onCreateDialog(int did)
	{
		AlertDialog dialog;
		
		switch (did) {
			case ADD_EFFECT_DIALOG:
			default:
				dialog = _initAddEffectDialog();
				break;
		}
		
		return dialog;
	}
	
	protected AlertDialog _initAddEffectDialog()
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		final View custom_view = inflater.inflate(R.layout.add_gear_dialog, null);
		
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Add Effect")
            .setView(custom_view)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.setNegativeButton("Cancel", null).create();

		return dialog;
	}
	
	protected void _nextRound()
	{
		
	}
	
	private OnClickListener handleAddClicked = new OnClickListener() {
    	public void onClick(View v)
    	{
    		Activity c = (Activity) v.getContext();
    		c.showDialog(ADD_EFFECT_DIALOG);
    	}
    };
}
