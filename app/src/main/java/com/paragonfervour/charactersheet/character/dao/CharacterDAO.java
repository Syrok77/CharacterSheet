package com.paragonfervour.charactersheet.character.dao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.paragonfervour.charactersheet.character.model.GameCharacter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private Context mContext;

    private static final String ACTIVE_CHARACTER_FILE_NAME = "active_character.json";
    private static final String TAG = CharacterDAO.class.getSimpleName();

    private GameCharacter mActiveCharacter = GameCharacter.createDefaultCharacter();
    private final BehaviorSubject<GameCharacter> mActiveCharacterSubject = BehaviorSubject.create();

    private Thread mSaveThread;
    // TODO: How does lock?
    private final Object mLock = new Object();

    @Inject
    public CharacterDAO(Context context) {
        mContext = context;
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
                        synchronized (mLock) {
                            if (mSaveThread == null) {
                                mSaveThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        saveActiveCharacter();
                                        mSaveThread = null;
                                    }
                                });
                                mSaveThread.start();
                            }
                        }
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
        try {
            FileInputStream fileStream = mContext.openFileInput(ACTIVE_CHARACTER_FILE_NAME);
            BufferedInputStream stream = new BufferedInputStream(fileStream);

            byte[] file = new byte[stream.available()];
            int read = stream.read(file, 0, stream.available());

            stream.close();

            Gson gson = new Gson();
            GameCharacter character = gson.fromJson(new String(file), GameCharacter.class);

            Log.d(TAG, "Read in character " + character.getInfo().getName() + " (" + read + ") bytes");
            mActiveCharacter = character;
            mActiveCharacterSubject.onNext(character);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                Log.d(TAG, "Character did not exist.");
                mActiveCharacter = GameCharacter.createDefaultCharacter();
                activeCharacterUpdated();
                saveActiveCharacter();
            } else {
                e.printStackTrace();
            }
        }
    }

    private void saveActiveCharacter() {
        if (mActiveCharacter == null) {
            return;
        }

        try {
            FileOutputStream outputStream = mContext.openFileOutput(ACTIVE_CHARACTER_FILE_NAME, Context.MODE_PRIVATE);
            BufferedOutputStream stream = new BufferedOutputStream(outputStream);

            Gson gson = new Gson();
            String json = gson.toJson(mActiveCharacter);
            stream.write(json.getBytes());

            Log.d(TAG, "Saved active character " + mActiveCharacter.getInfo().getName());

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
