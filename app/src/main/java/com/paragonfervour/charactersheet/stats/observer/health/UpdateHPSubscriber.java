package com.paragonfervour.charactersheet.stats.observer.health;


import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;

/**
 * Observer that updates the HP of the game character to the given value when the game character is loaded.
 * This is a subscriber that automatically unsubscribes itself upon receiving a signal. Thus, this
 * is a one-time-use subscriber per instance.
 */
public class UpdateHPSubscriber extends Subscriber<GameCharacter> {

    private int mHp;

    public UpdateHPSubscriber(int hp) {
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
        unsubscribe();
    }
}