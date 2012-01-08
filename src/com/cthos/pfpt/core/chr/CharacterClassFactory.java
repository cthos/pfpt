package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class CharacterClassFactory
{
	protected enum cClasses {
		barbarian,
		bard,
		cleric,
		fighter,
		gunslinger,
		monk
	}
	
	/**
	 * Takes a string character class name and turns
	 * it into the appropriate object
	 * 
	 * @param className
	 * @return
	 */
	public static CharacterClass factory(String className)
	{
		switch (cClasses.valueOf(className.toLowerCase())) {
			case barbarian:
				return new Barbarian();
			case bard:
				return new Bard();
			case cleric:
				return new Cleric();
			case fighter:
				return new Fighter();
			case gunslinger:
				return new Gunslinger();
			case monk:
				return new Monk();
		}
		
		return null;
	}
}
