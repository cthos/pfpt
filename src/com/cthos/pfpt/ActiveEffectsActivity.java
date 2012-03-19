package com.cthos.pfpt;

import android.app.ListActivity;
import android.os.Bundle;

public class ActiveEffectsActivity extends ListActivity
{
	protected long characterId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_effects_list);
		
		long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;
	}
}
