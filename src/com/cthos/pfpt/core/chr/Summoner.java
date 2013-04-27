package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Summoner extends CharacterClass {
    public Summoner() {
        babProgression = "medium";
        hitDie = 8;
        badSaves.add("reflex");
        badSaves.add("fort");
        skillsPerLevel = 2;
    }

    public String toString() {
        return "summoner";
    }
}
