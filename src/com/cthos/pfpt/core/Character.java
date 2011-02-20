/**
 * 
 */
package com.cthos.pfpt.core;

import com.cthos.pfpt.core.ArmorClass;

/**
 * @author cthos
 *
 */
public class Character 
{
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
