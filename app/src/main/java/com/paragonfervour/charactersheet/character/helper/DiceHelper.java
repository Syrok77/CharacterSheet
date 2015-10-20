package com.paragonfervour.charactersheet.character.helper;

import android.support.annotation.DrawableRes;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Dice;

/**
 * Helper methods when dealing with {@link Dice}.
 */
public final class DiceHelper {

    private DiceHelper() {
        // cannot be instantiated.
    }

    /**
     * Get the icon drawable res for the given dice.
     *
     * @param dice Dice to find icon for.
     * @return drawable res for the dice's icon.
     */
    @DrawableRes
    public static int getDiceDrawable(Dice dice) {
        switch(dice) {
            case D4:
                return R.drawable.d4_black_24dp;
            case D6:
                return R.drawable.d6_black_24dp;
            case D8:
                return R.drawable.d8_black_24dp;
            case D10:
                return R.drawable.d10_black_24dp;
            case D12:
                return R.drawable.d12_black_24dp;
            case D20:
                return R.drawable.d20_black_24dp;
            default:
                return 0;
        }
    }

}
