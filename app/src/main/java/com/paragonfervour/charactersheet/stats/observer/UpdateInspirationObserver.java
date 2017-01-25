package com.paragonfervour.charactersheet.stats.observer;

import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observer;

public class UpdateInspirationObserver implements Observer<GameCharacter> {

    private boolean mIsInspired;

    public UpdateInspirationObserver(boolean isInspired) {
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
    }
}
