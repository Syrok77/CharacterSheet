package com.paragonfervour.charactersheet.stats.observer.abilityscore;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;


public class UpdateStrSubscriber extends Subscriber<GameCharacter> {

    private int mStr;

    public UpdateStrSubscriber(int str) {
        mStr = str;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.getDefenseStats().setStrScore(mStr);
        unsubscribe();
    }
}
