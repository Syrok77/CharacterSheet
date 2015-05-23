package com.paragonfervour.charactersheet.model;

/**
 * Damage representation. Dice type, # of that dice, and modifier
 */
public class Damage {
    private Dice mDiceType;
    private int mDiceQuantity;
    private int mModifier;

    public static Damage createDefaultWeapon() {
        Damage d = new Damage();
        d.mDiceType = Dice.D8;
        d.mDiceQuantity = 1;
        d.mModifier = 2;
        return d;
    }

    public static Damage createDefaultOffhand() {
        Damage d = new Damage();
        d.mDiceType = Dice.D4;
        d.mDiceQuantity = 1;
        d.mModifier = 2;
        return d;
    }

    /**
     * Represents this Damage as a string.
     * @return i.e. "2D4 + 2"
     */
    public String toString() {
        return String.valueOf(mDiceQuantity) + mDiceType.toString() + " + " + mModifier;
    }

    public Dice getDiceType() {
        return mDiceType;
    }

    public void setDiceType(Dice diceType) {
        mDiceType = diceType;
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
