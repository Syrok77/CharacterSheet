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
import com.paragonfervour.charactersheet.offense.component.WeaponListViewComponent;

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
    private WeaponListViewComponent mMainHandList;

    @InjectView(R.id.offense_offhand_weapon)
    private WeaponListViewComponent mOffHandList;

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

        mAddWeaponButton.setOnClickListener(new AddWeaponClickListener());
    }

    private void updateUI(GameCharacter character) {
        mMainHandList.setWeapons(character.getOffenseStats().getMainHandWeapons());
        mOffHandList.setWeapons(character.getOffenseStats().getOffHandWeapons());
    }

    private class AddWeaponClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), AddWeaponActivity.class);
            startActivity(intent);
        }
    }
}
