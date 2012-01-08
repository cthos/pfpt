package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Druid extends CharacterClass
{
	public Druid()
	{
		babProgression = "medium";
		hitDie = 8;
		badSaves.add("reflex");
		skillsPerLevel = 4;
	}
	
	public String toString()
	{
		return "druid";
	}
}
