package com.paragonfervour.charactersheet.offense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.Damage;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Weapon;

import roboguice.inject.InjectView;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity that allows the user to create or edit a Weapon model, and posts a CharacterDAO change
 * event if a change was made.
 */
public class AddWeaponActivity extends ComponentBaseActivity {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.activity_add_weapon_toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.add_weapon_save_button)
    private Button mSaveButton;

    @InjectView(R.id.weapon_name)
    private EditText mWeaponName;

    @InjectView(R.id.add_weapon_hand_group)
    private RadioGroup mHandGroup;

    private static final String TAG = AddWeaponActivity.class.getSimpleName();
    public static final String EXTRA_WEAPON_MODEL = "extra_weapon_model";

    // temp
    public static final String EXTRA_IS_MAIN_HAND = "extra_is_main_hand";
    private boolean isMainHand = true;

    private CompositeSubscription mCompositeSubscription;
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
        Weapon weapon = (Weapon) i.getSerializableExtra(EXTRA_WEAPON_MODEL);
        isMainHand = i.getBooleanExtra(EXTRA_IS_MAIN_HAND, true);

        if (weapon == null) {
            // We are creating a new weapon
            mToolbar.setTitle(R.string.nav_add_weapon_title);
            mSaveButton.setVisibility(View.VISIBLE);

            weapon = Weapon.createDefault();
        } else {
            // We are editing an existing weapon.
            mToolbar.setTitle(R.string.nav_edit_weapon_title);
            mSaveButton.setVisibility(View.GONE);
            isEditing = true;
        }

        mSaveButton.setOnClickListener(new SaveButtonClickListener());
        mWeaponName.addTextChangedListener(new NameTextWatcher());

        updateWeaponView(weapon);
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

    private void updateWeaponView(Weapon weapon) {
        mWeaponName.setText(weapon.getName());
        if (isMainHand) {
            mHandGroup.check(R.id.add_weapon_hand_main_button);
        } else {
            mHandGroup.check(R.id.add_weapon_hand_off_button);
        }
    }

    /**
     * Get the active weapon model directly from the GameCharacter.
     *
     * @param gameCharacter game character model being edited.
     * @return the weapon inner-model from the passed in gameCharacter.
     */
    private Weapon getActiveWeapon(GameCharacter gameCharacter) {
        // TODO: Support multiple weapons in each hand
        if (isMainHand) {
            return gameCharacter.getOffenseStats().getMainHand();
        } else {
            return gameCharacter.getOffenseStats().getOffHand();
        }
    }

    private Damage createDamage() {
        Damage damage = new Damage();
        // TODO:
        damage.setDiceQuantity(2);
        damage.setDiceType(Dice.D8);
        damage.setModifier(4);
        return damage;
    }

    /**
     * Create the Weapon model that this AddWeaponActivity is creating/editing.
     *
     * @return updated Weapon model from this page.
     */
    private Weapon createWeapon() {
        Weapon weapon = new Weapon();
        weapon.setName(mWeaponName.getText().toString());
        weapon.setDamage(createDamage());
        // TODO:
        weapon.setValue(10);
        // TODO:
        weapon.setWeight(2);
        // TODO:
        weapon.setProperties("");
        return weapon;
    }

    /**
     * Set this weapon to the main hand. If we are editing, make the update into the model immediately,
     * otherwise just set the value on the Weapon.
     */
    private void setMainHand() {
        isMainHand = true;
        if (isEditing) {
            // TODO: Update the character model
            // Remove mWeapon from the offhand list

            // Add mWeapon to the offhand list.
        }
    }

    /**
     * Set this weapon to the off hand. If we are editing, make the update into the model immediately,
     * otherwise just set the value on the Weapon.
     */
    private void setOffHand() {
        isMainHand = false;
        if (isEditing) {
            // TODO: Update the character model.
            // Remove mWeapon from the main hand list

            // Add mWeapon to the offhand list.
        }
    }

    public void onHandOptionClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.add_weapon_hand_main_button:
                if (checked) {
                    setMainHand();
                    break;
                }
            case R.id.add_weapon_hand_off_button:
                if (checked) {
                    setOffHand();
                    break;
                }
        }

        if (isEditing) {
            Toast.makeText(this, "Switching hands is currently not supported.", Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCharacterDAO.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(GameCharacter gameCharacter) {
                    Log.d(TAG, "Updated weapon " + isMainHand);
                    if (isMainHand) {
                        gameCharacter.getOffenseStats().setMainHand(createWeapon());
                    } else {
                        gameCharacter.getOffenseStats().setOffHand(createWeapon());
                    }

                    mCharacterDAO.activeCharacterUpdated();
                    unsubscribe();
                }
            });
            finish();
        }
    }

    /**
     * Update the weapon's name value in the GameCharacter.
     */
    private class NameTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.setName(value);
        }
    }

    /**
     * Abstract base TextWatcher class for the EditText classes to use. Handles subscribing and
     * unsubscribing from the GameCharacter model, allowing subclasses to simply implement updateValue()
     * to get their work done.
     */
    private abstract class UpdateTextWatcher implements TextWatcher {

        public abstract void updateValue(Weapon weapon, String value);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            mCharacterDAO.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(GameCharacter gameCharacter) {
                    updateValue(getActiveWeapon(gameCharacter), s.toString());
                    unsubscribe();
                }
            });
        }
    }

    /**
     * Observer for character events. Right now we don't listen to this, but unsubscribing is a key
     * component to updating the model.
     * TODO: Evaluate if this is even necessary.
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
