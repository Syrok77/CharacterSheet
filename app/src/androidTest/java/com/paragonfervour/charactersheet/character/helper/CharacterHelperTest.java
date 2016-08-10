package com.paragonfervour.charactersheet.character.helper;

import junit.framework.TestCase;


public class CharacterHelperTest extends TestCase {

    public void testProficiencyBonus() {
        // Normal cases
        assertEquals(2, CharacterHelper.getProficiencyBonus(1));
        assertEquals(2, CharacterHelper.getProficiencyBonus(2));
        assertEquals(2, CharacterHelper.getProficiencyBonus(3));
        assertEquals(2, CharacterHelper.getProficiencyBonus(4));
        assertEquals(3, CharacterHelper.getProficiencyBonus(5));
        assertEquals(3, CharacterHelper.getProficiencyBonus(6));
        assertEquals(3, CharacterHelper.getProficiencyBonus(7));
        assertEquals(3, CharacterHelper.getProficiencyBonus(8));
        assertEquals(4, CharacterHelper.getProficiencyBonus(9));
        assertEquals(4, CharacterHelper.getProficiencyBonus(10));
        assertEquals(4, CharacterHelper.getProficiencyBonus(11));
        assertEquals(4, CharacterHelper.getProficiencyBonus(12));
        assertEquals(5, CharacterHelper.getProficiencyBonus(13));
        assertEquals(5, CharacterHelper.getProficiencyBonus(14));
        assertEquals(5, CharacterHelper.getProficiencyBonus(15));
        assertEquals(5, CharacterHelper.getProficiencyBonus(16));
        assertEquals(6, CharacterHelper.getProficiencyBonus(17));
        assertEquals(6, CharacterHelper.getProficiencyBonus(18));
        assertEquals(6, CharacterHelper.getProficiencyBonus(19));
        assertEquals(6, CharacterHelper.getProficiencyBonus(20));
    }

    public void testProficiencyBonusLevelZero() {
        // Character level 0 and below
        assertEquals(0, CharacterHelper.getProficiencyBonus(0));
        assertEquals(0, CharacterHelper.getProficiencyBonus(-1));
        assertEquals(0, CharacterHelper.getProficiencyBonus(-10));
    }

    public void testProficiencyBonusAboveTwenty() {
        // Character above level 20. What do we do here?
        assertEquals(7, CharacterHelper.getProficiencyBonus(21));
    }

}