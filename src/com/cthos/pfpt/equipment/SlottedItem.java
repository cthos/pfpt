package com.cthos.pfpt.equipment;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class SlottedItem
{
	public String name;
	
	public JSONObject bonuses;
	
	public String location;
	
	public String additional_properties;
	
	public SlottedItem(Cursor c)
	{
		
	}
	
	protected void _loadFromCursor(Cursor c)
	{
		c.moveToFirst();
		
		this.name = c.getString(c.getColumnIndex("name"));
		
		String bonuses = c.getString(c.getColumnIndex("bonuses"));
		try {
			this.bonuses = new JSONObject(bonuses);
		} catch (JSONException e) {
			this.bonuses = null;
		}
		
		this.location = c.getString(c.getColumnIndex("location"));
		this.additional_properties = c.getString(c.getColumnIndex("additional_properties"));
	}
}
