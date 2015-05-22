package com.paragonfervour.charactersheet.model;


public class CharacterInfo {
    private String mCharacterClass; // TODO: Create a CharacterClass model
    private int level;

    private String name;
    private int xp;


    public String getCharacterClass() {
        return mCharacterClass;
    }

    public void setCharacterClass(String mCharacterClass) {
        this.mCharacterClass = mCharacterClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
