package com.paragonfervour.charactersheet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.offense.activity.AddWeaponActivity;
import com.paragonfervour.charactersheet.view.WeaponViewComponent;

import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Fragment that displays a character's offensive abilities. This includes equipped weapons, as well as
 * proficiency bonus. Probably.
 */
public class OffenseFragment extends ComponentBaseFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.offense_main_weapon)
    private WeaponViewComponent mMainHand;

    @InjectView(R.id.offense_offhand_weapon)
    private WeaponViewComponent mOffHand;

    @InjectView(R.id.offense_add_weapon_button)
    private Button mAddWeaponButton;

    public OffenseFragment() {
        super();
    }

    public static OffenseFragment newInstance() {
        return new OffenseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offense, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCharacterDAO.getActiveCharacter()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GameCharacter>() {
                    @Override
                    public void call(GameCharacter character) {
                        updateUI(character);
                    }
                });

        mMainHand.setOnClickListener(new EditWeaponComponentClickListener());
        mOffHand.setOnClickListener(new EditWeaponComponentClickListener());
        mAddWeaponButton.setOnClickListener(new AddWeaponClickListener());
    }

    private void updateUI(GameCharacter character) {
        mMainHand.applyWeaponModel(character.getOffenseStats().getMainHand());
        mOffHand.applyWeaponModel(character.getOffenseStats().getOffHand());
    }

    private class EditWeaponComponentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            WeaponViewComponent comp = (WeaponViewComponent)v;

            Intent intent = new Intent(getActivity(), AddWeaponActivity.class);
            intent.putExtra(AddWeaponActivity.EXTRA_WEAPON_MODEL, comp.getWeapon());
            boolean isMainHand = mMainHand == comp;
            intent.putExtra(AddWeaponActivity.EXTRA_IS_MAIN_HAND, isMainHand);
            startActivity(intent);
        }
    }

    private class AddWeaponClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddWeaponActivity.class);
            startActivity(intent);
        }
    }
}
