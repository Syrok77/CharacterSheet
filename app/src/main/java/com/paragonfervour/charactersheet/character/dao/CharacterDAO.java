package com.paragonfervour.charactersheet.character.dao;

import com.google.inject.Singleton;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Provider of Characters to the application. Singleton that provides clients with Observables that
 * emit the character(s) requested.
 */
@Singleton
public class CharacterDAO {

    private GameCharacter mActiveCharacter = GameCharacter.createDefaultCharacter();

    private BehaviorSubject<GameCharacter> mActiveCharacterSubject = BehaviorSubject.create();

    public CharacterDAO() {
        mActiveCharacterSubject.onNext(mActiveCharacter);
    }

    /**
     * Get the active character through an Observable. The Observable operates on a background thread
     * but observes on the main thread, so all observer callbacks here will come back on the main
     * Android thread.
     *
     * @return Observable that emits the active GameCharacter on the main thread.
     */
    public Observable<GameCharacter> getActiveCharacter() {
        return mActiveCharacterSubject.asObservable();
    }

    /**
     * Alerts this DAO that it needs to publish the active character again. Use this to signal other
     * that the game character was changed, or when you switch active characters.
     */
    public void activeCharacterUpdated() {
        mActiveCharacterSubject.onNext(mActiveCharacter);
    }
}
