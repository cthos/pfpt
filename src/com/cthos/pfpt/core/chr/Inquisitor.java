package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Inquisitor extends CharacterClass
{
	public Inquisitor()
	{
		babProgression = "medium";
		hitDie = 8;
		badSaves.add("reflex");
		skillsPerLevel = 6;
	}
	
	public String toString()
	{
		return "inquisitor";
	}
}
