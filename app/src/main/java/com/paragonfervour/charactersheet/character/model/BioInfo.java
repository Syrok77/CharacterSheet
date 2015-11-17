package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

/**
 * Model contains background information and character traits.
 * TODO: Unimplemented
 */
public class BioInfo extends SugarRecord<BioInfo> {

    String dummy = "DUMMY";

    public static BioInfo createDefault() {
        return new BioInfo();
    }
}
