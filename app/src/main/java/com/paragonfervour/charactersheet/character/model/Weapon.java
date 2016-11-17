package com.paragonfervour.charactersheet.character.model;

import com.orm.SugarRecord;

/**
 * Model representing a Weapon, with a name (i.e. Rapier), damage, value, weight, and properties
 */
@SuppressWarnings("WeakerAccess")
public class Weapon extends SugarRecord {

    String mName;

    int mValue;
    int mWeight;

    Damage mDamage;

    String mProperties;

    boolean isMainHand;
    boolean isStrengthWeapon;

    Long mCharacterId;

    public static Weapon createDefault() {
        Weapon w = new Weapon();
        w.mName = "Short Sword";
        w.mValue = 25;
        w.mWeight = 2;
        w.mDamage = Damage.createDefaultWeapon();
        w.mProperties = "Light";
        w.isMainHand = true;
        w.isStrengthWeapon = true;
        return w;
    }

    public static Weapon createOffhand() {
        Weapon w = new Weapon();
        w.mName = "Dagger";
        w.mValue = 2;
        w.mWeight = 1;
        w.mDamage = Damage.createDefaultOffhand();
        w.isMainHand = false;
        w.isStrengthWeapon = false;
        return w;
    }

    public Weapon() {
    }

    @Override
    public long save() {
        mDamage.save();
        return super.save();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public Damage getDamage() {
        return mDamage;
    }

    public void setDamage(Damage damage) {
        mDamage = damage;
    }

    public String getProperties() {
        return mProperties;
    }

    public void setProperties(String properties) {
        mProperties = properties;
    }

    public boolean isMainHand() {
        return isMainHand;
    }

    public void setIsMainHand(boolean isMainHand) {
        this.isMainHand = isMainHand;
    }

    public boolean isStrengthWeapon() {
        return isStrengthWeapon;
    }

    public void setIsStrengthWeapon(boolean isStrengthWeapon) {
        this.isStrengthWeapon = isStrengthWeapon;
    }

    public Long getCharacterId() {
        return mCharacterId;
    }

    public void setCharacterId(Long characterId) {
        mCharacterId = characterId;
    }
}
