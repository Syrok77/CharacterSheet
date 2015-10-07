package com.paragonfervour.charactersheet.character.model;

/**
 * Model representing a Weapon, with a name (i.e. Rapier), damage, value, weight, and properties
 */
public class Weapon {

    private String mName;

    private int mValue;
    private int mWeight;

    private Damage mDamage;

    private String mProperties;

    public static Weapon createDefault() {
        Weapon w = new Weapon();
        w.mName = "Rapier";
        w.mValue = 25;
        w.mWeight = 2;
        w.mDamage = Damage.createDefaultWeapon();
        w.mProperties = "Finesse, Light";
        return w;
    }

    public static Weapon createOffhand() {
        Weapon w = new Weapon();
        w.mName = "Dagger";
        w.mValue = 2;
        w.mWeight = 1;
        w.mDamage = Damage.createDefaultOffhand();
        return w;
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
}
