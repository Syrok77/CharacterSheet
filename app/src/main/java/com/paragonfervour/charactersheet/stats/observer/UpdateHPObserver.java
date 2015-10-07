package com.paragonfervour.charactersheet.stats.observer;


import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;

/**
 * Observer that updates the HP of the game character to the given value when the game character is loaded.
 */
public class UpdateHPObserver implements Observer<GameCharacter> {

    private int mHp;

    public UpdateHPObserver(int hp) {
        mHp = hp;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setHitPoints(mHp);
    }
}