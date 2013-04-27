package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class CharacterClassFactory {
    protected enum cClasses {
        alchemist,
        barbarian,
        bard,
        cavalier,
        cleric,
        druid,
        fighter,
        gunslinger,
        inquisitor,
        magus,
        monk,
        ninja,
        oracle,
        paladin,
        ranger,
        rogue,
        samurai,
        sorcerer,
        summoner,
        witch,
        wizard
    }

    /**
     * Takes a string character class name and turns
     * it into the appropriate object
     *
     * @param className
     * @return
     */
    public static CharacterClass factory(String className) {
        switch (cClasses.valueOf(className.toLowerCase())) {
            case alchemist:
                return new Alchemist();
            case barbarian:
                return new Barbarian();
            case bard:
                return new Bard();
            case cavalier:
                return new Cavalier();
            case cleric:
                return new Cleric();
            case druid:
                return new Druid();
            case fighter:
                return new Fighter();
            case gunslinger:
                return new Gunslinger();
            case inquisitor:
                return new Inquisitor();
            case magus:
                return new Magus();
            case monk:
                return new Monk();
            case ninja:
                return new Ninja();
            case oracle:
                return new Oracle();
            case paladin:
                return new Paladin();
            case ranger:
                return new Ranger();
            case rogue:
                return new Rogue();
            case samurai:
                return new Samurai();
            case sorcerer:
                return new Sorcerer();
            case summoner:
                return new Summoner();
            case witch:
                return new Witch();
            case wizard:
                return new Wizard();
        }

        return null;
    }
}
