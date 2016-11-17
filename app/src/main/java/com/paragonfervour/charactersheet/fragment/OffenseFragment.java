package com.paragonfervour.charactersheet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.injection.Injectors;
import com.paragonfervour.charactersheet.offense.activity.AddWeaponActivity;
import com.paragonfervour.charactersheet.offense.component.WeaponListViewComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Fragment that displays a character's offensive abilities. This includes equipped weapons, as well as
 * proficiency bonus. Probably.
 */
public class OffenseFragment extends ComponentBaseFragment {

    @Inject
    CharacterDao mCharacterDao;

    @BindView(R.id.offense_proficiency_bonus)
    TextView mProficiencyBonus;

    @BindView(R.id.offense_main_weapon)
    WeaponListViewComponent mMainHandList;

    @BindView(R.id.offense_offhand_weapon)
    WeaponListViewComponent mOffHandList;

    private static final String TAG = OffenseFragment.class.getSimpleName();

    private CompositeSubscription mCompositeSubscription;
    private Unbinder mUnbinder;

    public OffenseFragment() {
        super();
    }

    public static OffenseFragment newInstance() {
        return new OffenseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.currentActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offense_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
        mUnbinder = ButterKnife.bind(this, view);

        mCompositeSubscription.add(mCharacterDao.getActiveCharacter()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GameCharacter>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error getting game character", e);
                    }

                    @Override
                    public void onNext(GameCharacter gameCharacter) {
                        updateUI(gameCharacter);
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeSubscription.unsubscribe();
        mUnbinder.unbind();
    }

    @OnClick(R.id.offense_add_weapon_button)
    void onAddWeaponClick() {
        Intent intent = new Intent(getActivity(), AddWeaponActivity.class);
        startActivity(intent);
    }

    private void updateUI(GameCharacter character) {
        mMainHandList.setWeapons(character.getMainHandWeapons(), character);
        mOffHandList.setWeapons(character.getOffHandWeapons(), character);

        int proficiency = CharacterHelper.getProficiencyBonus(character.getInfo().getLevel());
        String displayableProficiencyBonus = getString(R.string.offense_proficiency_summary, proficiency);
        mProficiencyBonus.setText(displayableProficiencyBonus);
    }
}
