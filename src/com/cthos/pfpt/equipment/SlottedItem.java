package com.cthos.pfpt.equipment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class SlottedItem
{
	public String name;
	
	public JSONArray bonuses;
	
	public ArrayList<String> bonusTexts;
	
	public String location;
	
	public String additional_properties;
	
	public SlottedItem(Cursor c)
	{
		_loadFromCursor(c);
	}
	
	protected void _loadFromCursor(Cursor c)
	{
		c.moveToFirst();
		
		this.name = c.getString(c.getColumnIndex("name"));
		
		String bonuses = c.getString(c.getColumnIndex("bonuses"));
		try {
			this.bonuses = new JSONArray(bonuses);
			int bonusLength = this.bonuses.length();
			this.bonusTexts = new ArrayList<String>();
			String txt;
			
			for (int i = 0; i < bonusLength; i++) {
				JSONObject bonus = this.bonuses.getJSONObject(i);
				txt = bonus.getString("name").toString() + " (" +
						bonus.getString("toWhat").toString() + ") (" +
						bonus.getString("howMuch").toString() + ")";
				
				this.bonusTexts.add(txt);
			}
		} catch (JSONException e) {
			this.bonuses = null;
		}
		
		this.location = c.getString(c.getColumnIndex("location"));
		this.additional_properties = c.getString(c.getColumnIndex("additional_properties"));
	}
}
