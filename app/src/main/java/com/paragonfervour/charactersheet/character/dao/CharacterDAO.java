package com.paragonfervour.charactersheet.character.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Provider of Characters to the application. Singleton that provides clients with Observables that
 * emit the character(s) requested.
 */
@Singleton
public class CharacterDAO {

    private static final String TAG = CharacterDAO.class.getSimpleName();

    private GameCharacter mActiveCharacter = GameCharacter.createDefaultCharacter();
    private final BehaviorSubject<GameCharacter> mActiveCharacterSubject = BehaviorSubject.create();

    @Inject
    public CharacterDAO() {
        loadActiveCharacter();
    }

    /**
     * Get the active character through an Observable. The Observable operates on a background thread
     * but observes on the main thread, so all observer callbacks here will come back on the main
     * Android thread.
     *
     * @return Observable that emits the active GameCharacter on the main thread.
     */
    public Observable<GameCharacter> getActiveCharacter() {
        return mActiveCharacterSubject.asObservable()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mActiveCharacter == null) {
                            loadActiveCharacter();
                        }
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        // Batch changes together. Changes will only be saved every 3 seconds.
                        mActiveCharacter.save();
                    }
                })
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Alerts this DAO that it needs to publish the active character again. Use this to signal other
     * that the game character was changed, or when you switch active characters.
     */
    public void activeCharacterUpdated() {
        mActiveCharacterSubject.onNext(mActiveCharacter);
    }

    private void loadActiveCharacter() {
        mActiveCharacter = GameCharacter.findById(GameCharacter.class, 1l);

        if (mActiveCharacter == null) {
            // Create a new one
            mActiveCharacter = GameCharacter.createDefaultCharacter();
            mActiveCharacter.save();
        }

        mActiveCharacterSubject.onNext(mActiveCharacter);
    }
}
