package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Alchemist extends CharacterClass
{
	public Alchemist()
	{
		babProgression = "medium";
		hitDie = 8;
		badSaves.add("will");
		skillsPerLevel = 4;
	}
	
	public String toString()
	{
		return "alchemist";
	}
}
