package com.paragonfervour.charactersheet.character.model;


public enum Ability {
    STR("STR"),
    DEX("DEX"),
    CON("CON"),
    INT("INT"),
    WIS("WIS"),
    CHA("CHA"),
    ;

    private String mAbbr;
    Ability(String abbreviation) {
        mAbbr = abbreviation;
    }

    public static Ability abilityForAbbreviation(String abbreviation) {
        for (Ability ability : Ability.values()) {
            if (ability.mAbbr.equalsIgnoreCase(abbreviation)) {
                return ability;
            }
        }
        return Ability.STR;
    }

    public String getAbbreviation() {
        return mAbbr;
    }
}
