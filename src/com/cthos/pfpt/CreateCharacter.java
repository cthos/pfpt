package com.cthos.pfpt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateCharacter extends Activity 
{
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
        strSpin.setSelection(7);
        
        Spinner dexSpinner = (Spinner) findViewById(R.id.dexteritySpinner);
        dexSpinner.setAdapter(ablAdapter);
        dexSpinner.setSelection(7);
        
        Spinner conSpinner = (Spinner) findViewById(R.id.constitutionSpinner);
        conSpinner.setAdapter(ablAdapter);
        conSpinner.setSelection(7);
        
        Spinner intSpinner = (Spinner) findViewById(R.id.intellegenceSpinner);
        intSpinner.setAdapter(ablAdapter);
        intSpinner.setSelection(7);
    }
}
