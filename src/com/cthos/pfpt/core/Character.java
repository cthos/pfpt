/**
 * 
 */
package com.cthos.pfpt.core;

import java.util.HashMap;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.cthos.db.CharacterProvider;
import com.cthos.pfpt.core.ArmorClass;

/**
 * @author cthos
 *
 */
public class Character 
{
	public static final Uri CONTENT_URI = Uri.parse("content://com.cthos.pfpt.core/character");
	
	public String name;
	
	public HashMap<String, Number> attributes = new HashMap();
	
	public long ac;
	
	public Number bab;
	
	public Number cmd;
	
	public Number cmb;
	
	public Number rangedToHit;
	
	public Number meleeToHit;
	
	public String languages;
	
	public Character(Cursor c)
	{
		_loadFromCursor(c);
	}
	
	protected void _loadFromCursor(Cursor c)
	{
		c.moveToFirst();
		
		this.name = c.getString(c.getColumnIndex("name"));
		
		this.attributes.put("strength", c.getInt(c.getColumnIndex("strength")));
		this.attributes.put("dexterity", c.getInt(c.getColumnIndex("dexterity")));
		this.attributes.put("constitution", c.getInt(c.getColumnIndex("constitution")));
		this.attributes.put("wisdom", c.getInt(c.getColumnIndex("wisdom")));
		this.attributes.put("intelligence", c.getInt(c.getColumnIndex("intelligence")));
		this.attributes.put("charisma", c.getInt(c.getColumnIndex("charisma")));
		
		// TODO: Modify attrs by magic gear, etc. 
		this.calculateAC();
	}
	
	public long calculateAC()
	{
		long baseAC = 10;
		
		long dexBonus = this.calculateBonus(this.attributes.get("dexterity"));
		
		// TODO: Figure in Armour Max Dex Bonus + Armor bonus
		long armor = 0;
		long deflection = 0;
		long dodge = 0;
		
		long AC = baseAC + dexBonus + armor + deflection + dodge;
		
		this.ac = AC;
		
		return this.ac;
	}
	
	/**
	 * Determines the bonus provided by an ability score.
	 * 
	 * @param long score
	 * 
	 * @return
	 */
	public long calculateBonus(Number score)
	{
		double rawScore = Math.ceil((score.longValue() - 10) / 2);
		if (score.longValue() - 10 < 0){
			rawScore--;
		}
				
		return (long) rawScore;
	}
}
