package com.paragonfervour.charactersheet.stats.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Skill;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateChaSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateConSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateDexSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateIntSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateStrSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateWisSubscriber;
import com.paragonfervour.charactersheet.stats.observer.health.UpdateHPSubscriber;
import com.paragonfervour.charactersheet.stats.observer.health.UpdateMaxHpSubscriber;
import com.paragonfervour.charactersheet.stats.observer.health.UpdateTempHPSubscriber;
import com.paragonfervour.charactersheet.view.SkillValueComponent;
import com.paragonfervour.charactersheet.view.StatValueComponent;

import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import rx.Observer;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Fragment containing a view that shows the user's stats. Stats include HP, character scores, skills,
 * etc.
 */
public class StatsFragment extends RoboFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

    // region injected views -----------------------------------------------------------------------

    @InjectView(R.id.stats_health_stat_component)
    private StatValueComponent mHealthComponent;

    @InjectView(R.id.stats_temp_health_stat_component)
    private StatValueComponent mTempHpComponent;

    @InjectView(R.id.stats_max_health_stat_component)
    private StatValueComponent mMaxHealthComponent;

    @InjectView(R.id.stats_health_summary_value)
    private TextView mHealthSummary;

    @InjectView(R.id.stats_score_str_control)
    private StatValueComponent mStrength;

    @InjectView(R.id.stats_score_str_mod)
    private TextView mStrengthModifier;

    @InjectView(R.id.stats_score_con_control)
    private StatValueComponent mConstitution;

    @InjectView(R.id.stats_score_con_mod)
    private TextView mConstitutionModifier;

    @InjectView(R.id.stats_score_dex_control)
    private StatValueComponent mDexterity;

    @InjectView(R.id.stats_score_dex_mod)
    private TextView mDexterityModifier;

    @InjectView(R.id.stats_score_int_control)
    private StatValueComponent mIntelligence;

    @InjectView(R.id.stats_score_int_mod)
    private TextView mIntelligenceModifier;

    @InjectView(R.id.stats_score_wis_control)
    private StatValueComponent mWisdom;

    @InjectView(R.id.stats_score_wis_mod)
    private TextView mWisdomModifier;

    @InjectView(R.id.stats_score_cha_control)
    private StatValueComponent mCharisma;

    @InjectView(R.id.stats_score_cha_mod)
    private TextView mCharismaModifier;

    @InjectView(R.id.stats_skill_section)
    private ViewGroup mSkillsSection;

    // endregion

    // region constructors -------------------------------------------------------------------------

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }
    public StatsFragment() {}

    // endregion


    private CompositeSubscription mCompositeSubscription;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();

        bindHealthValues();
        bindAbilityScores();

        mCharacterDAO.getActiveCharacter()
                .subscribe(new Observer<GameCharacter>() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }

    /**
     * Initialize all stat views to show the stats of the given GameCharacter.
     *
     * @param character game character whose stats we are going to show on screen.
     */
    private void initializeView(GameCharacter character) {
        mHealthComponent.setValue(character.getDefenseStats().getHitPoints());
        mMaxHealthComponent.setValue(character.getDefenseStats().getMaxHp());
        mTempHpComponent.setValue(character.getDefenseStats().getTempHp());

        mStrength.setValue(character.getDefenseStats().getStrScore());
        mConstitution.setValue(character.getDefenseStats().getConScore());
        mDexterity.setValue(character.getDefenseStats().getDexScore());
        mIntelligence.setValue(character.getDefenseStats().getIntScore());
        mWisdom.setValue(character.getDefenseStats().getWisScore());
        mCharisma.setValue(character.getDefenseStats().getChaScore());

        buildSkillsView(character.getSkills());

        updateHealthSummary();
    }

    private void buildSkillsView(List<Skill> skills) {
        mSkillsSection.removeAllViews();

        for (Skill skill : skills) {
            SkillValueComponent skillView = new SkillValueComponent(getContext());
            skillView.setSkillName(skill.getName());
            skillView.setSkillModifier(skill.getValue());

            skillView.setComponentColor(getResources().getColor(R.color.blue_200));

            //android:layout_gravity="end"
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            skillView.setLayoutParams(params);

            mSkillsSection.addView(skillView);
        }
    }

    /**
     * Bind stat components for ability scores to the active GameCharacter model.
     */
    private void bindAbilityScores() {
        final String modFormat = getString(R.string.stat_ability_score_modifier_format);

        mStrength.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer strength) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateStrSubscriber(strength));

                String strMod = String.format(modFormat, StatHelper.getScoreModifierString(strength));
                mStrengthModifier.setText(strMod);
            }
        });

        mConstitution.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer constitution) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateConSubscriber(constitution));

                String conMod = String.format(modFormat, StatHelper.getScoreModifierString(constitution));
                mConstitutionModifier.setText(conMod);
            }
        });

        mDexterity.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer dexterity) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateDexSubscriber(dexterity));

                String dexMod = String.format(modFormat, StatHelper.getScoreModifierString(dexterity));
                mDexterityModifier.setText(dexMod);
            }
        });

        mIntelligence.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer intelligence) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateIntSubscriber(intelligence));

                String intMod = String.format(modFormat, StatHelper.getScoreModifierString(intelligence));
                mIntelligenceModifier.setText(intMod);
            }
        });

        mWisdom.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer wisdom) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateWisSubscriber(wisdom));

                String wisMod = String.format(modFormat, StatHelper.getScoreModifierString(wisdom));
                mWisdomModifier.setText(wisMod);
            }
        });

        mCharisma.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer charisma) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateChaSubscriber(charisma));

                String chaMod = String.format(modFormat, StatHelper.getScoreModifierString(charisma));
                mCharismaModifier.setText(chaMod);
            }
        });

    }

    /**
     * Bind stat components for health stats to the active GameCharacter model.
     */
    private void bindHealthValues() {
        mHealthComponent.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer hitPoints) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateHPSubscriber(hitPoints));
                updateHealthSummary();
            }
        });

        mTempHpComponent.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer tempHitPoints) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateTempHPSubscriber(tempHitPoints));
                updateHealthSummary();
            }
        });

        mMaxHealthComponent.getValueObservable().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer maxHealth) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateMaxHpSubscriber(maxHealth));
                updateHealthSummary();
            }
        });
    }

    /**
     * Update the health summary text, which shows effective hp / max hp.
     */
    private void updateHealthSummary() {
        String healthFormat = getString(R.string.stat_health_summary_format);
        int effectiveHp = mTempHpComponent.getValue() + mHealthComponent.getValue();
        String healthSummary = String.format(healthFormat, effectiveHp, mMaxHealthComponent.getValue());

        SpannableString spannableString = new SpannableString(healthSummary);
        if (effectiveHp < mMaxHealthComponent.getValue() / 2) {
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.dangerous_red));
            spannableString.setSpan(foregroundColorSpan, 0, healthSummary.indexOf('/'), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        mHealthSummary.setText(spannableString);
    }

}