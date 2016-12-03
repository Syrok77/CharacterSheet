package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Model represents a Spell.
 * Spell name, level, description, casting time, components, range
 */
@SuppressWarnings("WeakerAccess")
public class Spell extends SugarRecord {

    String mName;
    int mLevel;
    String mDescription;
    String mCastingTime;
    String mComponents;
    String mDuration;
    String mRange;

    Long mCharacterId;

    public Spell() {
    }

    public Spell(String name,
                 int level,
                 String description,
                 String castingTime,
                 String components,
                 String duration,
                 String range,
                 long characterId) {
        mName = name;
        mLevel = level;
        mDescription = description;
        mCastingTime = castingTime;
        mComponents = components;
        mDuration = duration;
        mRange = range;
        mCharacterId = characterId;
    }

    public static Spell createDefault() {
        Spell s = new Spell();
        s.mName = "Cloud of Daggers";
        s.mLevel = 0;
        s.mCastingTime = "1 action";
        s.mRange = "60 feet";
        s.mComponents = "V, S, M (a sliver of glass)";
        s.mDuration = "Concentration, up to 1 minute";
        s.mDescription = "You fill the air with spinning daggers in a cube 5 feet on each side, centered....";
        return s;
    }

    public static Spell createMagicMissile() {
        Spell s = new Spell();
        s.mName = "Magic Missile";
        s.mLevel = 1;
        s.mCastingTime = "1 action";
        s.mRange = "60 feet";
        s.mComponents = "V, S";
        s.mDuration = "instant";
        s.mDescription = "MAGIC MISSILE!";
        return s;
    }

    public static List<Spell> createMaldalairList() {
        List<Spell> spells = new ArrayList<>();
        spells.add(createDefault());
        spells.add(createMagicMissile());
        return spells;
    }

    public String getName() {
        return mName;
    }

    public int getLevel() {
        return mLevel;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCastingTime() {
        return mCastingTime;
    }

    public String getComponents() {
        return mComponents;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getRange() {
        return mRange;
    }

    public Long getCharacterId() {
        return mCharacterId;
    }

    public void setCharacterId(Long characterId) {
        mCharacterId = characterId;
    }
}
