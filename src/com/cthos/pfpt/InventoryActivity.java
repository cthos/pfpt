package com.cthos.pfpt;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class InventoryActivity extends TabActivity
{
	public long characterId;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory);
		
		characterId = getIntent().getLongExtra("characterId", 0);
		
		Resources res = getResources();
		TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;
	    
	    intent = new Intent().setClass(this, SlottedItemActivity.class);
	    intent.putExtra("characterId", characterId);
	    
	    spec = tabHost.newTabSpec("slotted").setIndicator("Slotted Items",
	    				res.getDrawable(R.drawable.slotted_item_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    Intent gearIntent = new Intent().setClass(this, GearActivity.class);
	    gearIntent.putExtra("characterId", characterId);
	    
	    spec = tabHost.newTabSpec("gear").setIndicator("Gear",
				res.getDrawable(R.drawable.gear_tab))
              .setContent(gearIntent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
}
