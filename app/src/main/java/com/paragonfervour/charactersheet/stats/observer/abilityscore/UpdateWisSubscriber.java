package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;


public class UpdateWisSubscriber extends Subscriber<GameCharacter> {

    private int mWis;

    public UpdateWisSubscriber(int wis) {
        mWis = wis;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setWisScore(mWis);
        unsubscribe();
    }
}