package com.paragonfervour.charactersheet.character.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Damage representation. Dice type, # of that dice, and modifier
 */
public class Damage implements Serializable {

    @SerializedName("DiceType")
    private Dice mDiceType;
    @SerializedName("DiceQuantity")
    private int mDiceQuantity;
    @SerializedName("Modifier")
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
     *
     * @return i.e. "2D4 + 2"
     */
    public String toString() {
        String quantity = mDiceQuantity > 1 ? String.valueOf(mDiceQuantity) : "";   // Do not show "1d4", should show "d4"
        return quantity + mDiceType.toString() + " + " + mModifier;
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
