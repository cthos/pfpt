package com.cthos.pfpt.core;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

public class ActiveEffect
{
	public String name;
	
	public JSONArray bonuses;
	
	public ArrayList<String> bonusTexts;
	
	public String description;
	
	public String duration;
	
	public ActiveEffect(Cursor c)
	{
		_loadFromCursor(c);
	}
	
	protected void _loadFromCursor(Cursor c)
	{
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
		
		this.description = c.getString(c.getColumnIndex("description"));
		this.duration = c.getString(c.getColumnIndex("duration"));
	}
	
	public ArrayList<JSONObject> getBonuses(String toWhat)
	{
		int bonusLength = this.bonuses.length();
		ArrayList<JSONObject> al = new ArrayList<JSONObject>();
		
		toWhat = toWhat.toLowerCase();
		
		for (int i = 0; i < bonusLength; i++) {
			JSONObject bonus;
			try {
				bonus = this.bonuses.getJSONObject(i);
				
				if (!bonus.getString("toWhat").toLowerCase().equals(toWhat)) {
					continue;
				}
				
				Log.d("Bonuses", "Adding Bonus of " + bonus.getString("name"));
				
				al.add(bonus);
			} catch (JSONException e) {
				Log.d("Fail", e.getMessage());
			}
		}
		
		return al;
	}
}
