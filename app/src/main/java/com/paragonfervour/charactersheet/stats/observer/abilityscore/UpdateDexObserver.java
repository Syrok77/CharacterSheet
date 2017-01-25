package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;


public class UpdateDexObserver implements Observer<GameCharacter> {

    private int mDex;

    public UpdateDexObserver(int dex) {
        mDex = dex;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setDexScore(mDex);
    }
}
