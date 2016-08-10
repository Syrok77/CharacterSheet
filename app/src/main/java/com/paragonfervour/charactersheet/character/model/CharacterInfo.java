package com.paragonfervour.charactersheet.character.model;


import com.orm.SugarRecord;

/**
 * Character info model, containing name, class, level, XP, and race info.
 */
public class CharacterInfo extends SugarRecord {

    String mCharacterClass; // TODO: Create a CharacterClass model?
    String mRace;
    int mLevel;
    String mName;
    int mXp;

    public static CharacterInfo createDefault() {
        CharacterInfo info = new CharacterInfo();
        info.mCharacterClass = "Rogue";
        info.mRace = "Elf";
        info.mLevel = 4;
        info.mName = "Maldalair";
        info.mXp = 4500;
        return info;
    }


    public String getCharacterClass() {
        return mCharacterClass;
    }

    public void setCharacterClass(String mCharacterClass) {
        this.mCharacterClass = mCharacterClass;
    }

    public String getRace() {
        return mRace;
    }

    public void setRace(String mRace) {
        this.mRace = mRace;
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

    public int getXp() {
        return mXp;
    }

    public void setXp(int xp) {
        this.mXp = xp;
    }
}
