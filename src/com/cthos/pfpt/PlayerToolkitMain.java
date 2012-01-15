package com.cthos.pfpt;

import com.cthos.pfpt.core.CharacterClass;
import com.cthos.pfpt.core.chr.Barbarian;
import com.cthos.pfpt.core.chr.CharacterClassFactory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;

public class PlayerToolkitMain extends Activity {
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	moveTaskToBack(true);
	    }
	    return super.onKeyDown(keyCode, event);
	}
}