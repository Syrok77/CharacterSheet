package com.paragonfervour.charactersheet.character.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Hierarchy of models that contains all the data of a D&D Character.
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

    // Skill list
    private List<Skill> mSkills;

    // Temp - create a default character
    public static GameCharacter createDefaultCharacter() {
        GameCharacter maldalair = new GameCharacter();
        maldalair.mInfo = CharacterInfo.createDefault();
        maldalair.mDefenseStats = DefenseStats.createDefault();
        maldalair.mOffenseStats = OffenseStats.createDefault();
        maldalair.mBioInfo = BioInfo.createDefault();
        maldalair.mSkills = new ArrayList<>();
        maldalair.mSkills.add(Skill.createDefault());
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

    public List<Skill> getSkills() {
        return mSkills;
    }

    public void setSkills(List<Skill> skills) {
        mSkills = skills;
    }
}
