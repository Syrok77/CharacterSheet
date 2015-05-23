package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.dao.CharacterDAO;
import com.paragonfervour.charactersheet.model.GameCharacter;

import roboguice.fragment.RoboFragment;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class OffenseFragment extends RoboFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

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
    }

    private void updateUI(GameCharacter character) {

    }
}
