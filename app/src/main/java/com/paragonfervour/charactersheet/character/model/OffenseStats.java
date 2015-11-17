package com.paragonfervour.charactersheet.character.model;


import com.orm.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

public class OffenseStats extends SugarRecord<OffenseStats> {

    @Ignore
    private List<Weapon> mWeapons;

    @Ignore
    private List<Spell> mSpellList;

    // This is just here so that Sugar saves this.
    // TODO: Maybe move all of this functionality up into GameCharacter,
    // since nothing is actually persisted here.
    String dummy = "DUMMY";


    public static OffenseStats createDefault() {
        OffenseStats os = new OffenseStats();

        Weapon main = Weapon.createDefault();
        Weapon off = Weapon.createOffhand();

        os.mWeapons = new ArrayList<>();
        os.mWeapons.add(main);
        os.mWeapons.add(off);

        os.mSpellList = new ArrayList<>();
        os.mSpellList.add(Spell.createDefault());

        return os;
    }

    @Override
    public void save() {
        super.save();
        for (Weapon w : getWeapons()) {
            w.setOffenseStatId(getId());
            w.save();
        }
    }

    public List<Weapon> getWeapons() {
        if (mWeapons == null) {
            String varName = StringUtil.toSQLName("mOffenseStatId");
            mWeapons = Weapon.find(Weapon.class, varName + " = ?", String.valueOf(getId()));
        }

        return mWeapons;
    }

    /**
     * Get a List of Weapons that are equipped in the main hand.
     *
     * @return a List<Weapon> containing Weapons for the main hand.
     */
    public List<Weapon> getMainHandWeapons() {
        String query = StringUtil.toSQLName("mOffenseStatId") + " = ? and " + StringUtil.toSQLName("isMainHand")+ " = ?";
        return Weapon.find(Weapon.class, query, String.valueOf(getId()), "1");
    }

    /**
     * Get a List of Weapons that are equipped in the off hand.
     *
     * @return a List<Weapon> containing Weapons for the off hand.
     */
    public List<Weapon> getOffHandWeapons() {
        String query = StringUtil.toSQLName("mOffenseStatId") + " = ? and " + StringUtil.toSQLName("isMainHand")+ " = ?";
        return Weapon.find(Weapon.class, query, String.valueOf(getId()), "0");
    }

    public List<Spell> getSpellList() {
        return mSpellList;
    }

    public void setSpellList(List<Spell> spellList) {
        mSpellList = spellList;
    }
}
