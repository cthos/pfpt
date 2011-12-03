package com.cthos.pfpt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddSlottedItemActivity extends Activity
{
	public static String slotLocation = "Armor";
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_slotted_item);
		
		_initSpinner();
	}
	
	protected void _initSpinner()
	{
		ArrayAdapter<CharSequence> slotsAdapter = ArrayAdapter.createFromResource(
                this, R.array.slotted_items_slots_array, android.R.layout.simple_spinner_item);
		slotsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		Spinner slots = (Spinner) findViewById(R.id.slotted_item_slot_spinner);
		slots.setAdapter(slotsAdapter);
		slots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				AddSlottedItemActivity.slotLocation = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
	}
}
