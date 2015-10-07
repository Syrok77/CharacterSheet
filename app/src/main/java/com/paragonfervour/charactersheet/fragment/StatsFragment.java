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
import com.paragonfervour.charactersheet.view.StatValueComponent;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Fragment containing a view that shows the user's stats. Stats include HP, character scores, skills,
 * etc.
 */
public class StatsFragment extends RoboFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.stats_health_stat_component)
    private StatValueComponent mHealthComponent;

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHealthComponent.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(final Integer hitPoints) {
                mCharacterDAO.getActiveCharacter().subscribe(new Observer<GameCharacter>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GameCharacter gameCharacter) {
                        gameCharacter.getDefenseStats().setHitPoints(hitPoints);
                    }
                });
            }
        });


        mCharacterDAO.getActiveCharacter().subscribe(new Observer<GameCharacter>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GameCharacter gameCharacter) {
                initializeView(gameCharacter);
            }
        });
    }

    private void initializeView(GameCharacter character) {
        mHealthComponent.setValue(character.getDefenseStats().getHitPoints());
    }
}