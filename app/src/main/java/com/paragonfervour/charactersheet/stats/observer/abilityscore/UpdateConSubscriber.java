package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;


public class UpdateConSubscriber extends Subscriber<GameCharacter> {

    private int mCon;

    public UpdateConSubscriber(int con) {
        mCon = con;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setConScore(mCon);
        unsubscribe();
    }
}