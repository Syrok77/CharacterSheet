package com.paragonfervour.charactersheet.character.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model representing a Weapon, with a name (i.e. Rapier), damage, value, weight, and properties
 */
public class Weapon implements Serializable {

    @SerializedName("Name")
    private String mName;

    @SerializedName("Value")
    private int mValue;
    @SerializedName("Weight")
    private int mWeight;

    @SerializedName("Damage")
    private Damage mDamage;

    @SerializedName("Properties")
    private String mProperties;

    public static Weapon createDefault() {
        Weapon w = new Weapon();
        w.mName = "Short Sword";
        w.mValue = 25;
        w.mWeight = 2;
        w.mDamage = Damage.createDefaultWeapon();
        w.mProperties = "Light";
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
