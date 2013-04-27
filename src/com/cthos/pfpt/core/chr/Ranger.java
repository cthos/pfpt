package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Ranger extends CharacterClass {
    public Ranger() {
        babProgression = "full";
        hitDie = 10;
        badSaves.add("will");
        skillsPerLevel = 6;
    }

    public String toString() {
        return "ranger";
    }
}
