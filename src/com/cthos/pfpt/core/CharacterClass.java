package com.cthos.pfpt.core;

import java.util.ArrayList;

import com.cthos.pfpt.core.chr.*;

public class CharacterClass 
{
	public String name;
	
	public int numLevels;
	
	public String babProgression;
	
	public ArrayList<String> badSaves;
	
	public int hitDie;
	
	public int skillsPerLevel;
	
	public CharacterClass()
	{
		badSaves = new ArrayList<String>();
		hitDie = 0;
	}
	
	public CharacterClass(int classId)
	{
		
	}
	
	public void setNumLevels(int levels)
	{
		numLevels = levels;
	}
	
	public long getBAB()
	{
		if (babProgression == "full") {
			return numLevels;
		}
		
		if (babProgression == "medium") {
			return (long) Math.floor(numLevels * .75);
		}
		
		if (babProgression == "slow") {
			return (long) Math.floor(numLevels * .5);
		}
		
		return 0;
	}
}
