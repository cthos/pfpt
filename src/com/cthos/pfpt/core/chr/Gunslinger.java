package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Gunslinger extends CharacterClass
{
	public Gunslinger()
	{
		babProgression = "full";
		hitDie = 10;
		badSaves.add("will");
		skillsPerLevel = 4;
	}
	
	public String toString()
	{
		return "gunslinger";
	}
}
