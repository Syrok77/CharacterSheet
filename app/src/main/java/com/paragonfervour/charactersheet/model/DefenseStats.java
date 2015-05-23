package com.paragonfervour.charactersheet.model;


public class DefenseStats {

    private int mAC;

    // saves
    private int mStrSave;
    private int mDexSave;
    private int mConSave;
    private int mIntSave;
    private int mWisSave;
    private int mChaSave;

    // HP
    private int mHitPoints;
    private int mTempHp;
    private int mMaxHp;
    private Dice mHitDice;

    // death saves
    private int mSuccessAttempts;
    private int mFailAttempts;

    // Create default Defense Stats
    public static DefenseStats createDefault() {
        DefenseStats d = new DefenseStats();
        d.mAC = 16;
        d.mStrSave = 10;
        d.mDexSave = 10;
        d.mConSave = 10;
        d.mIntSave = 10;
        d.mWisSave = 10;
        d.mChaSave = 10;

        d.mHitPoints = 20;
        d.mTempHp = 0;
        d.mMaxHp = 24;
        d.mHitDice = Dice.D8;

        d.mSuccessAttempts = 0;
        d.mFailAttempts = 0;

        return d;
    }

    public int getAC() {
        return mAC;
    }

    public void setAC(int AC) {
        mAC = AC;
    }

    public int getStrSave() {
        return mStrSave;
    }

    public void setStrSave(int strSave) {
        mStrSave = strSave;
    }

    public int getDexSave() {
        return mDexSave;
    }

    public void setDexSave(int dexSave) {
        mDexSave = dexSave;
    }

    public int getConSave() {
        return mConSave;
    }

    public void setConSave(int conSave) {
        mConSave = conSave;
    }

    public int getIntSave() {
        return mIntSave;
    }

    public void setIntSave(int intSave) {
        mIntSave = intSave;
    }

    public int getWisSave() {
        return mWisSave;
    }

    public void setWisSave(int wisSave) {
        mWisSave = wisSave;
    }

    public int getChaSave() {
        return mChaSave;
    }

    public void setChaSave(int chaSave) {
        mChaSave = chaSave;
    }

    public int getHitPoints() {
        return mHitPoints;
    }

    public void setHitPoints(int hitPoints) {
        mHitPoints = hitPoints;
    }

    public int getTempHp() {
        return mTempHp;
    }

    public void setTempHp(int tempHp) {
        mTempHp = tempHp;
    }

    public int getMaxHp() {
        return mMaxHp;
    }

    public void setMaxHp(int maxHp) {
        mMaxHp = maxHp;
    }

    public Dice getHitDice() {
        return mHitDice;
    }

    public void setHitDice(Dice hitDice) {
        mHitDice = hitDice;
    }

    public int getSuccessAttempts() {
        return mSuccessAttempts;
    }

    public void setSuccessAttempts(int successAttempts) {
        mSuccessAttempts = successAttempts;
    }

    public int getFailAttempts() {
        return mFailAttempts;
    }

    public void setFailAttempts(int failAttempts) {
        mFailAttempts = failAttempts;
    }
}
