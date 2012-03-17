package com.cthos.pfpt;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.cthos.pfpt.core.Character;

public class AdjustStatsActivity extends Activity
{
	protected long characterId;
	
	protected Character character;
	
	@Override
	public void onCreate(Bundle SavedInstanceState)
	{
		super.onCreate(SavedInstanceState);
		characterId = getIntent().getLongExtra("characterId", 0);
		
		this.setContentView(R.layout.adjust_stats);
		
		_loadCharacter();
		_initButtons();
		_initCurrentSettings();
	}
	
	protected void _loadCharacter()
	{
		Uri chUri = ContentUris.withAppendedId(
			Uri.parse("content://com.cthos.pfpt.core/character"),
			characterId
		);
		
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(chUri);
        ContentProvider provider = client.getLocalContentProvider();
        
        Cursor c = provider.query(chUri, null, null, null, null);
        
        character = new com.cthos.pfpt.core.Character(c, this);
        
        startManagingCursor(c);
	}
	
	protected void _initButtons()
	{
		Button saveButton = (Button) findViewById(R.id.adjust_stats_save_button);
		saveButton.setOnClickListener(saveClicked);
	}
	
	protected void _initCurrentSettings()
	{
		EditText hpField = (EditText) findViewById(R.id.adjust_stats_additional_hp);
		long additionalHp = character.getAdditionalHP();
		if (additionalHp != 0) {
			hpField.setText(String.valueOf(additionalHp));
		}
	}
	
	protected void _saveChanges()
	{
		EditText hpField = (EditText) findViewById(R.id.adjust_stats_additional_hp);
		String hpValue = hpField.getText().toString();
		
		try {
			character.setAdditionalHP(Long.valueOf(hpValue));
		} catch (NumberFormatException e) {
			Log.d("AdditionalHPFail", e.getMessage());
		}
		
		Intent i = new Intent();
		
		i.setClass(this, ViewCharacter.class);
		i.putExtra("characterId", characterId);
		
		startActivity(i);
	}
	
	protected OnClickListener saveClicked = new OnClickListener()
	{
		public void onClick(View v) {
			_saveChanges();
		}
	};
}
