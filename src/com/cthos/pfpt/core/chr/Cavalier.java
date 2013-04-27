package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Cavalier extends CharacterClass {
    public Cavalier() {
        babProgression = "full";
        hitDie = 10;
        badSaves.add("reflex");
        badSaves.add("will");
        skillsPerLevel = 4;
    }

    public String toString() {
        return "cavalier";
    }
}
