package com.cthos.pfpt;

import android.app.Activity;
import android.os.Bundle;

public class AdjustStatsActivity extends Activity
{
	protected long characterId;
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		characterId = getIntent().getLongExtra("characterId", 0);
		
		this.setContentView(R.layout.adjust_stats);
		
	}
}
