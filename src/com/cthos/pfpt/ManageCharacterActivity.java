package com.cthos.pfpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ManageCharacterActivity extends Activity
{
	protected long characterId;
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		characterId = getIntent().getLongExtra("characterId", 0);
		
		this.setContentView(R.layout.manage_character);
		
		_initButtons();
	}
	
	protected void _initButtons()
	{
		Button charLevels = (Button) findViewById(R.id.manage_character_levels_button);
		charLevels.setOnClickListener(handleManageLevels);
		
		Button adjustStats = (Button) findViewById(R.id.adjust_stats_button);
		adjustStats.setOnClickListener(handleAdjustStats);
	}
	
	private OnClickListener handleManageLevels = new OnClickListener() {
    	public void onClick(View v)
    	{
    		Intent i = new Intent();
    		i.setClass(v.getContext(), CharacterLevelActivity.class);
    		i.putExtra("characterId", characterId);
    		
    		startActivity(i);
    	}
    };
    
    private OnClickListener handleAdjustStats = new OnClickListener() {
    	public void onClick(View v)
    	{
    		
    	}
    };
}
