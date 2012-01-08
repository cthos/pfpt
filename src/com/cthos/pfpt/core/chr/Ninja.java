package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Ninja extends CharacterClass
{
	public Ninja()
	{
		babProgression = "medium";
		hitDie = 8;
		badSaves.add("will");
		badSaves.add("fort");
		skillsPerLevel = 8;
	}
	
	public String toString()
	{
		return "ninja";
	}
}
