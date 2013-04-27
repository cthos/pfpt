package com.cthos.pfpt.core.chr;

import com.cthos.pfpt.core.CharacterClass;

public class Bard extends CharacterClass {
    public Bard() {
        super();

        babProgression = "medium";
        hitDie = 6;
        badSaves.add("will");
        skillsPerLevel = 6;
    }

    public String toString() {
        return "bard";
    }
}
