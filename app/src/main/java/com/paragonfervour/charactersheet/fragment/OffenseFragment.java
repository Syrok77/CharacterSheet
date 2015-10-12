package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.view.WeaponComponent;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Fragment that displays a character's offensive abilities. This includes equipped weapons, as well as
 * proficiency bonus. Probably.
 */
public class OffenseFragment extends RoboFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.offense_main_weapon)
    private WeaponComponent mMainHand;

    @InjectView(R.id.offense_offhand_weapon)
    private WeaponComponent mOffHand;

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
    }

    private void updateUI(GameCharacter character) {
        mMainHand.applyWeaponModel(character.getOffenseStats().getMainHand());
        mOffHand.applyWeaponModel(character.getOffenseStats().getOffHand());
    }

    private class EditWeaponComponentClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            WeaponComponent comp = (WeaponComponent)v;
            Toast.makeText(getActivity(), "Edit " + comp.getWeaponName() + ": unimplemented", Toast.LENGTH_SHORT).show();
        }
    }
}
