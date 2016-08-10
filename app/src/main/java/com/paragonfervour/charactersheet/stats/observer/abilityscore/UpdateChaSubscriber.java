package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;


public class UpdateChaSubscriber extends Subscriber<GameCharacter> {

    private int mCha;

    public UpdateChaSubscriber(int cha) {
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
        unsubscribe();
    }
}
