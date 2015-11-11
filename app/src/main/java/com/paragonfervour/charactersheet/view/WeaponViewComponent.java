package com.paragonfervour.charactersheet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Weapon;

/**
 * View component that displays, but does not update, a Weapon model.
 */
public class WeaponViewComponent extends LinearLayout {

    private TextView mNameText;
    private TextView mPropertiesText;
    private TextView mDamageText;
    private TextView mValueText;
    private TextView mWeightText;

    private View mNameSeparator;

    private Weapon mWeapon;


    public WeaponViewComponent(Context context) {
        super(context);
        init();
    }

    public WeaponViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeaponViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.component_weapon, this);

        mNameText = (TextView) findViewById(R.id.component_weapon_name);
        mPropertiesText = (TextView) findViewById(R.id.component_weapon_properties);
        mDamageText = (TextView) findViewById(R.id.component_weapon_damage);
        mValueText = (TextView) findViewById(R.id.component_weapon_value);
        mWeightText = (TextView) findViewById(R.id.component_weapon_weight);

        mNameSeparator = findViewById(R.id.component_weapon_separator);

        if (isInEditMode()) {
            applyWeaponModel(Weapon.createDefault());
        }
    }

    public void applyWeaponModel(Weapon weapon) {
        mNameText.setText(weapon.getName());
        mPropertiesText.setText(weapon.getProperties());
        mDamageText.setText(weapon.getDamage().toString());
        mValueText.setText(String.valueOf(weapon.getValue() + getContext().getString(R.string.standard_value)));
        mWeightText.setText(getContext().getString(R.string.standard_weight_format, weapon.getWeight()));

        if (weapon.getProperties() == null || weapon.getProperties().isEmpty()) {
            mNameSeparator.setVisibility(View.GONE);
        }
        else {
            mNameSeparator.setVisibility(View.VISIBLE);
        }

        mWeapon = weapon;
    }

    /**
     * Create a Weapon model that represents this view component.
     *
     * @return a new Weapon model representing this component.
     */
    public Weapon getWeapon() {
        return mWeapon;
    }

}