package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

/**
 * Damage representation. Dice type, # of that dice, and modifier
 */
public class Damage extends SugarRecord<Damage> {

    int mDiceTypeValue;
    int mDiceQuantity;
    int mModifier;

    public static Damage createDefaultWeapon() {
        Damage d = new Damage();
        d.setDiceType(Dice.D8);
        d.mDiceQuantity = 1;
        d.mModifier = 2;
        return d;
    }

    public static Damage createDefaultOffhand() {
        Damage d = new Damage();
        d.setDiceType(Dice.D4);
        d.mDiceQuantity = 1;
        d.mModifier = 2;
        return d;
    }

    /**
     * Represents this Damage as a string.
     *
     * @return i.e. "2D4 + 2"
     */
    public String toString() {
        String quantity = mDiceQuantity > 1 ? String.valueOf(mDiceQuantity) : "";   // Do not show "1d4", should show "d4"
        return quantity + getDiceType().toString() + " + " + mModifier;
    }

    public Dice getDiceType() {
        return Dice.diceFromValue(mDiceTypeValue);
    }

    public void setDiceType(Dice diceType) {
        mDiceTypeValue = diceType.getValue();
    }

    public int getDiceQuantity() {
        return mDiceQuantity;
    }

    public void setDiceQuantity(int diceQuantity) {
        mDiceQuantity = diceQuantity;
    }

    public int getModifier() {
        return mModifier;
    }

    public void setModifier(int modifier) {
        mModifier = modifier;
    }
}
