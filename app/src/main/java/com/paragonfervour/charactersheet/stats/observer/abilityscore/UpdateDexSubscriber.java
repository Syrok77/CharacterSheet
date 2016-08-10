package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;


public class UpdateDexSubscriber
        extends Subscriber<GameCharacter> {

    private int mDex;

    public UpdateDexSubscriber(int dex) {
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
        unsubscribe();
    }
}
