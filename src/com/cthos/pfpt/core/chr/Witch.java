package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Witch extends CharacterClass
{
	public Witch()
	{
		babProgression = "slow";
		hitDie = 6;
		badSaves.add("reflex");
		badSaves.add("fort");
		skillsPerLevel = 2;
	}
	
	public String toString()
	{
		return "witch";
	}
}
