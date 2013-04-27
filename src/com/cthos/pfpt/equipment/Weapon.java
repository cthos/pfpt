package com.cthos.pfpt.equipment;

public class Weapon extends Gear {
    public String name;

    public int enhancementBonus = 0;

    public Boolean isMasterworked = false;

    public int[] damageDice = {1, 6};

    public String size = "M";

    public String specialQualities;

    public Weapon(int weaponId) {

    }

    public Weapon() {

    }

    protected void _loadFromDatabase(int weaponId) {

    }
}
