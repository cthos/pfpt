package com.cthos.pfpt.core;

import java.util.ArrayList;

import com.cthos.pfpt.core.chr.*;

public class CharacterClass {
    public String name;

    public int numLevels;

    public String babProgression;

    public ArrayList<String> badSaves;

    public int hitDie;

    public int skillsPerLevel;

    public CharacterClass() {
        badSaves = new ArrayList<String>();
        hitDie = 0;
    }

    public CharacterClass(int classId) {

    }

    public void setNumLevels(int levels) {
        numLevels = levels;
    }

    public long getBAB() {
        if (babProgression == "full") {
            return numLevels;
        }

        if (babProgression == "medium") {
            return (long) Math.floor(numLevels * .75);
        }

        if (babProgression == "slow") {
            return (long) Math.floor(numLevels * .5);
        }

        return 0;
    }

    /**
     * Gives the save total for a given character class.
     * If the class has a bad save, the formula is levels / 3.
     * Otherwise a "good" save is 2 + (levels / 2). Round
     * down as per usual.
     *
     * @param saveType
     * @return long
     */
    public long getSave(String saveType) {
        long save = 0;

        if (this.badSaves.contains(saveType)) {
            save = (long) Math.floor(this.numLevels / 3);
        } else {
            long base = (long) Math.floor(this.numLevels / 2);
            save = 2 + base;
        }

        return save;
    }
}
