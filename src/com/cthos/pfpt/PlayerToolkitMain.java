package com.cthos.pfpt;

import com.cthos.pfpt.core.CharacterClass;
import com.cthos.pfpt.core.chr.Barbarian;
import com.cthos.pfpt.core.chr.CharacterClassFactory;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

public class PlayerToolkitMain extends Activity
{	
	protected static final int MENU_ITEM_SETTINGS = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onCreateCharacter(View v)
    {
    	Intent i = new Intent();
    	i.setClassName("com.cthos.pfpt", "com.cthos.pfpt.CreateCharacter");
    	startActivity(i);
    }
    
    public void onLoadCharacter(View v)
    {
    	Intent i = new Intent();
    	i.setClassName("com.cthos.pfpt", "com.cthos.pfpt.LoadCharacter");
    	startActivity(i);
    }
    
    public void openPFSRD(View v)
    {
    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.d20pfsrd.com")));
    }
    
    public void openPRD(View v)
    {
    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://paizo.com/pathfinderRPG/prd/")));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_SETTINGS, 0, "Settings")
                .setShortcut('1', 's')
                .setIcon(android.R.drawable.ic_menu_preferences);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Intent intent = new Intent();
    	
    	switch (item.getItemId()) {
    		case MENU_ITEM_SETTINGS:
    			intent.setClass(this, SettingsActivity.class);
    			break;
    		default:
    			return false;
    	}
    	
    	startActivity(intent);
    	
    	return true;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	moveTaskToBack(true);
	    }
	    return super.onKeyDown(keyCode, event);
	}
}