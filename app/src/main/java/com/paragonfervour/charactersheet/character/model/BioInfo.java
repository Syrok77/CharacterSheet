package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

/**
 * Model contains background information and character traits.
 * TODO: Unimplemented
 */
@SuppressWarnings("WeakerAccess")
public class BioInfo extends SugarRecord {

    String dummy = "DUMMY";

    public static BioInfo createDefault() {
        return new BioInfo();
    }
}
