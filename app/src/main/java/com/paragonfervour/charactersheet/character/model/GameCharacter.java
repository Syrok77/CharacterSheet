package com.paragonfervour.charactersheet.character.model;

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
        maldalair.mDefenseStats = DefenseStats.createMaldalair();
        maldalair.mOffenseStats = OffenseStats.createDefault();
        maldalair.mBioInfo = BioInfo.createDefault();
        maldalair.mSkills = Skill.createMaldalairList();
        return maldalair;
    }

    public CharacterInfo getInfo() {
        return mInfo;
    }

    public DefenseStats getDefenseStats() {
        return mDefenseStats;
    }

    public OffenseStats getOffenseStats() {
        return mOffenseStats;
    }

    public BioInfo getBioInfo() {
        return mBioInfo;
    }

    public List<Skill> getSkills() {
        return mSkills;
    }

    public void setSkills(List<Skill> skills) {
        mSkills = skills;
    }
}
