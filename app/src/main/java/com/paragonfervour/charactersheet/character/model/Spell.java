package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

/**
 * Model represents a Spell.
 * Spell name, level, description, casting time, components, range
 */
public class Spell extends SugarRecord {

    String mName;
    int mLevel;
    String mDescription;
    String mCastingTime;
    String mComponents;
    String mDuration;
    String mRange;

    public static Spell createDefault() {
        Spell s = new Spell();
        s.mName = "Cloud of Daggers";
        s.mCastingTime = "1 action";
        s.mRange = "60 feet";
        s.mComponents = "V, S, M (a sliver of glass)";
        s.mDuration = "Concentration, up to 1 minute";
        s.mDescription = "You fill the air with spinning daggers in a cube 5 feet on each side, centered....";
        return s;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getCastingTime() {
        return mCastingTime;
    }

    public void setCastingTime(String castingTime) {
        mCastingTime = castingTime;
    }

    public String getComponents() {
        return mComponents;
    }

    public void setComponents(String components) {
        mComponents = components;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getRange() {
        return mRange;
    }

    public void setRange(String range) {
        mRange = range;
    }
}
