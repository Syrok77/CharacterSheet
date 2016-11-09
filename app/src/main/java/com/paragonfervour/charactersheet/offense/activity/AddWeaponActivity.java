package com.paragonfervour.charactersheet.offense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.Damage;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Weapon;
import com.paragonfervour.charactersheet.component.DicePickerViewComponent;
import com.paragonfervour.charactersheet.injection.Injectors;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Activity that allows the user to create or edit a Weapon model, and posts a CharacterDAO change
 * event if a change was made.
 */
public class AddWeaponActivity extends ComponentBaseActivity {

    @Inject
    CharacterDAO mCharacterDAO;

    @BindView(R.id.activity_add_weapon_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.add_weapon_save_button)
    Button mSaveButton;

    @BindView(R.id.weapon_name)
    EditText mWeaponName;

    @BindView(R.id.add_weapon_hand_group)
    RadioGroup mHandGroup;

    @BindView(R.id.add_weapon_weight)
    EditText mWeightText;

    @BindView(R.id.add_weapon_value)
    EditText mValueText;

    @BindView(R.id.add_weapon_score_selector)
    Spinner mScoreSelector;

    @BindView(R.id.add_weapon_attack_mod)
    TextView mHitModifier;

    @BindView(R.id.add_weapon_dice_multiplier)
    EditText mDamageDiceMultiplier;

    @BindView(R.id.add_weapon_modifier)
    EditText mDamageModifier;

    @BindView(R.id.add_weapon_summary_text)
    TextView mDamageSummary;

    @BindView(R.id.add_weapon_dice_picker)
    DicePickerViewComponent mDamageDiceComponent;

    @BindView(R.id.add_weapon_properties_text)
    EditText mProperties;

    private static final String TAG = AddWeaponActivity.class.getSimpleName();
    private static final int STR_INDEX = 0;
    private static final int DEX_INDEX = 1;

    public static final String EXTRA_WEAPON_ID = "extra_weapon_id";

    private CompositeSubscription mCompositeSubscription;
    private Weapon mWeapon;
    private boolean isEditing;

    private int mDex;
    private int mStr;
    private int mProficiencyBonus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.activityComponent(this).inject(this);
        setContentView(R.layout.add_weapon_activity);
        ButterKnife.bind(this);

        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(mCharacterDAO.getActiveCharacter()
                .subscribe(new CharacterObserver()));

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
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
            mProperties.addTextChangedListener(new PropertiesTextWatcher());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_weapon, menu);
        return isEditing;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_weapon_menu_delete && isEditing) {
            mWeapon.delete();
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    /**
     * Initialize score selector with an adapter and a change listener.
     */
    private void initializeScoreSelector() {
        // Initialize the ability score modifier spinner
        List<String> scores = new ArrayList<>();
        String strMod = StatHelper.getStatIndicator(mStr) + mStr;
        String dexMod = StatHelper.getStatIndicator(mDex) + mDex;
        scores.add(getString(R.string.add_weapon_str_format, strMod));
        scores.add(getString(R.string.add_weapon_dex_format, dexMod));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, scores);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mScoreSelector.setAdapter(spinnerAdapter);
        mScoreSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mWeapon.setIsStrengthWeapon(position == STR_INDEX);
                if (isEditing) {
                    mWeapon.save();
                }
                updateDamageSummary();
                updateHitModifier();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
        mProperties.setText(weapon.getProperties());
    }

    /**
     * Update the Damage Summary view to reflect the current Damage input values.
     */
    private void updateDamageSummary() {
        Log.d(TAG, "Updating damage summary text.");
        mDamageSummary.setText(createDamage().toString());
    }

    private void updateHitModifier() {
        int mod;
        if (mWeapon.isStrengthWeapon()) {
            mScoreSelector.setSelection(STR_INDEX);
            mod = mStr;
        } else {
            mScoreSelector.setSelection(DEX_INDEX);
            mod = mDex;
        }

        String hitStr = StatHelper.getStatIndicator(mod) + String.valueOf(mod + mProficiencyBonus);
        mHitModifier.setText(hitStr);
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
        int modifier = getIntFromTextView(mDamageModifier);
        if (mWeapon.isStrengthWeapon()) {
            modifier += mStr;
        } else {
            modifier += mDex;
        }
        damage.setModifier(modifier);
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
        weapon.setIsStrengthWeapon(mWeapon.isStrengthWeapon());
        weapon.setProperties(mProperties.getText().toString());
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
                    weapon.setCharacterId(gameCharacter.getId());
                    weapon.save();

                    mCharacterDAO.activeCharacterUpdated();
                    unsubscribe();
                }
            });
            finish();
        }
    }

    /**
     * Update the weapon's Properties value in the GameCharacter as it is edited.
     */
    private class PropertiesTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.setProperties(value);
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
     * Update the weapon's value in the GameCharacter. If we are creating a new Weapon, this class
     * ignores the GameCharacter update and just updates the Damage Summary.
     */
    private class DamageDiceMultiplierTextWatcher extends UpdateTextWatcher {
        @Override
        public void updateValue(Weapon weapon, String value) {
            weapon.getDamage().setDiceQuantity(getIntFromString(value));
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isEditing) {
                super.afterTextChanged(s);
            }
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
     * Observer for character events. This initializes the view with character info such as STR/DEX
     * and proficiency modifier.
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
            mDex = StatHelper.getScoreModifier(gameCharacter.getDefenseStats().getDexScore());
            mStr = StatHelper.getScoreModifier(gameCharacter.getDefenseStats().getStrScore());
            mProficiencyBonus = CharacterHelper.getProficiencyBonus(gameCharacter.getInfo().getLevel());

            updateHitModifier();
            updateDamageSummary();
            initializeScoreSelector();
        }
    }
}
