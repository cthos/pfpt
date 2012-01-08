package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Fighter extends CharacterClass
{
	public Fighter()
	{
		babProgression = "full";
		hitDie = 10;
		badSaves.add("reflex");
		badSaves.add("will");
		skillsPerLevel = 2;
	}
	
	public String toString()
	{
		return "fighter";
	}
}
