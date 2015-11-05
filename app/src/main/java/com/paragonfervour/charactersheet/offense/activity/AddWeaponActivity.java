package com.paragonfervour.charactersheet.offense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Weapon;

import roboguice.inject.InjectView;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity that allows the user to create or edit a Weapon model, and posts a CharacterDAO change
 * event if a change was made.
 * TODO: Weight, value, name, hand type
 */
public class AddWeaponActivity extends ComponentBaseActivity {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.activity_add_weapon_toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.add_weapon_save_button)
    private Button mSaveButton;

    public static final String EXTRA_WEAPON_MODEL = "extra_weapon_model";

    // temp
    public static final String EXTRA_IS_MAIN_HAND = "extra_is_main_hand";
    private boolean isMainHand = true;

    private CompositeSubscription mCompositeSubscription;
    private Weapon mWeapon;
    private boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weapon);

        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(mCharacterDAO.getActiveCharacter()
                .subscribe(new CharacterObserver()));

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        Intent i = getIntent();
        mWeapon = (Weapon) i.getSerializableExtra(EXTRA_WEAPON_MODEL);
        isMainHand = i.getBooleanExtra(EXTRA_IS_MAIN_HAND, true);

        if (mWeapon == null) {
            // We are creating a new weapon
            mToolbar.setTitle(R.string.nav_add_weapon_title);
            mSaveButton.setVisibility(View.VISIBLE);

            mWeapon = Weapon.createDefault();
        } else {
            // We are editing an existing weapon.
            mToolbar.setTitle(R.string.nav_edit_weapon_title);
            mSaveButton.setVisibility(View.GONE);
            isEditing = true;
        }

         mSaveButton.setOnClickListener(new SaveButtonClickListener());

        updateWeaponView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isEditing) {
            mCharacterDAO.activeCharacterUpdated();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    private void updateWeaponView() {

    }

    private class SaveButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO: Save this weapon into the character.
            finish();
        }
    }

    /**
     * Observer for character events. Right now we don't listen to this, but unsubscribing is a key
     * component to updating the model.
     */
    private class CharacterObserver implements Observer<GameCharacter> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(GameCharacter gameCharacter) {

        }
    }
}
