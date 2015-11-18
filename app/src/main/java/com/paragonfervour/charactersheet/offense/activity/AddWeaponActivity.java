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
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.Damage;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Weapon;
import com.paragonfervour.charactersheet.component.DicePickerViewComponent;

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

    @InjectView(R.id.add_weapon_weight)
    private EditText mWeightText;

    @InjectView(R.id.add_weapon_value)
    private EditText mValueText;

    @InjectView(R.id.add_weapon_dice_multiplier)
    private EditText mDamageDiceMultiplier;

    @InjectView(R.id.add_weapon_modifier)
    private EditText mDamageModifier;

    @InjectView(R.id.add_weapon_summary_text)
    private TextView mDamageSummary;

    @InjectView(R.id.add_weapon_dice_picker)
    private DicePickerViewComponent mDamageDiceComponent;

    private static final String TAG = AddWeaponActivity.class.getSimpleName();
    public static final String EXTRA_WEAPON_ID = "extra_weapon_id";

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
        Long weaponId = i.getLongExtra(EXTRA_WEAPON_ID, 0L);
        mWeapon = mCharacterDAO.getWeaponById(weaponId);

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
        updateWeaponView(mWeapon);

        mSaveButton.setOnClickListener(new SaveButtonClickListener());

        if (isEditing) {
            // Set CharacterDAO update text watchers.
            mWeaponName.addTextChangedListener(new NameTextWatcher());
            mWeightText.addTextChangedListener(new WeightTextWatcher());
            mValueText.addTextChangedListener(new ValueTextWatcher());
        }
        // These watchers are used whether we are editing or not.
        mDamageDiceMultiplier.addTextChangedListener(new DamageDiceMultiplierTextWatcher());
        mDamageModifier.addTextChangedListener(new DamageModifierTextWatcher());

        mDamageDiceComponent.getDiceObservable().subscribe(new Observer<Dice>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Dice dice) {
                updateDamageSummary();
                if (isEditing) {
                    updateCharacterWeaponDice(dice);
                }
            }
        });
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

    /**
     * Get a TextView's input as an integer.
     *
     * @param view TextView input.
     * @return int form of the TextView's input, which should represent an integer.
     */
    private int getIntFromTextView(TextView view) {
        return getIntFromString(view.getText().toString());
    }

    /**
     * Get a String's value as an integer. Essentially Integer.valueOf(), but will return 0 for the empty string.
     *
     * @param s String to turn into an integer.
     * @return integer that the String represents. i.e. "5" would return 5.
     */
    private int getIntFromString(String s) {
        if (s != null && !s.isEmpty()) {
            return Integer.valueOf(s);
        }
        return 0;
    }

    private void updateWeaponView(Weapon weapon) {
        mWeaponName.setText(weapon.getName());
        if (weapon.isMainHand()) {
            mHandGroup.check(R.id.add_weapon_hand_main_button);
        } else {
            mHandGroup.check(R.id.add_weapon_hand_off_button);
        }
        mWeightText.setText(String.valueOf(weapon.getWeight()));
        mValueText.setText(String.valueOf(weapon.getValue()));
        mDamageDiceMultiplier.setText(String.valueOf(weapon.getDamage().getDiceQuantity()));
        mDamageModifier.setText(String.valueOf(weapon.getDamage().getModifier()));
        mDamageSummary.setText(weapon.getDamage().toString());
        mDamageDiceComponent.setDice(weapon.getDamage().getDiceType());
    }

    /**
     * Update the Damage Summary view to reflect the current Damage input values.
     */
    private void updateDamageSummary() {
        Log.d(TAG, "Updating damage summary text.");
        mDamageSummary.setText(createDamage().toString());
    }

    /**
     * Update active character's weapon dice to the given value.
     *
     * @param dice Dice the weapon is now using.
     */
    private void updateCharacterWeaponDice(final Dice dice) {
        if (mWeapon != null) {
            mWeapon.getDamage().setDiceType(dice);
            mWeapon.save();
        }
    }

    /**
     * Create a Damage model corresponding to the input views.
     *
     * @return a Damage model from the user input.
     */
    private Damage createDamage() {
        Damage damage = new Damage();
        damage.setDiceType(mDamageDiceComponent.getDice());
        damage.setDiceQuantity(getIntFromTextView(mDamageDiceMultiplier));
        damage.setModifier(getIntFromTextView(mDamageModifier));
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
        weapon.setValue(getIntFromTextView(mValueText));
        weapon.setWeight(getIntFromTextView(mWeightText));
        weapon.setIsMainHand(mHandGroup.getCheckedRadioButtonId() == R.id.add_weapon_hand_main_button);
        // TODO:
        weapon.setProperties("");
        return weapon;
    }

    /**
     * Set this weapon to the main hand. If we are editing, make the update into the model immediately,
     * otherwise just set the value on the Weapon.
     */
    private void setMainHand() {
        if (isEditing && mWeapon != null) {
            mWeapon.setIsMainHand(true);
            mWeapon.save();
        }
    }

    /**
     * Set this weapon to the off hand. If we are editing, make the update into the model immediately,
     * otherwise just set the value on the Weapon.
     */
    private void setOffHand() {
        if (isEditing && mWeapon != null) {
            mWeapon.setIsMainHand(false);
            mWeapon.save();
        }
    }

    /**
     * A Hand radio button was clicked, which is probably the user switching the weapon's hand.
     *
     * @param view RadioButton view that was clicked.
     */
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
    }

    private class SaveButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCharacterDAO.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(GameCharacter gameCharacter) {
                    Weapon weapon = createWeapon();
                    weapon.setOffenseStatId(gameCharacter.getOffenseStats().getId());
                    weapon.save();

                    mCharacterDAO.activeCharacterUpdated();
                    unsubscribe();
                }
            });
            finish();
        }
    }

    /**
     * Update the weapon's weight value in the GameCharacter.
     */
    private class WeightTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.setWeight(getIntFromString(value));
        }
    }

    /**
     * Update the weapon's value in the GameCharacter.
     */
    private class DamageDiceMultiplierTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.getDamage().setDiceQuantity(getIntFromString(value));
        }
    }

    /**
     * Update the weapon's value in the GameCharacter. If we are creating a new Weapon, this class
     * ignores the GameCharacter update and just updates the Damage Summary.
     */
    private class DamageModifierTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.getDamage().setModifier(getIntFromString(value));
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isEditing) {
                super.afterTextChanged(s);
            }
            updateDamageSummary();
        }
    }

    /**
     * Update the weapon's value in the GameCharacter. If we are creating a new Weapon, this class
     * ignores the GameCharacter update and just updates the Damage Summary.
     */
    private class ValueTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.setValue(getIntFromString(value));
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
            updateValue(mWeapon, s.toString());
            mWeapon.save();
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
