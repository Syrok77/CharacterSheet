package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a character's skill. A Skill has a name (i.e. Sneak or
 */
public class Skill extends SugarRecord {

    String mName;
    int mValue;

    Long mCharacterId;

    /**
     * Create a default Skill model, mainly used in temporary test values.
     *
     * @return Default Skill (skilled at Stealth with 1 point of proficiency).
     */
    public static Skill createDefault() {
        Skill skill = new Skill();
        skill.setName("stealth");
        skill.setValue(1);
        return skill;
    }

    /**
     * Create maldalair's skill list (for testing).
     *
     * @return a list of maldalair's skills.
     */
    public static List<Skill> createMaldalairList() {
        List<Skill> skills = new ArrayList<>();
        Skill skill = createDefault();
        skills.add(skill);

        Skill perception = new Skill();
        perception.setName("acrobatics");
        perception.setValue(2);
        skills.add(perception);

        return skills;
    }

    public Skill() {
    }

    /**
     * Get the "name" field name for SQL queries.
     *
     * @return StringUtils.toSQLName() for the "Name" field.
     */
    public static String getNameFieldSqlValue() {
        try {
            return NamingHelper.toSQLName(Skill.class.getDeclaredField("mName"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the "Character ID" field name for SQL queries.
     *
     * @return StringUtils.toSQLName() for the "Character Id" field.
     */
    public static String getCharacterIdFieldSqlValue() {
        try {
            return NamingHelper.toSQLName(Skill.class.getDeclaredField("mCharacterId"));
        } catch (Exception e) {
            return null;
        }
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public Long getCharacterId() {
        return mCharacterId;
    }

    public void setCharacterId(Long characterId) {
        mCharacterId = characterId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Skill) {
            Skill s = (Skill) o;
            return getName() != null &&
                    getName().equals(s.getName()) &&
                    getValue() == s.getValue();
        } else {
            return super.equals(o);
        }
    }

    /**
     * Check if this Skill is equal to given skill, ignoring any difference in skill level.
     *
     * @param other Other skill to check.
     * @return True if this skill is equal to other.
     */
    public boolean equalsIgnoreValue(Skill other) {
        return getName().equalsIgnoreCase(other.getName());
    }
}
