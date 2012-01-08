package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Paladin extends CharacterClass
{
	public Paladin()
	{
		babProgression = "full";
		hitDie = 10;
		badSaves.add("reflex");
		skillsPerLevel = 2;
	}
	
	public String toString()
	{
		return "paladin";
	}
}
