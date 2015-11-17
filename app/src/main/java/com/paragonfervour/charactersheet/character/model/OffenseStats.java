package com.paragonfervour.charactersheet.character.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OffenseStats {

    @SerializedName("Weapons")
    private List<Weapon> mWeapons;

    @SerializedName("SpellList")
    private List<Spell> mSpellList;


    public static OffenseStats createDefault() {
        OffenseStats os = new OffenseStats();

        os.mWeapons = new ArrayList<>();
        os.mWeapons.add(Weapon.createDefault());
        os.mWeapons.add(Weapon.createOffhand());

        os.mSpellList = new ArrayList<>();
        os.mSpellList.add(Spell.createDefault());

        return os;
    }

    public List<Weapon> getWeapons() {
        return mWeapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        mWeapons = weapons;
    }

    /**
     * Get a List of Weapons that are equipped in the main hand.
     *
     * @return a List<Weapon> containing Weapons for the main hand.
     */
    public List<Weapon> getMainHandWeapons() {
        List<Weapon> list = new ArrayList<>();
        for (Weapon w : mWeapons) {
            if (w.isMainHand()) {
                list.add(w);
            }
        }
        return list;
    }

    /**
     * Get a List of Weapons that are equipped in the off hand.
     *
     * @return a List<Weapon> containing Weapons for the off hand.
     */
    public List<Weapon> getOffHandWeapons() {
        List<Weapon> list = new ArrayList<>();
        for (Weapon w : mWeapons) {
            if (!w.isMainHand()) {
                list.add(w);
            }
        }
        return list;
    }

    public List<Spell> getSpellList() {
        return mSpellList;
    }

    public void setSpellList(List<Spell> spellList) {
        mSpellList = spellList;
    }
}
