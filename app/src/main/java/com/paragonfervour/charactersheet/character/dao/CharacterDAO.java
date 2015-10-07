package com.paragonfervour.charactersheet.character.dao;

import com.google.inject.Singleton;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observable;

/**
 * Provider of Characters to the application. Singleton that provides clients with Observables that
 * emit the character(s) requested.
 */
@Singleton
public class CharacterDAO {

    private GameCharacter mActiveCharacter = GameCharacter.createDefaultCharacter();

    public CharacterDAO() {
    }

    public Observable<GameCharacter> getActiveCharacter() {
        return Observable.just(mActiveCharacter);
    }
}
