package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Rogue extends CharacterClass {
    public Rogue() {
        babProgression = "medium";
        hitDie = 8;
        badSaves.add("fort");
        badSaves.add("will");
        skillsPerLevel = 8;
    }

    public String toString() {
        return "rogue";
    }
}
