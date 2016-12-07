package com.paragonfervour.charactersheet.spells.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.injection.Injectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity allowing the user to create or edit spells. For creating a new spell, simply start this
 * activity. For editing spells, add {@link #KEY_SPELL_ID} to the Intent's Extras with the editing
 * {@link Spell}'s ID.
 */
public class AddSpellActivity extends ComponentBaseActivity {

    @Inject
    CharacterDao mCharacterDao;

    @BindView(R.id.activity_add_spell_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.add_spell_name)
    TextView mName;

    @BindView(R.id.add_spell_level)
    TextView mLevel;

    @BindView(R.id.add_spell_casting_time)
    TextView mCastingTime;

    @BindView(R.id.add_spell_components)
    TextView mComponents;

    @BindView(R.id.add_spell_duration)
    TextView mDuration;

    @BindView(R.id.add_spell_range)
    TextView mRange;

    @BindView(R.id.add_spell_description)
    TextView mDescription;

    public static final String KEY_SPELL_ID = "key_spell_id";
    private static final String TAG = AddSpellActivity.class.getSimpleName();

    private long mCharacterId;
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spell_activity);
        ButterKnife.bind(this);
        Injectors.activityComponent(this).inject(this);

        // setup toolbar
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle(getTitle());

        mCompositeSubscription.add(mCharacterDao.getActiveCharacter()
                .subscribe(this::onActiveCharacter, this::onActiveCharacterError));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.clear();
    }

    @OnClick(R.id.add_spell_save_button)
    void onSaveClick() {
        String name = mName.getText().toString();
        int level = Integer.valueOf(mLevel.getText().toString());
        String description = mDescription.getText().toString();
        String castingTime = mCastingTime.getText().toString();
        String components = mComponents.getText().toString();
        String duration = mDuration.getText().toString();
        String range = mRange.getText().toString();
        Spell spell = new Spell(name,
                level,
                description,
                castingTime,
                components,
                duration,
                range,
                mCharacterId);

        spell.save();
        mCharacterDao.activeCharacterUpdated();
        finish();
    }

    private void onActiveCharacter(GameCharacter activeCharacter) {
        mCharacterId = activeCharacter.getId();
    }

    private void onActiveCharacterError(Throwable error) {
        Log.e(TAG, "Error getting character", error);
    }
}
