package com.paragonfervour.charactersheet.character.model;


import com.google.gson.annotations.SerializedName;

public class CharacterInfo {

    @SerializedName("CharacterClass")
    private String mCharacterClass; // TODO: Create a CharacterClass model?
    @SerializedName("Race")
    private String mRace;
    @SerializedName("Level")
    private int mLevel;
    @SerializedName("Name")
    private String mName;
    @SerializedName("XP")
    private int mXp;

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
