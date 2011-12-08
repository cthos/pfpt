package com.cthos.pfpt;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;

public class GearActivity extends ListActivity
{
	public static final int MENU_ITEM_ADD_GEAR = 1;
	
	public long characterId;
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		
		characterId = getIntent().getLongExtra("characterId", 0);
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
