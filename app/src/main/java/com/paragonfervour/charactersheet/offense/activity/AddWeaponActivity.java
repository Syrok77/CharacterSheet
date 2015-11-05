package com.paragonfervour.charactersheet.offense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.ComponentBaseActivity;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.Weapon;

import roboguice.inject.InjectView;

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

    public static final String EXTRA_WEAPON_MODEL = "extra_weapon_model";

    // temp
    public static final String EXTRA_IS_MAIN_HAND = "extra_is_main_hand";
    private boolean isMainHand;

    private Weapon mWeapon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weapon);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        Intent i = getIntent();
        if (i != null) {
            mWeapon = (Weapon) i.getSerializableExtra(EXTRA_WEAPON_MODEL);
            isMainHand = i.getBooleanExtra(EXTRA_IS_MAIN_HAND, true);
        }

        if (mWeapon == null) {
            // We are creating a new weapon
            mToolbar.setTitle(R.string.nav_add_weapon_title);

            mWeapon = Weapon.createDefault();
        } else {
            // We are editing an existing weapon.
            mToolbar.setTitle(R.string.nav_edit_weapon_title);
        }

        updateWeaponView();
    }

    private void updateWeaponView() {

    }
}
