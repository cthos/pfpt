package com.cthos.pfpt;

import android.app.Activity;
import android.view.Menu;

public class GearActivity extends Activity
{
	public static final int MENU_ITEM_ADD_GEAR = 1;
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_ADD_GEAR, 0, "Add Gear")
                .setShortcut('1', 'i')
                .setIcon(android.R.drawable.ic_menu_add);
        
        return true;
    }
}
