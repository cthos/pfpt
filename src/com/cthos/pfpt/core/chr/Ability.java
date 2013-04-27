package com.cthos.pfpt.core.chr;

/**
 * Simple Wrapper to keep track of a given ability.
 *
 * @author cthos <daginus@gmail.com>
 */
public class Ability {
    int maxUses;

    int currentUses;

    public Ability(int uses) {
        this.maxUses = this.currentUses = uses;
    }
}
