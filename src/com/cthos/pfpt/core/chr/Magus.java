package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Magus extends CharacterClass {
    public Magus() {
        babProgression = "medium";
        hitDie = 8;
        badSaves.add("reflex");
        skillsPerLevel = 2;
    }

    public String toString() {
        return "magus";
    }
}
