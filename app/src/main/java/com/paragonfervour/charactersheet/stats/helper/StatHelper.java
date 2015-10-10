package com.paragonfervour.charactersheet.stats.helper;


public class StatHelper {

    private StatHelper() {}

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

}
