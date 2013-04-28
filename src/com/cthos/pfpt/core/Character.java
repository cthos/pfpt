/**
 *
 */
package com.cthos.pfpt.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.cthos.db.CharacterProvider;
import com.cthos.pfpt.core.ArmorClass;
import com.cthos.pfpt.equipment.SlottedItem;
import com.cthos.util.Registry;

/**
 * @author cthos <daginus@gmail.com>
 */
public class Character {
    public static final String SETTINGS_KEY = "CharacterSettings_";

    public static final Uri CONTENT_URI = Uri.parse("content://com.cthos.pfpt.core/character");

    public String name;

    public HashMap<String, Number> attributes = new HashMap<String, Number>();

    public HashMap<String, Number> modifiedAttributes = new HashMap<String, Number>();

    public HashMap<String, Number> saves = new HashMap<String, Number>();

    public HashMap<String, Number> modifiedSaves = new HashMap<String, Number>();

    public long ac;

    public long hp;

    public long currentHp;

    public long bab;

    public Number cmd;

    public Number cmb;

    public long rangedToHit;

    public long meleeToHit;

    protected Context _context;

    public String languages;

    protected ArrayList<SlottedItem> gear;

    protected ArrayList<CharacterClass> characterClasses;

    protected ArrayList<ActiveEffect> activeEffects;

    public Character(Cursor c, Context context) {
        this._context = context;
        _loadFromCursor(c);
    }

    protected void _loadFromCursor(Cursor c) {
        c.moveToFirst();

        this.name = c.getString(c.getColumnIndex("name"));

        long str = c.getLong(c.getColumnIndex("strength"));
        long dex = c.getInt(c.getColumnIndex("dexterity"));
        long con = c.getInt(c.getColumnIndex("constitution"));
        long wis = c.getInt(c.getColumnIndex("wisdom"));
        long intel = c.getInt(c.getColumnIndex("intelligence"));
        long cha = c.getInt(c.getColumnIndex("charisma"));


        this.attributes.put("strength", str);
        this.attributes.put("dexterity", dex);
        this.attributes.put("constitution", con);
        this.attributes.put("wisdom", wis);
        this.attributes.put("intelligence", intel);
        this.attributes.put("charisma", cha);

        str += getAttributeBonus("Strength");
        dex += getAttributeBonus("Dexterity");
        con += getAttributeBonus("Constitution");
        wis += getAttributeBonus("Wisdom");
        intel += getAttributeBonus("Intelligence");
        cha += getAttributeBonus("Charisma");

        this.modifiedAttributes.put("strength", str);
        this.modifiedAttributes.put("dexterity", dex);
        this.modifiedAttributes.put("constitution", con);
        this.modifiedAttributes.put("wisdom", wis);
        this.modifiedAttributes.put("intelligence", intel);
        this.modifiedAttributes.put("charisma", cha);

        this.calculateAC();
    }

    public void setGear(ArrayList<SlottedItem> gear) {
        this.gear = gear;
    }

    public void setActiveEffects(ArrayList<ActiveEffect> effects) {
        this.activeEffects = effects;
    }

    public void setClasses(ArrayList<CharacterClass> characterClasses) {
        Log.d("CharClasses", "Loading Character Classes");
        this.characterClasses = characterClasses;
    }

    public long calculateAC() {
        long baseAC = 10;

        long dexBonus = this.calculateBonus(this.modifiedAttributes.get("dexterity"));

        // TODO: Figure in Armour Max Dex Bonus + Armor bonus
        long AC = baseAC + dexBonus;

        AC += getAttributeBonus("AC");

        this.ac = AC;

        Log.d("AC Bonus", String.valueOf(AC));

        return this.ac;
    }

    public long calculateHP() {
        long conBonus = this.calculateBonus(this.attributes.get("constitution"));

        long baseHP = conBonus;

        int classLen = this.characterClasses.size();
        CharacterClass cl;
        double hpbeep;

        for (int i = 0; i < classLen; i++) {
            cl = this.characterClasses.get(i);
            int numLevels = cl.numLevels;

            if (i == 0) {
                baseHP += cl.hitDie;
                numLevels--;
                if (numLevels == 0) {
                    continue;
                }
            }

            // Uses Pathfinder Society HP rules for the moment.
            hpbeep = (Math.ceil(cl.hitDie / 2) + 1) * numLevels + (conBonus * numLevels);
            baseHP += hpbeep;
        }

        this.hp = this.currentHp = baseHP;

        this.hp += getAdditionalHP();

        return baseHP;
    }

    /**
     * Sets the current hp value. Need to have some
     * way of tracking temporary hp, though.
     * <p/>
     * Returns false if you gave it too many hit points.
     * Otherwise returns true
     *
     * @param hpVal
     * @return Boolean
     */
    public Boolean setCurrentHP(long hpVal) {
        if (hpVal > this.hp) {
            this.currentHp = this.hp;
            return false;
        }

        this.currentHp = hpVal;

        SharedPreferences settings = _context.getSharedPreferences(
                Character.SETTINGS_KEY + this.name,
                0
        );
        settings.edit().putLong("CurrentHP", this.currentHp).commit();

        Registry reg = Registry.getInstance();
        reg.set("currentHP", this.currentHp);

        return true;
    }

    /**
     * Tries to load saved HP from the settings object,
     * defaults to maximum hp. Should only be called after
     * character classes have been added.
     */
    public void loadSavedHP() {
        SharedPreferences settings = _context.getSharedPreferences(
                Character.SETTINGS_KEY + this.name,
                0
        );

        this.currentHp = settings.getLong("CurrentHP", this.hp);
    }

    /**
     * Set from the AdjustStats activity, allows you to save
     * additional HP.
     *
     * @param hpVal
     */
    public void setAdditionalHP(long hpVal) {
        SharedPreferences settings = _context.getSharedPreferences(
                Character.SETTINGS_KEY + this.name,
                0
        );

        settings.edit().putLong("AdditionalHP", hpVal).commit();
    }

    public long getAdditionalHP() {
        SharedPreferences settings = _context.getSharedPreferences(
                Character.SETTINGS_KEY + this.name,
                0
        );

        long additionalHp = settings.getLong("AdditionalHP", 0);
        return additionalHp;
    }

    public void calculateAttacks() {
        long bab = 0;

        int classLen = this.characterClasses.size();
        CharacterClass cl;

        for (int i = 0; i < classLen; i++) {
            cl = this.characterClasses.get(i);
            bab += cl.getBAB();
        }

        this.bab = bab;
        long dexBonus = this.calculateBonus(this.modifiedAttributes.get("dexterity"));
        long strBonus = this.calculateBonus(this.modifiedAttributes.get("strength"));

        long atbonus = 0;

        atbonus += getAttributeBonus("Attack");

        this.rangedToHit = bab + dexBonus + atbonus;
        this.meleeToHit = bab + strBonus + atbonus;

        //TODO: Remember to add in Size and Misc bonuses.
        this.cmb = bab + strBonus;
        this.cmd = 10 + bab + strBonus + dexBonus;
    }

    /**
     * Calculates the saves, and applies any gear bonuses.
     *
     * @return
     */
    public HashMap<String, Number> calculateSaves() {
        int will = 0;
        int reflex = 0;
        int fort = 0;

        int classLen = this.characterClasses.size();
        CharacterClass cl;
        double hpbeep;

        for (int i = 0; i < classLen; i++) {
            cl = this.characterClasses.get(i);

            will += cl.getSave("will");
            reflex += cl.getSave("reflex");
            fort += cl.getSave("fort");
        }

        int saveBonus = getAttributeBonus("saves");

        will += calculateBonus(this.modifiedAttributes.get("wisdom")) + saveBonus;
        reflex += calculateBonus(this.modifiedAttributes.get("dexterity")) + saveBonus;
        fort += calculateBonus(this.modifiedAttributes.get("constitution")) + saveBonus;

        saves.put("will", will);
        saves.put("reflex", reflex);
        saves.put("fort", fort);

        modifiedSaves = saves;

        return saves;
    }

    public void gearUpdateAttributes() {
        for (String key : attributes.keySet()) {
            int attMod = 0;
            int newVal = attributes.get(key).intValue();

            attMod = getAttributeBonus(key);
            this.modifiedAttributes.put(key, newVal + attMod);
        }
    }

    /**
     * Figures out what the bonuse is across gear and active effects.
     *
     * @param toWhat
     * @return
     */
    public int getAttributeBonus(String toWhat) {
        int mod = 0;

        HashMap<String, Number> gearMap = getGearMap(toWhat);
        HashMap<String, Number> effectMap = getEffectMap(toWhat);

        Set<String> allKeys = new HashSet<String>();

        for (String key : gearMap.keySet()) {
            if (!allKeys.contains(key)) {
                allKeys.add(key);
            }
        }

        for (String key : effectMap.keySet()) {
            if (!allKeys.contains(key)) {
                allKeys.add(key);
            }
        }

        for (String key : allKeys) {
            long emod = 0;
            long gmod = 0;

            if (gearMap.containsKey(key)) {
                gmod += gearMap.get(key).longValue();
            }

            if (effectMap.containsKey(key)) {
                emod += effectMap.get(key).longValue();
            }

            if (Bonus.canBonusStack(key)) {
                mod += gmod + emod;
                continue;
            }

            mod += Math.max(gmod, emod);
        }

        return mod;
    }

    /**
     * Determines the bonus provided by an ability score.
     *
     * @param long score
     * @return
     */
    public long calculateBonus(Number score) {
        double rawScore = Math.ceil((score.longValue() - 10) / 2);
        if (score.longValue() - 10 < 0) {
            rawScore--;
        }

        return (long) rawScore;
    }

    protected HashMap<String, Number> getGearMap(String toWhat) {
        HashMap<String, Number> gearMap = new HashMap<String, Number>();

        toWhat = toWhat.toLowerCase();

        if (this.gear != null) {
            int gearLen = this.gear.size();

            for (int i = 0; i < gearLen; i++) {
                SlottedItem g = this.gear.get(i);
                ArrayList<JSONObject> bonusL = new ArrayList<JSONObject>();
                bonusL = g.getBonuses(toWhat);
                int lSize = bonusL.size();

                if (lSize <= 0) {
                    continue;
                }

                for (int j = 0; j < lSize; j++) {
                    JSONObject bonus = bonusL.get(j);
                    try {
                        String type = bonus.getString("name").toLowerCase();
                        long amt = bonus.getLong("howMuch");

                        long typeBonus = 0;

                        if (gearMap.containsKey(type)) {
                            typeBonus = gearMap.get(type).longValue();
                        }

                        Log.d("TypeBonus", String.valueOf(typeBonus));

                        if (Bonus.canBonusStack(type)) {
                            amt = amt + typeBonus;
                        }

                        if (typeBonus == 0 || typeBonus < amt) {
                            Log.d("typeBonus", "Setting " + type + " to " + String.valueOf(typeBonus));
                            typeBonus = amt;
                            gearMap.put(type, typeBonus);
                        }

                    } catch (JSONException e) {
                        Log.d("Fail", e.getMessage());
                    }
                }
            }
        }

        return gearMap;
    }

    protected HashMap<String, Number> getEffectMap(String toWhat) {
        HashMap<String, Number> effectMap = new HashMap<String, Number>();

        toWhat = toWhat.toLowerCase();

        if (this.activeEffects != null) {
            int gearLen = this.activeEffects.size();

            for (int i = 0; i < gearLen; i++) {
                ActiveEffect e = this.activeEffects.get(i);
                ArrayList<JSONObject> bonusL = new ArrayList<JSONObject>();
                bonusL = e.getBonuses(toWhat);
                int lSize = bonusL.size();

                if (lSize <= 0) {
                    continue;
                }

                for (int j = 0; j < lSize; j++) {
                    JSONObject bonus = bonusL.get(j);
                    try {
                        String type = bonus.getString("name").toLowerCase();
                        long amt = bonus.getLong("howMuch");

                        long typeBonus = 0;

                        if (effectMap.containsKey(type)) {
                            typeBonus = effectMap.get(type).longValue();
                        }

                        Log.d("TypeBonus", String.valueOf(typeBonus));

                        if (Bonus.canBonusStack(type)) {
                            amt = amt + typeBonus;
                        }

                        if (typeBonus == 0 || typeBonus < amt) {
                            Log.d("typeBonus", "Setting " + type + " to " + String.valueOf(typeBonus));
                            typeBonus = amt;
                            effectMap.put(type, typeBonus);
                        }

                    } catch (JSONException ex) {
                        Log.d("Fail", ex.getMessage());
                    }
                }
            }
        }

        return effectMap;
    }
}
