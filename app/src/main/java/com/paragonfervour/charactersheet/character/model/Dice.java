package com.paragonfervour.charactersheet.character.model;


import java.util.Random;

public enum Dice {
    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20);
//    D100(100);

    private final int mValue;

    Dice(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    /**
     * @return d[value] i.e. "d4"
     */
    public String toString() {
        return "d" + getValue();
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
