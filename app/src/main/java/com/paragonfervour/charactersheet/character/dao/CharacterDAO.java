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

    /**
     * Get the active character through an Observable. The Observable operates on a background thread
     * but observes on the main thread, so all observer callbacks here will come back on the main
     * Android thread.
     *
     * @return Observable that emits the active GameCharacter on the main thread.
     */
    public Observable<GameCharacter> getActiveCharacter() {
        return Observable.just(mActiveCharacter);
    }
}
