package com.cthos.pfpt;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateCharacter extends Activity 
{
	public final int MENU_ITEM_CANCEL = 2010;
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_character);
        
        this.setUpSpinners();
    }
    
    private void setUpSpinners()
    {
    	Spinner lvSpin = (Spinner) findViewById(R.id.levelSpinner);
    	ArrayAdapter<CharSequence> lvAdapter = ArrayAdapter.createFromResource(
                this, R.array.character_levels_array, android.R.layout.simple_spinner_item);
    	lvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lvSpin.setAdapter(lvAdapter);
        
        ArrayAdapter<CharSequence> ablAdapter = ArrayAdapter.createFromResource(
                this, R.array.ability_scores_array, android.R.layout.simple_spinner_item);
        ablAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Spinner strSpin = (Spinner) findViewById(R.id.strengthSpinner);
        strSpin.setAdapter(ablAdapter);
        strSpin.setSelection(10);
        
        Spinner dexSpinner = (Spinner) findViewById(R.id.dexteritySpinner);
        dexSpinner.setAdapter(ablAdapter);
        dexSpinner.setSelection(10);
        
        Spinner conSpinner = (Spinner) findViewById(R.id.constitutionSpinner);
        conSpinner.setAdapter(ablAdapter);
        conSpinner.setSelection(10);
        
        Spinner intSpinner = (Spinner) findViewById(R.id.intellegenceSpinner);
        intSpinner.setAdapter(ablAdapter);
        intSpinner.setSelection(10);
        
        Spinner wisSpinner = (Spinner) findViewById(R.id.wisdomSpinner);
        wisSpinner.setAdapter(ablAdapter);
        wisSpinner.setSelection(10);
        
        Spinner chaSpinner = (Spinner) findViewById(R.id.charismaSpinner);
        chaSpinner.setAdapter(ablAdapter);
        chaSpinner.setSelection(10);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // This is our one standard application action -- inserting a
        // new note into the list.
        menu.add(0, MENU_ITEM_CANCEL, 0, "Cancel Character")
                .setShortcut('3', 'a')
                .setIcon(android.R.drawable.ic_menu_delete);
        
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, CreateCharacter.class), null, intent, 0, null);

        return true;
    }
}
