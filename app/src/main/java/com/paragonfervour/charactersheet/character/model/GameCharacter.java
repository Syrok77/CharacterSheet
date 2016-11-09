package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Hierarchy of models that contains all the data of a D&D Character.
 */
@SuppressWarnings("WeakerAccess")
public class GameCharacter extends SugarRecord {

    // Name, class, race, level, xp
    CharacterInfo mInfo;

    // Defense scores; AC, saves, hit points, hit die
    DefenseStats mDefenseStats;

    // Bio - background, bond/ideal/etc.
    BioInfo mBioInfo;

    boolean isInspired;
    int mSpeed;

    // Temp - create a default character
    public static GameCharacter createDefaultCharacter() {
        GameCharacter maldalair = new GameCharacter();
        maldalair.mInfo = CharacterInfo.createDefault();
        maldalair.mDefenseStats = DefenseStats.createMaldalair();
        maldalair.mBioInfo = BioInfo.createDefault();
        maldalair.isInspired = false;
        maldalair.mSpeed = 30;
        return maldalair;
    }

    @Override
    public long save() {
        long id = super.save();
        // Save sub models
        mBioInfo.save();
        mInfo.save();
        mDefenseStats.save();
        return id;
    }

    /**
     * Get a list of all Weapons this Character has equipped. This is both main-hand and off-hand weapons.
     *
     * @return a List of all the character's Weapons.
     */
    public List<Weapon> getWeapons() {
        try {
            String varName = NamingHelper.toSQLName(Weapon.class.getDeclaredField("mCharacterId"));
            return Weapon.find(Weapon.class, varName + " = ?", String.valueOf(getId()));
        } catch (NoSuchFieldException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Get a List of Weapons that are equipped in the main hand.
     *
     * @return a List<Weapon> containing Weapons for the main hand.
     */
    public List<Weapon> getMainHandWeapons() {
        try {
            String query = NamingHelper.toSQLName(Weapon.class.getDeclaredField("mCharacterId")) + " = ? " +
                    "and " + NamingHelper.toSQLName(Weapon.class.getDeclaredField("isMainHand")) + " = ?";
            return Weapon.find(Weapon.class, query, String.valueOf(getId()), "1");
        } catch (NoSuchFieldException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Get a List of Weapons that are equipped in the off hand.
     *
     * @return a List<Weapon> containing Weapons for the off hand.
     */
    public List<Weapon> getOffHandWeapons() {
        try {
            String query = NamingHelper.toSQLName(Weapon.class.getDeclaredField("mCharacterId")) + " = ? " +
                    "and " + NamingHelper.toSQLName(Weapon.class.getDeclaredField("isMainHand")) + " = ?";
            return Weapon.find(Weapon.class, query, String.valueOf(getId()), "0");
        } catch (NoSuchFieldException e) {
            return new ArrayList<>();
        }
    }

    public CharacterInfo getInfo() {
        return mInfo;
    }

    public DefenseStats getDefenseStats() {
        return mDefenseStats;
    }

    public BioInfo getBioInfo() {
        return mBioInfo;
    }

    public List<Skill> getSkills() {
        try {
            String query = NamingHelper.toSQLName(Weapon.class.getDeclaredField("mCharacterId")) + " = ?";
            return Skill.find(Skill.class, query, String.valueOf(getId()));
        } catch (NoSuchFieldException e) {
            return new ArrayList<>();
        }
    }

    public boolean isInspired() {
        return isInspired;
    }

    public void setIsInspired(boolean isInspired) {
        this.isInspired = isInspired;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }
}
