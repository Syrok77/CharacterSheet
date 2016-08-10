package com.paragonfervour.charactersheet.character.helper;

import android.content.Context;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

/**
 * Helper class for all things Character related.
 */
public final class CharacterHelper {

    // cannot instantiate
    private CharacterHelper() {
    }

    public static String getToolbarTitle(Context context, GameCharacter character) {
        return character.getInfo().getName();
//        String format = context.getString(R.string.toolbar_character_format);
//        String level = stringRankifyValue(character.getInfo().getLevel());
//        return String.format(format, character.getInfo().getName(),
//                level,
//                character.getInfo().getCharacterClass());
    }

    /**
     * Return a 'ranked' version of an int as a String. 'Ranked' means that 1 would be come "1st",
     * 2 would become "2nd" etc.
     *
     * @param number number to stringify.
     * @return Formatted String of the number, represented as a ranking.
     */
    public static String stringRankifyValue(int number) {
        String value = String.valueOf(number);
        if (value.length() > 1) {
            // Check for special case: 11 - 13 are all "th".
            // So if the second to last digit is 1, it is "th".
            char secondToLastDigit = value.charAt(value.length() - 2);
            if (secondToLastDigit == '1')
                return value + "th";
        }
        char lastDigit = value.charAt(value.length() - 1);
        switch (lastDigit) {
            case '1':
                return value + "st";
            case '2':
                return value + "nd";
            case '3':
                return value + "rd";
            default:
                return value + "th";
        }
    }

    /**
     * Get displayable String for an XP amount.
     *
     * @param context Android context;
     * @param xp      XP amount
     * @return user's XP as a displayable String.
     */
    public static String getXpDisplayText(Context context, int xp) {
        String format = context.getString(R.string.nav_drawer_xp_button_text);
        return String.format(format, xp);
    }

    /**
     * Get the proficiency bonus for a character's level.
     *
     * @param characterLevel Character level, which determines the proficiency bonus value.
     * @return proficiency bonus for the given character level.
     */
    public static int getProficiencyBonus(int characterLevel) {
        int proficiency;

        if (characterLevel <= 0) {
            proficiency = 0;
        } else {
            proficiency = ((characterLevel - 1) / 4) + 2;
        }

        return  proficiency;
    }

}
