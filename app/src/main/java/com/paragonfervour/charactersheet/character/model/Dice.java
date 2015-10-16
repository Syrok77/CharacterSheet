package com.paragonfervour.charactersheet.character.model;


import java.util.Random;

public enum Dice {
    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D20(20),
    D100(100);

    private int mValue;

    Dice(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    /**
     * @return D[value] i.e. "D4"
     */
    public String toString() {
        return "D" + getValue();
    }

    /**
     * Roll the dice!
     *
     * @return random value [1, value].
     */
    public int roll() {
        Random roller = new Random(System.currentTimeMillis());
        return roller.nextInt(mValue) + 1;
    }
}
