package com.paragonfervour.charactersheet.stats.observer;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Subscriber;

public class UpdateInspirationSubscriber extends Subscriber<GameCharacter> {

    private boolean mIsInspired;

    public UpdateInspirationSubscriber(boolean isInspired) {
        mIsInspired = isInspired;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GameCharacter gameCharacter) {
        gameCharacter.setIsInspired(mIsInspired);
        unsubscribe();
    }
}
