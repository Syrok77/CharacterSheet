package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;


public class UpdateIntObserver implements Observer<GameCharacter> {

    private int mInt;

    public UpdateIntObserver(int intelligence) {
        mInt = intelligence;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setIntScore(mInt);
    }
}