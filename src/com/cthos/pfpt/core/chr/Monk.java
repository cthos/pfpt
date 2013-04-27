package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Monk extends CharacterClass {
    public Monk() {
        babProgression = "medium";
        hitDie = 8;
        skillsPerLevel = 2;
    }

    public String toString() {
        return "monk";
    }
}
