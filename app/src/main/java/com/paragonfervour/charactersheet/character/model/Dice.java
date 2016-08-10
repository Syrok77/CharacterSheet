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

    public static Dice diceFromValue(int value) {
        switch(value) {
            case 4: return D4;
            case 6: return D6;
            case 8: return D8;
            case 10: return D10;
            case 12: return D12;
            case 20: return D20;
//            case 100: return D100;
            default: return D4;
        }
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
