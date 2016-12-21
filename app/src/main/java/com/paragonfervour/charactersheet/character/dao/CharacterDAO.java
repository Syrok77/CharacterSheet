package com.paragonfervour.charactersheet.character.dao;

import android.support.annotation.Nullable;
import android.util.Log;

import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Skill;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.character.model.Weapon;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Provider of Characters to the application. Singleton that provides clients with Observables that
 * emit the character(s) requested. This class also provides access to the Skills and Weapons of a
 * GameCharacter.
 */
@Singleton
public class CharacterDao {

    private static final String TAG = CharacterDao.class.getSimpleName();

    private GameCharacter mActiveCharacter;
    private final BehaviorSubject<GameCharacter> mActiveCharacterSubject = BehaviorSubject.create();

    @Inject
    public CharacterDao() {
    }

    /**
     * Get the active character through an Observable. The Observable operates on a background thread
     * but observes on the main thread, so all observer callbacks here will come back on the main
     * Android thread.
     *
     * @return Observable that emits the active GameCharacter on the main thread.
     */
    public Observable<GameCharacter> getActiveCharacter() {
        if (mActiveCharacter == null) {
            loadActiveCharacter();
        }

        return mActiveCharacterSubject.asObservable()
                .doOnUnsubscribe(mActiveCharacter::save)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Get the {@link Spell} with the given Id.
     *
     * @param id spell's id.
     * @return Spell matching the given id, or null if there is no Spell with that id.
     */
    @Nullable
    public Spell getSpell(Long id) {
        if (id != null) {
            return Spell.findById(Spell.class, id);
        }
        return null;
    }

    /**
     * Get a weapon by the model's ID.
     *
     * @param id Weapon model's ID.
     * @return Weapon matching the ID, or null if it doesn't exist.
     */
    @Nullable
    public Weapon getWeaponById(Long id) {
        if (id != null) {
            return Weapon.findById(Weapon.class, id);
        }
        return null;
    }

    /**
     * Get the Skill with the given name.
     *
     * @param name          skill's name to identify the skill.
     * @param gameCharacter GameCharacter who knows the skill.
     * @return Skill with the given name, or null if it doesn't exist.
     */
    public Skill getSkillByName(String name, GameCharacter gameCharacter) {
        if (name != null) {
            String query = Skill.getNameFieldSqlValue() + " = ? and " + Skill.getCharacterIdFieldSqlValue() + " = ?";
            List<Skill> skillsByName = Skill.find(Skill.class, query, name.toLowerCase(), String.valueOf(gameCharacter.getId()));
            if (skillsByName != null && skillsByName.size() > 0) {
                if (skillsByName.size() > 1) {
                    Log.w(TAG, "Multiple skills found named " + name);
                }
                return skillsByName.get(0);
            }
        }
        return null;
    }

    /**
     * Save a given skill to the given GameCharacter.
     *
     * @param skill         skill to save.
     * @param gameCharacter GameCharacter who knows the skill.
     * @return true if a new skill was created, false if the skill already existed.
     */
    public boolean saveSkill(Skill skill, GameCharacter gameCharacter) {
        Skill existing = getSkillByName(skill.getName(), gameCharacter);
        if (existing != null) {
            skill.setId(existing.getId());
            skill.save();
            return false;
        } else {
            skill.setCharacterId(gameCharacter.getId());
            skill.save();
            return true;
        }
    }

    /**
     * Alerts this DAO that it needs to publish the active character again. Use this to signal other
     * that the game character was changed, or when you switch active characters.
     */
    public void activeCharacterUpdated() {
        mActiveCharacterSubject.onNext(mActiveCharacter);
    }

    private void loadActiveCharacter() {
        mActiveCharacter = GameCharacter.findById(GameCharacter.class, 1L);

        if (mActiveCharacter == null) {
            // Create a new one
            mActiveCharacter = GameCharacter.createDefaultCharacter();
            long characterId = mActiveCharacter.save();

            // Add default weapons
            Weapon main = Weapon.createDefault();
            main.setCharacterId(characterId);
            main.save();

            Weapon off = Weapon.createOffhand();
            off.setCharacterId(characterId);
            off.save();

            Weapon bow = Weapon.createShortbow();
            bow.setCharacterId(characterId);
            bow.save();

            // Create default skills
            List<Skill> skills = Skill.createMaldalairList();
            for (Skill s : skills) {
                s.setCharacterId(characterId);
                s.save();
            }

            // Create default spells
            List<Spell> spells = Spell.createMaldalairList();
            for (Spell spell : spells) {
                spell.setCharacterId(characterId);
                spell.save();
            }
        }

        mActiveCharacterSubject.onNext(mActiveCharacter);
    }
}
