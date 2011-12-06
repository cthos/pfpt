package com.cthos.pfpt;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewCharacter extends Activity
{
	protected com.cthos.pfpt.core.Character character;
	
	long characterId;
	
	public static final int MENU_ITEM_INVENTORY = 0;
	public static final int MENU_ITEM_ABILITIES = 1;
	public static final int MENU_ITEM_SKILLS = 2;
	public static final int MENU_ITEM_EFFECTS = 3;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_character);
        
        long charId = getIntent().getLongExtra("characterId", 0);
        characterId = charId;
        
        Log.d("", String.valueOf(characterId));
        
        loadCharacterData(characterId);
    }
	
	protected void loadCharacterData(long characterId)
	{
		Uri chUri = ContentUris.withAppendedId(
			Uri.parse("content://com.cthos.pfpt.core/character"),
			characterId
		);
		
		ContentProviderClient client = getContentResolver().acquireContentProviderClient(chUri);
        ContentProvider provider = client.getLocalContentProvider();
        
        Cursor c = provider.query(chUri, null, null, null, null);
        
        this.character = new com.cthos.pfpt.core.Character(c);
        
        startManagingCursor(c);
        
        buildInterface();
	}
	
	protected void buildInterface()
	{
		TextView nameLabel = (TextView) findViewById(R.id.character_name);
		nameLabel.setText(this.character.name);
		
		populateAttributes();
		populateAC();
	}
	
	/**
	 * Populates everything in the attributes fields.
	 * 
	 */
	protected void populateAttributes()
	{
		TextView strval = (TextView) findViewById(R.id.str_value);
		long strBonus = this.character.calculateBonus(this.character.attributes.get("strength"));
		strval.setText(String.valueOf(this.character.attributes.get("strength"))
				+ " (" + String.valueOf(strBonus) + ")");
		
		TextView dexval = (TextView) findViewById(R.id.dex_value);
		Number dex_value = this.character.attributes.get("dexterity");
		long dexBonus = this.character.calculateBonus(dex_value);
		dexval.setText(dex_value.toString() + " (" + String.valueOf(dexBonus) + ")");
		
		TextView conval = (TextView) findViewById(R.id.con_value);
		Number con_value = this.character.attributes.get("constitution");
		long conBonus = this.character.calculateBonus(con_value);
		conval.setText(con_value.toString() + " (" + String.valueOf(conBonus) + ")");
		
		TextView wisval = (TextView) findViewById(R.id.wis_value);
		Number wis_value = this.character.attributes.get("wisdom");
		long wisBonus = this.character.calculateBonus(wis_value);
		wisval.setText(wis_value.toString() + " (" + String.valueOf(wisBonus) + ")");
		
		TextView intval = (TextView) findViewById(R.id.int_value);
		Number int_value = this.character.attributes.get("intelligence");
		long intBonus = this.character.calculateBonus(int_value);
		intval.setText(int_value.toString() + " (" + String.valueOf(intBonus) + ")");
		
		TextView chaval = (TextView) findViewById(R.id.cha_value);
		Number cha_value = this.character.attributes.get("charisma");
		long chaBonus = this.character.calculateBonus(cha_value);
		chaval.setText(cha_value.toString() + " (" + String.valueOf(chaBonus) + ")");
	}
	
	protected void populateAC()
	{
		TextView acval = (TextView) findViewById(R.id.ac_value);
		acval.setText(String.valueOf(this.character.ac));
	}
	
	/**
	 * Create the options menu. Add things like Manage Gear, Spell Book, Effects
	 * etc.
	 * 
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, MENU_ITEM_INVENTORY, 0, "Inventory")
                .setShortcut('1', 'i')
                .setIcon(android.R.drawable.ic_menu_compass);
        
        menu.add(1, MENU_ITEM_ABILITIES, 0, "Abilities")
        		.setIcon(android.R.drawable.ic_menu_search);
        
        menu.add(2, MENU_ITEM_SKILLS, 0, "Skills")
        	.setIcon(android.R.drawable.ic_menu_directions);
        
        menu.add(2, MENU_ITEM_EFFECTS, 0, "Effects")
    		.setIcon(android.R.drawable.ic_menu_add);
        
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, CreateCharacter.class), null, intent, 0, null);

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Intent intent = new Intent();
    	
    	switch (item.getItemId()) {
    		case MENU_ITEM_INVENTORY:
    			intent.setClassName("com.cthos.pfpt", "com.cthos.pfpt.InventoryActivity");
    			break;
    		default:
    			return false;
    	}
    	
    	intent.putExtra("characterId", characterId);
    	
    	startActivity(intent);
    	
    	return true;
    }
}
