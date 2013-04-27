package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Oracle extends CharacterClass {
    public Oracle() {
        babProgression = "medium";
        hitDie = 8;
        badSaves.add("reflex");
        badSaves.add("fort");
        skillsPerLevel = 4;
    }

    public String toString() {
        return "oracle";
    }
}
