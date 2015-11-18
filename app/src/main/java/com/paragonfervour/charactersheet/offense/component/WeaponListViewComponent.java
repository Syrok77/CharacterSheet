package com.paragonfervour.charactersheet.offense.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Weapon;
import com.paragonfervour.charactersheet.offense.activity.AddWeaponActivity;
import com.paragonfervour.charactersheet.view.WeaponViewComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A component view that displays a List of Weapons as WeaponViewComponents.
 */
public class WeaponListViewComponent extends LinearLayout {

    public WeaponListViewComponent(Context context) {
        super(context);
        init();
    }

    public WeaponListViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WeaponListViewComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(LinearLayout.VERTICAL);

        if (isInEditMode()) {
            List<Weapon> previewWeapons = new ArrayList<>();
            previewWeapons.add(Weapon.createDefault());
            setWeapons(previewWeapons);
        }
    }

    /**
     * Set the List of weapons to display. This will create subviews for the passed in weapons.
     *
     * @param weapons Weapon list to display inside this view.
     */
    public void setWeapons(List<Weapon> weapons) {
        removeAllViews();

        for (Weapon weapon : weapons) {
            WeaponViewComponent component = new WeaponViewComponent(getContext());
            component.applyWeaponModel(weapon);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.weapon_list_item_top_margin), 0, 0);
            component.setLayoutParams(params);

            component.setOnClickListener(new EditWeaponComponentClickListener());

            addView(component);
        }
    }

    private class EditWeaponComponentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            WeaponViewComponent comp = (WeaponViewComponent)v;

            Intent intent = new Intent(getContext(), AddWeaponActivity.class);
            intent.putExtra(AddWeaponActivity.EXTRA_WEAPON_ID, comp.getWeapon().getId());
            getContext().startActivity(intent);
        }
    }
}
