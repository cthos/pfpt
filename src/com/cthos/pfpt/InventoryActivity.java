package com.cthos.pfpt;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class InventoryActivity extends TabActivity
{
	public static final int MENU_ITEM_ADD_SLOTTED_ITEM = 1;
	public static final int MENU_ITEM_ADD_GEAR = 2;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory);
		
		Resources res = getResources();
		TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;
	    
	    intent = new Intent().setClass(this, SlottedItemActivity.class);
	    
	    spec = tabHost.newTabSpec("slotted").setIndicator("Slotted Items",
	    				res.getDrawable(R.drawable.slotted_item_tab))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    spec = tabHost.newTabSpec("gear").setIndicator("Gear",
				res.getDrawable(R.drawable.gear_tab))
              .setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_ADD_SLOTTED_ITEM, 0, "Add Slotted Item")
                .setShortcut('1', 'i')
                .setIcon(android.R.drawable.ic_menu_add);
        
        menu.add(0, MENU_ITEM_ADD_GEAR, 0, "Add Gear")
		        .setShortcut('2', 'g')
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
			break;
		}
		
		startActivity(i);
		return true;
    }
}
