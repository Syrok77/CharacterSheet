package com.paragonfervour.charactersheet.stats.helper;


import android.content.Context;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Skill;

import java.util.List;

public final class StatHelper {

    private StatHelper() {
    }

    /**
     * Get the +/- indicator for a stat. Will be + if > 0, - if < 0 and ' ' if == 0.
     *
     * @param modifier stat modifier for the indicator.
     * @return the negative/positive indicator for the passed in stat.
     */
    public static String getStatIndicator(int modifier) {
        String indicator;

        if (modifier > 0) {
            indicator = "+";
        } else if (modifier < 0) {
            indicator = "-";
        } else {
            indicator = " ";
        }

        return indicator;
    }

    /**
     * Get the modifier value for a given ability score.
     *
     * @param score ability score.
     * @return modifier value.
     */
    public static int getScoreModifier(int score) {
        double mod = ((double) score - 10d) / 2d;
        // Always round this value down. Integer division rounds towards zero, not down.
        return (int) Math.floor(mod);
    }

    /**
     * Get the modifier displayable text, in the form of "+/-MOD".
     *
     * @param score ability score.
     * @return ability score modifier formatted string.
     */
    public static String getScoreModifierString(int score) {
        int scoreMod = getScoreModifier(score);
        return getStatIndicator(scoreMod) + Math.abs(scoreMod);
    }

    /**
     * Create formatted text that displays the character's speed value.
     *
     * @param context Android context.
     * @param speed   Character speed.
     * @return Formatted display string for speed.
     */
    public static String makeSpeedText(Context context, int speed) {
        return String.format(context.getString(R.string.stat_speed_format), speed);
    }

    /**
     * Create formatted text that displays the character's initiative value.
     *
     * @param context   Android context.
     * @param dexterity Character's dexterity, which determines a character's initiative.
     * @return Formatted display string for initiative.
     */
    public static String makeInitiativeText(Context context, int dexterity) {
        return String.format(context.getString(R.string.stat_initiative_format), getScoreModifierString(dexterity));
    }

    /**
     * Create formatted text that displays the character's passive wisdom attribute, which is
     * WIS + perception skill. TODO: Does this also include the character's proficiency bonus?
     *
     * @param context Android context.
     * @param skills  Character's skill list.
     * @param wisdom  Character's WIS score.
     * @return Formatted display string for user's passive Wisdom.
     */
    public static String makePassiveWisdomText(Context context, List<Skill> skills, int wisdom) {
        Skill perception = null;
        String skillName;

        String perceptionName = context.getString(R.string.skill_name_perception);
        for (Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase(perceptionName)) {
                perception = skill;
            }
        }

        int passiveWisdom = getScoreModifier(wisdom);
        if (perception != null) {
            passiveWisdom += perception.getValue();
            skillName = perceptionName;
        } else {
            skillName = context.getString(R.string.skill_name_wisdom);
        }

        String format = context.getString(R.string.stat_skill_passive_wisdom_format);
        return String.format(format, skillName, getStatIndicator(passiveWisdom), Math.abs(passiveWisdom));
    }
}
