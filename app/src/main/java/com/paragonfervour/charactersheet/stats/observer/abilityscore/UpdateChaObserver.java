package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;

public class UpdateChaObserver implements Observer<GameCharacter> {

    private int mCha;

    public UpdateChaObserver(int cha) {
        mCha = cha;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setChaScore(mCha);
    }
}
