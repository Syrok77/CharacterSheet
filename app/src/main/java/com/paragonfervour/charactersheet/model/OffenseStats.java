package com.paragonfervour.charactersheet.model;


import java.util.ArrayList;
import java.util.List;

public class OffenseStats {

    private Weapon mMainHand;
    private Weapon mOffHand;

    private List<Weapon> mWeapons;
    private List<Spell> mSpellList;


    public static OffenseStats createDefault() {
        OffenseStats os = new OffenseStats();
        os.mMainHand = Weapon.createDefault();
        os.mOffHand = Weapon.createOffhand();

        os.mWeapons = new ArrayList<>();
        os.mWeapons.add(os.mMainHand);
        os.mWeapons.add(os.mOffHand);

        os.mSpellList = new ArrayList<>();
        os.mSpellList.add(Spell.createDefault());

        return os;
    }

    public Weapon getMainHand() {
        return mMainHand;
    }

    public void setMainHand(Weapon mainHand) {
        mMainHand = mainHand;
    }

    public Weapon getOffHand() {
        return mOffHand;
    }

    public void setOffHand(Weapon offHand) {
        mOffHand = offHand;
    }

    public List<Weapon> getWeapons() {
        return mWeapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        mWeapons = weapons;
    }

    public List<Spell> getSpellList() {
        return mSpellList;
    }

    public void setSpellList(List<Spell> spellList) {
        mSpellList = spellList;
    }
}
