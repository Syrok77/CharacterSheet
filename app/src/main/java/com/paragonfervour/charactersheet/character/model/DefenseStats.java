package com.paragonfervour.charactersheet.character.model;


import com.orm.SugarRecord;

@SuppressWarnings("WeakerAccess")
public class DefenseStats extends SugarRecord {

    int mAC;

    // ability scores
    int mStrScore;
    int mDexScore;
    int mConScore;
    int mIntScore;
    int mWisScore;
    int mChaScore;

    // HP
    int mHitPoints;
    int mTempHp;
    int mMaxHp;
    int mHitDiceValue;

    // death saves
    int mSuccessAttempts;
    int mFailAttempts;

    /**
     * Create a new, default set of DefenseStats.
     *
     * @return new default defense stat model.
     */
    public static DefenseStats createDefault() {
        DefenseStats d = new DefenseStats();
        d.mAC = 14;
        d.mStrScore = 10;
        d.mDexScore = 10;
        d.mConScore = 10;
        d.mIntScore = 10;
        d.mWisScore = 10;
        d.mChaScore = 10;

        d.mHitPoints = 14;
        d.mTempHp = 0;
        d.mMaxHp = 14;
        d.setHitDice(Dice.D6);

        d.mSuccessAttempts = 0;
        d.mFailAttempts = 0;

        return d;
    }

    /**
     * Create default model for Maldalair test rogue
     *
     * @return default rogue stats
     */
    public static DefenseStats createMaldalair() {
        DefenseStats d = new DefenseStats();
        d.mAC = 16;
        d.mStrScore = 11;
        d.mDexScore = 16;
        d.mConScore = 13;
        d.mIntScore = 11;
        d.mWisScore = 12;
        d.mChaScore = 8;

        d.mHitPoints = 22;
        d.mTempHp = 0;
        d.mMaxHp = 24;
        d.setHitDice(Dice.D8);

        d.mSuccessAttempts = 0;
        d.mFailAttempts = 0;

        return d;
    }

    public DefenseStats() {
    }

    public int getAC() {
        return mAC;
    }

    public void setAC(int AC) {
        mAC = AC;
    }

    public int getStrScore() {
        return mStrScore;
    }

    public void setStrScore(int strScore) {
        mStrScore = strScore;
    }

    public int getDexScore() {
        return mDexScore;
    }

    public void setDexScore(int dexScore) {
        mDexScore = dexScore;
    }

    public int getConScore() {
        return mConScore;
    }

    public void setConScore(int conScore) {
        mConScore = conScore;
    }

    public int getIntScore() {
        return mIntScore;
    }

    public void setIntScore(int intScore) {
        mIntScore = intScore;
    }

    public int getWisScore() {
        return mWisScore;
    }

    public void setWisScore(int wisScore) {
        mWisScore = wisScore;
    }

    public int getChaScore() {
        return mChaScore;
    }

    public void setChaScore(int chaScore) {
        mChaScore = chaScore;
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
        return Dice.diceFromValue(mHitDiceValue);
    }

    public void setHitDice(Dice hitDice) {
        mHitDiceValue = hitDice.getValue();
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
