package com.paragonfervour.charactersheet.model;

/**
 * Hierarchy of models that contains all the data of a D&D Character.
 * TODO: Make this gson compliant
 */
public class GameCharacter {
    // Name, class, race, level, xp
    private CharacterInfo mInfo;

    // Defense scores; AC, saves, hit points, hit die
    private DefenseStats mDefenseStats;

    // Offense - weapons, spells
    private OffenseStats mOffenseStats;

    // Bio - background, bond/ideal/etc.
    private BioInfo mBioInfo;

    // Temp - create a default character
    public static GameCharacter createDefaultCharacter() {
        GameCharacter maldalair = new GameCharacter();
        maldalair.mInfo = CharacterInfo.createDefault();
        maldalair.mDefenseStats = DefenseStats.createDefault();
        maldalair.mOffenseStats = OffenseStats.createDefault();
        maldalair.mBioInfo = BioInfo.createDefault();
        return maldalair;
    }

    public CharacterInfo getInfo() {
        return mInfo;
    }

    public void setInfo(CharacterInfo info) {
        mInfo = info;
    }

    public DefenseStats getDefenseStats() {
        return mDefenseStats;
    }

    public void setDefenseStats(DefenseStats defenseStats) {
        mDefenseStats = defenseStats;
    }

    public OffenseStats getOffenseStats() {
        return mOffenseStats;
    }

    public void setOffenseStats(OffenseStats offenseStats) {
        mOffenseStats = offenseStats;
    }

    public BioInfo getBioInfo() {
        return mBioInfo;
    }

    public void setBioInfo(BioInfo bioInfo) {
        mBioInfo = bioInfo;
    }
}
