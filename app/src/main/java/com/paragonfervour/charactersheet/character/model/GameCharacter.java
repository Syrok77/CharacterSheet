package com.paragonfervour.charactersheet.character.model;

import com.orm.StringUtil;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Hierarchy of models that contains all the data of a D&D Character.
 */
public class GameCharacter extends SugarRecord<GameCharacter> {
    // Name, class, race, level, xp
    CharacterInfo mInfo;

    // Defense scores; AC, saves, hit points, hit die
    DefenseStats mDefenseStats;

    // Offense - weapons, spells
    OffenseStats mOffenseStats;

    // Bio - background, bond/ideal/etc.
    BioInfo mBioInfo;

    boolean isInspired;
    int mSpeed;

    // Temp - create a default character
    public static GameCharacter createDefaultCharacter() {
        GameCharacter maldalair = new GameCharacter();
        maldalair.mInfo = CharacterInfo.createDefault();
        maldalair.mDefenseStats = DefenseStats.createMaldalair();
        maldalair.mOffenseStats = OffenseStats.createDefault();
        maldalair.mBioInfo = BioInfo.createDefault();
        maldalair.isInspired = false;
        maldalair.mSpeed = 30;
        return maldalair;
    }

    @Override
    public void save() {
        super.save();
        // Save sub models
        mBioInfo.save();
        mInfo.save();
        mDefenseStats.save();
        mOffenseStats.save();
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
        String query = StringUtil.toSQLName("mCharacterId") + " = ?";
        return Skill.find(Skill.class, query, String.valueOf(getId()));
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
