package com.paragonfervour.charactersheet.spells.activity;


import android.os.Bundle;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.injection.Injectors;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Activity allowing the user to create or edit spells. For creating a new spell, simply start this
 * activity. For editing spells, add {@link #KEY_SPELL_ID} to the Intent's Extras with the editing
 * {@link Spell}'s ID.
 */
public class AddSpellActivity extends ComponentBaseActivity {

    @Inject
    CharacterDao mCharacterDao;

    public static final String KEY_SPELL_ID = "key_spell_id";
    private static final String TAG = AddSpellActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spell_activity);
        ButterKnife.bind(this);
        Injectors.activityComponent(this).inject(this);
    }
}
