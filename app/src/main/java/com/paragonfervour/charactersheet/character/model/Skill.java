package com.paragonfervour.charactersheet.character.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a character's skill. A Skill has a name (i.e. Sneak or
 */
public class Skill {

    private String mName;
    private int mValue;

    /**
     * Create a default Skill model, mainly used in temporary test values.
     *
     * @return Default Skill (skilled at Stealth with 1 point of proficiency).
     */
    public static Skill createDefault() {
        Skill skill = new Skill();
        skill.setName("Stealth");
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
        skills.add(createDefault());

        Skill perception = new Skill();
        perception.setName("Perception");
        perception.setValue(2);
        skills.add(perception);

        return skills;
    }

    public Skill() {

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
}