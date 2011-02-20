package com.cthos.pfpt.core;

import java.util.HashMap;

public class SkillSet 
{
	HashMap<String, Integer> skillValues = new HashMap<String, Integer>();
	
	static final String[] availableSkills = {
		"Acrobatics",
		"Appraise",
		"Bluff",
		"Climb",
		"Craft",
		"Diplomacy",
		"Disable Device",
		"Disguise",
		"Escape Artist",
		"Fly",
		"Handle Animal",
		"Heal",
		"Intimidate",
		"Knowledge (Arcana)",
		"Knowledge (Dungeoneering)",
		"Knowledge (Geography)",
		"Knowledge (History)",
		"Knowledge (Local)",
		"Knowledge (Nature)",
		"Knowledge (Planes)",
		"Knowledge (Religion)",
		"Linguistics",
		"Perception",
		"Perform",
		"Profession",
		"Ride",
		"Sense Motive",
		"Slight of Hand",
		"Speechcraft",
		"Stealth",
		"Survival",
		"Swim",
		"Use Magic Device"
	};
	
	public SkillSet()
	{
		
	}
	
	public SkillSet(int characterIdentifier)
	{
		
	}
	
	public HashMap<String, Object>[] getSkills()
	{
		HashMap<String, Object>[] skills = null;
		
		
		return skills;
	}
}
