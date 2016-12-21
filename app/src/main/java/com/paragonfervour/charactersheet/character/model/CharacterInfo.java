package com.paragonfervour.charactersheet.character.model;


import com.orm.SugarRecord;

/**
 * Character info model, containing name, class, level, XP, and race info.
 */
@SuppressWarnings("WeakerAccess")
public class CharacterInfo extends SugarRecord {

    int mCastingAbility;
    String mCharacterClass; // TODO: Create a CharacterClass model?
    int mLevel;
    String mName;
    String mRace;
    int mXp;

    public static CharacterInfo createDefault() {
        CharacterInfo info = new CharacterInfo();
        info.mCharacterClass = "Rogue";
        info.mRace = "Elf";
        info.mLevel = 2;
        info.mName = "Frédéric Lacroix";
        info.mXp = 435;
        info.mCastingAbility = Ability.CHA.ordinal();
        return info;
    }

    public CharacterInfo() {
    }

    public int getCastingAbility() {
        return mCastingAbility;
    }

    public void setCastingAbility(int castingAbility) {
        mCastingAbility = castingAbility;
    }

    public String getCharacterClass() {
        return mCharacterClass;
    }

    public void setCharacterClass(String mCharacterClass) {
        this.mCharacterClass = mCharacterClass;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getRace() {
        return mRace;
    }

    public void setRace(String mRace) {
        this.mRace = mRace;
    }

    public int getXp() {
        return mXp;
    }

    public void setXp(int xp) {
        this.mXp = xp;
    }
}
