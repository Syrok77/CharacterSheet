package com.paragonfervour.charactersheet.character.model;


import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

public class OffenseStats extends SugarRecord<OffenseStats> {

    @Ignore
    private List<Spell> mSpellList; // TODO: Remove this field and implement for realz (see Weapon or Skill).

    // This is just here so that Sugar saves this.
    // TODO: Maybe move all of this functionality up into GameCharacter,
    // since nothing is actually persisted here.
    String dummy = "DUMMY";


    public static OffenseStats createDefault() {
        OffenseStats os = new OffenseStats();

        os.mSpellList = new ArrayList<>();
        os.mSpellList.add(Spell.createDefault());

        return os;
    }

    public List<Weapon> getWeapons() {
        String varName = StringUtil.toSQLName("mOffenseStatId");
        return Weapon.find(Weapon.class, varName + " = ?", String.valueOf(getId()));
    }

    /**
     * Get a List of Weapons that are equipped in the main hand.
     *
     * @return a List<Weapon> containing Weapons for the main hand.
     */
    public List<Weapon> getMainHandWeapons() {
        String query = StringUtil.toSQLName("mOffenseStatId") + " = ? and " + StringUtil.toSQLName("isMainHand") + " = ?";
        return Weapon.find(Weapon.class, query, String.valueOf(getId()), "1");
    }

    /**
     * Get a List of Weapons that are equipped in the off hand.
     *
     * @return a List<Weapon> containing Weapons for the off hand.
     */
    public List<Weapon> getOffHandWeapons() {
        String query = StringUtil.toSQLName("mOffenseStatId") + " = ? and " + StringUtil.toSQLName("isMainHand") + " = ?";
        return Weapon.find(Weapon.class, query, String.valueOf(getId()), "0");
    }

    public List<Spell> getSpellList() {
        return mSpellList;
    }

    public void setSpellList(List<Spell> spellList) {
        mSpellList = spellList;
    }
}
