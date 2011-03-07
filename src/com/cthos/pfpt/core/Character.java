/**
 * 
 */
package com.cthos.pfpt.core;

import android.net.Uri;

import com.cthos.pfpt.core.ArmorClass;

/**
 * @author cthos
 *
 */
public class Character 
{
	public static final Uri CONTENT_URI = Uri.parse("content://com.cthos.pfpt.core/character");
	
	public String name;
	
	public ArmorClass ac;
	
	public Number bab;
	
	public Number cmd;
	
	public Number cmb;
	
	public Number rangedToHit;
	
	public Number meleeToHit;
	
	public String languages;
	
	public Character()
	{
		
	}
	
	protected void _loadFromDatabase(String characterName)
	{
		
	}
}
