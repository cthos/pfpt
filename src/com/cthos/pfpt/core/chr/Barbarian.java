package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Barbarian extends CharacterClass
{
	public Barbarian()
	{
		babProgression = "full";
		hitDie = 12;
		badSaves.add("will");
		skillsPerLevel = 2;
	}
	
	public String toString()
	{
		return "barbarian";
	}
}