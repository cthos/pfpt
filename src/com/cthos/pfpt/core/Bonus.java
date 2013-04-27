package com.cthos.pfpt.core;

public class Bonus {
    public static final String[] stackableBonuses = {"Dodge", "Other"};

    /**
     * Checks to see if a given bonus type can stack. To add a new bonus
     * add it to the static array stackableBonuses
     *
     * @param String bonusName
     * @return
     */
    public static boolean canBonusStack(String bonusName) {
        for (int i = 0; i < Bonus.stackableBonuses.length; i++) {
            if (bonusName.toLowerCase().equals(Bonus.stackableBonuses[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
