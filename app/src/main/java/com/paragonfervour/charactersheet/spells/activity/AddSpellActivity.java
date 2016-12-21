package com.paragonfervour.charactersheet.spells.activity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

    @BindView(R.id.add_spell_save_button)
    View mSaveButton;

    public static final String KEY_SPELL_ID = "key_spell_id";
    private static final String TAG = AddSpellActivity.class.getSimpleName();

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private long mCharacterId;
    private boolean mIsEditingSpell;
    private long mSpellId;

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

        // check for spell
        Long spellId = getIntent().getLongExtra(KEY_SPELL_ID, 0);
        Spell spell = mCharacterDao.getSpell(spellId);
        if (spell != null) {
            mIsEditingSpell = true;
            mSpellId = spellId;

            // populate fields
            bindViews(spell);

            // hide save button, changes are applied immediately
            mSaveButton.setVisibility(View.GONE);
        }

        mCompositeSubscription.add(mCharacterDao.getActiveCharacter()
                .subscribe(this::onActiveCharacter, this::onActiveCharacterError));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return mIsEditingSpell;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete && mIsEditingSpell) {
            Spell spell = mCharacterDao.getSpell(mSpellId);
            if (spell != null) {
                spell.delete();
            } else {
                Log.e(TAG, "Attempted to delete spell, but it could not be found.");
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // do this in onPause so that it saves immediately. Doing so later can cause View
        // updates in other Activities caused by this change to be visible to the user.
        if (mIsEditingSpell) {
            Spell spell = createSpell();
            spell.setId(mSpellId);
            spell.save();
            mCharacterDao.activeCharacterUpdated();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.clear();
    }

    /**
     * Bind a given spell to the UI.
     *
     * @param spell spell whose values will be shown in the UI as input.
     */
    private void bindViews(Spell spell) {
        mName.setText(spell.getName());
        mLevel.setText(String.valueOf(spell.getLevel()));
        mDescription.setText(spell.getDescription());
        mCastingTime.setText(spell.getCastingTime());
        mComponents.setText(spell.getComponents());
        mDuration.setText(spell.getDuration());
        mRange.setText(spell.getDuration());
    }

    /**
     * Create the {@link Spell} from the View input.
     */
    private Spell createSpell() {
        String name = mName.getText().toString();
        int level = Integer.valueOf(mLevel.getText().toString());
        String description = mDescription.getText().toString();
        String castingTime = mCastingTime.getText().toString();
        String components = mComponents.getText().toString();
        String duration = mDuration.getText().toString();
        String range = mRange.getText().toString();
        return new Spell(name,
                level,
                description,
                castingTime,
                components,
                duration,
                range,
                mCharacterId);
    }

    @OnClick(R.id.add_spell_save_button)
    void onSaveClick() {
        Spell spell = createSpell();
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
