package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;


public class UpdateConObserver implements Observer<GameCharacter> {

    private int mCon;

    public UpdateConObserver(int con) {
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
    }
}