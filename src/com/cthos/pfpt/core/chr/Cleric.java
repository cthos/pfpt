package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Cleric extends CharacterClass
{
	public Cleric()
	{
		babProgression = "medium";
		hitDie = 8;
		badSaves.add("reflex");
		skillsPerLevel = 2;
	}
	
	public String toString()
	{
		return "cleric";
	}
}
