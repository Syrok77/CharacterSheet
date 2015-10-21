package com.paragonfervour.charactersheet.stats.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.helper.DiceHelper;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Skill;
import com.paragonfervour.charactersheet.fragment.ComponentBaseFragment;
import com.paragonfervour.charactersheet.helper.SnackbarHelper;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;
import com.paragonfervour.charactersheet.stats.observer.UpdateInspirationSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateChaSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateConSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateDexSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateIntSubscriber;
import com.paragonfervour.charactersheet.stats.observer.abilityscore.UpdateStrSubscriber;
import com.paragonfervour.charactersheet.stats.observer.health.UpdateMaxHpSubscriber;
import com.paragonfervour.charactersheet.stats.observer.health.UpdateTempHPSubscriber;
import com.paragonfervour.charactersheet.stats.widget.DiceDialogFactory;
import com.paragonfervour.charactersheet.stats.widget.SkillDialogFactory;
import com.paragonfervour.charactersheet.view.DeathSaveViewComponent;
import com.paragonfervour.charactersheet.view.SkillValueViewComponent;
import com.paragonfervour.charactersheet.view.StatValueViewComponent;

import java.util.Iterator;
import java.util.List;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**
 * Fragment containing a view that shows the user's stats. Stats include HP, character scores, skills,
 * etc.
 */
public class StatsFragment extends ComponentBaseFragment {

    @Inject
    private CharacterDAO mCharacterDAO;

    @Inject
    private SkillDialogFactory mSkillDialogFactory;

    // region injected views -----------------------------------------------------------------------

    @InjectView(R.id.stats_speed)
    private TextView mSpeed;

    @InjectView(R.id.stats_inspriation)
    private CheckBox mInspiration;

    @InjectView(R.id.stats_initiative)
    private TextView mInitiative;

    @InjectView(R.id.stats_health_stat_component)
    private StatValueViewComponent mHealthComponent;

    @InjectView(R.id.stats_temp_health_stat_component)
    private StatValueViewComponent mTempHpComponent;

    @InjectView(R.id.stats_max_health_stat_component)
    private StatValueViewComponent mMaxHealthComponent;

    @InjectView(R.id.stats_death_saves)
    private DeathSaveViewComponent mDeathSaveComponent;

    @InjectView(R.id.stats_health_summary_value)
    private TextView mHealthSummary;

    @InjectView(R.id.stats_health_dice_indicator)
    private ImageView mDiceIndicator;

    @InjectView(R.id.stats_health_dice_roll)
    private TextView mHitDiceRollButton;

    @InjectView(R.id.stats_score_str_control)
    private StatValueViewComponent mStrength;

    @InjectView(R.id.stats_score_str_mod)
    private TextView mStrengthModifier;

    @InjectView(R.id.stats_score_con_control)
    private StatValueViewComponent mConstitution;

    @InjectView(R.id.stats_score_con_mod)
    private TextView mConstitutionModifier;

    @InjectView(R.id.stats_score_dex_control)
    private StatValueViewComponent mDexterity;

    @InjectView(R.id.stats_score_dex_mod)
    private TextView mDexterityModifier;

    @InjectView(R.id.stats_score_int_control)
    private StatValueViewComponent mIntelligence;

    @InjectView(R.id.stats_score_int_mod)
    private TextView mIntelligenceModifier;

    @InjectView(R.id.stats_score_wis_control)
    private StatValueViewComponent mWisdom;

    @InjectView(R.id.stats_score_wis_mod)
    private TextView mWisdomModifier;

    @InjectView(R.id.stats_score_cha_control)
    private StatValueViewComponent mCharisma;

    @InjectView(R.id.stats_score_cha_mod)
    private TextView mCharismaModifier;

    @InjectView(R.id.stats_skill_section)
    private ViewGroup mSkillsSection;

    @InjectView(R.id.stats_skill_passive_wis)
    private TextView mPassiveWisdom;

    @InjectView(R.id.stats_add_skill_button)
    private Button mAddSkillButton;

    // endregion

    // region constructors -------------------------------------------------------------------------

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    public StatsFragment() {
    }

    // endregion

    @SuppressWarnings("unused") // doesn't matter if TAG is unused, it's nice to keep it around.
    private static final String TAG = StatsFragment.class.getSimpleName();

    private CompositeSubscription mCompositeSubscription;
    private AlertDialog mActiveAlert;
    private AddUpdateSkillListener mSkillListener = new AddUpdateSkillListener();

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
        bindAttributes();

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

        if (mActiveAlert != null) {
            if (mActiveAlert.isShowing()) {
                mActiveAlert.dismiss();
            }
            mActiveAlert = null;
        }
    }

    /**
     * Initialize all stat views to show the stats of the given GameCharacter.
     *
     * @param character game character whose stats we are going to show on screen.
     */
    private void initializeView(GameCharacter character) {
        mSpeed.setText(StatHelper.makeSpeedText(getActivity(), character.getSpeed()));
        mInspiration.setChecked(character.isInspired());
        updateInitiative(character.getDefenseStats().getDexScore());

        mHealthComponent.setValue(character.getDefenseStats().getHitPoints());
        mTempHpComponent.setValue(character.getDefenseStats().getTempHp());
        mDiceIndicator.setImageResource(DiceHelper.getDiceDrawable(character.getDefenseStats().getHitDice()));
        mDiceIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiceDialogFactory.createDicePickerDialog(getActivity())
                        .subscribe(new HitDicePickerObserver());
            }
        });

        updateMaxHp(character.getDefenseStats().getMaxHp());

        mStrength.setValue(character.getDefenseStats().getStrScore());
        mConstitution.setValue(character.getDefenseStats().getConScore());
        mDexterity.setValue(character.getDefenseStats().getDexScore());
        mIntelligence.setValue(character.getDefenseStats().getIntScore());
        mWisdom.setValue(character.getDefenseStats().getWisScore());
        mCharisma.setValue(character.getDefenseStats().getChaScore());

        buildSkillsView(character.getSkills());
        mAddSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveAlert = mSkillDialogFactory.createSkillDialog(mSkillListener);
                mActiveAlert.show();
            }
        });
    }

    /**
     * Remove a skill from UI and game character
     *
     * @param skill         skill to remove from ui and character.
     * @param gameCharacter game character from which to remove the skill.
     */
    private void removeSkill(Skill skill, final GameCharacter gameCharacter) {
        Iterator<Skill> iterator = gameCharacter.getSkills().iterator();
        while (iterator.hasNext()) {
            Skill existing = iterator.next();
            if (existing.equalsIgnoreValue(skill)) {
                iterator.remove();
                if (getView() != null) {
                    // Undo action would use this copy. Just in case.
                    final Skill existingCopy = new Skill();
                    existingCopy.setName(existing.getName());
                    existingCopy.setValue(existing.getValue());

                    SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), R.string.toast_skill_removed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addSkill(existingCopy, gameCharacter);
                                }
                            }));
                }
            }
        }

        // Find the associated view to delete
        for (int i = 0; i < mSkillsSection.getChildCount(); ++i) {
            if (mSkillsSection.getChildAt(i) instanceof SkillValueViewComponent) {
                SkillValueViewComponent skillComponent = (SkillValueViewComponent) mSkillsSection.getChildAt(i);
                if (skillComponent.getSkillName().equalsIgnoreCase(skill.getName())) {
                    mSkillsSection.removeView(skillComponent);
                    break;
                }
            }
        }
    }

    /**
     * Update the skill in the GameCharacter and in the UI.
     *
     * @param skill         New skill values.
     * @param gameCharacter Game character to update.
     */
    private void updateSkill(Skill skill, GameCharacter gameCharacter) {
        Iterator<Skill> iterator = gameCharacter.getSkills().iterator();
        int index = -1;
        while (iterator.hasNext()) {
            Skill existing = iterator.next();
            index++;

            // Replace existing skill with passed in skill.
            if (existing.equalsIgnoreValue(skill)) {
                iterator.remove();
                if (getView() != null) {
                    String snackbarText = String.format(getString(R.string.toast_skill_updated), skill.getName());
                    SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), snackbarText, Snackbar.LENGTH_LONG));
                }
            }
        }

        if (index >= 0 && index < gameCharacter.getSkills().size()) {
            gameCharacter.getSkills().add(index, skill);
        } else {
            gameCharacter.getSkills().add(skill);
        }

        // Find the associated view to update
        for (int i = 0; i < mSkillsSection.getChildCount(); ++i) {
            if (mSkillsSection.getChildAt(i) instanceof SkillValueViewComponent) {
                SkillValueViewComponent skillComponent = (SkillValueViewComponent) mSkillsSection.getChildAt(i);
                if (skillComponent.getSkillName().equalsIgnoreCase(skill.getName())) {
                    skillComponent.setSkillModifier(skill.getValue());
                }
            }
        }
    }

    /**
     * Add a skill to the game character and the UI.
     *
     * @param skill         skill to add.
     * @param gameCharacter character to add skill to.
     */
    private void addSkill(Skill skill, GameCharacter gameCharacter) {
        for (Skill existing : gameCharacter.getSkills()) {
            if (existing.equalsIgnoreValue(skill) &&
                    getView() != null) {
                SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), R.string.toast_skill_already_exists, Snackbar.LENGTH_LONG));
                return;
            }
        }

        addSkillView(skill);
        gameCharacter.getSkills().add(skill);
    }

    /**
     * Add a skill to the UI.
     *
     * @param skill Skill to add a view for.
     */
    private void addSkillView(final Skill skill) {
        SkillValueViewComponent skillView = new SkillValueViewComponent(getContext());
        skillView.setSkillName(skill.getName());
        skillView.setSkillModifier(skill.getValue());

        //noinspection deprecation
        skillView.setComponentColor(getResources().getColor(R.color.tertiary_500));

        //android:layout_gravity="end"
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        skillView.setLayoutParams(params);

        skillView.setSkillClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveAlert = mSkillDialogFactory.updateSkillDialog(mSkillListener, skill);
                mActiveAlert.show();
            }
        });

        mSkillsSection.addView(skillView);
    }

    private void buildSkillsView(List<Skill> skills) {
        mSkillsSection.removeAllViews();

        for (Skill skill : skills) {
            addSkillView(skill);
        }
    }

    private void bindAttributes() {
        mInspiration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCharacterDAO.getActiveCharacter().subscribe(new UpdateInspirationSubscriber(isChecked));
            }
        });
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
                updateInitiative(dexterity);
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
                Log.d(TAG, "Updating wisdom: " + wisdom);
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
            public void call(final Integer hitPoints) {
                mCharacterDAO.getActiveCharacter().subscribe(new Subscriber<GameCharacter>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GameCharacter gameCharacter) {
                        gameCharacter.getDefenseStats().setHitPoints(hitPoints);

                        if (hitPoints <= 0 && mDeathSaveComponent.getVisibility() != View.VISIBLE) {
                            mDeathSaveComponent.setVisibility(View.VISIBLE);
                            mDeathSaveComponent.reset(gameCharacter.getDefenseStats().getFailAttempts());
                        } else if (hitPoints > 0 && mDeathSaveComponent.getVisibility() == View.VISIBLE) {
                            mDeathSaveComponent.setVisibility(View.GONE);
                        }

                        unsubscribe();
                    }
                });
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

        mHitDiceRollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCharacterDAO.getActiveCharacter().subscribe(new RollHitDiceSubscriber());
            }
        });

        mCompositeSubscription.add(Observable.combineLatest(mDeathSaveComponent.getFailuresObservable(), mCharacterDAO.getActiveCharacter(), new Func2<Integer, GameCharacter, Observable<GameCharacter>>() {
            @Override
            public Observable<GameCharacter> call(Integer failCount, GameCharacter gameCharacter) {
                gameCharacter.getDefenseStats().setFailAttempts(failCount);
                return Observable.just(gameCharacter);
            }
        }).subscribe());
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
            //noinspection deprecation
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.dangerous_red));
            spannableString.setSpan(foregroundColorSpan, 0, healthSummary.indexOf('/'), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        mHealthSummary.setText(spannableString);
    }

    /**
     * Update the passive wisdom value for the character.
     *
     * @param gameCharacter active game character.
     */
    private void updatePassiveWisdom(GameCharacter gameCharacter) {
        String passiveWisdom = StatHelper.makePassiveWisdomText(getActivity(),
                gameCharacter.getSkills(),
                gameCharacter.getDefenseStats().getWisScore());
        Log.d(TAG, "Update passive wisdom: " + passiveWisdom);
        mPassiveWisdom.setText(passiveWisdom);
    }

    /**
     * Update the max health component to the given value.
     *
     * @param maxHealth new max health to put into component.
     */
    private void updateMaxHp(int maxHealth) {
        mMaxHealthComponent.setValue(maxHealth);
    }

    /**
     * Update initiative view with the given dexterity value.
     *
     * @param dexterity character dexterity.
     */
    private void updateInitiative(int dexterity) {
        mInitiative.setText(StatHelper.makeInitiativeText(getActivity(), dexterity));
    }

    // region listeners ----------------------------------------------------------------------------

    private class AddUpdateSkillListener implements SkillDialogFactory.SkillListener {
        @Override
        public void onSkillCreated(final Skill skill) {
            mCharacterDAO.getActiveCharacter()
                    .subscribe(new Action1<GameCharacter>() {
                        @Override
                        public void call(GameCharacter gameCharacter) {
                            addSkill(skill, gameCharacter);
                            updatePassiveWisdom(gameCharacter);

                        }
                    });
        }

        @Override
        public void onSkillUpdated(final Skill skill) {
            mCharacterDAO.getActiveCharacter()
                    .subscribe(new Action1<GameCharacter>() {
                        @Override
                        public void call(GameCharacter gameCharacter) {
                            updateSkill(skill, gameCharacter);
                            updatePassiveWisdom(gameCharacter);
                        }
                    });
        }

        @Override
        public void onSkillDeleted(final Skill skill) {
            mCharacterDAO.getActiveCharacter()
                    .subscribe(new Action1<GameCharacter>() {
                        @Override
                        public void call(GameCharacter gameCharacter) {
                            removeSkill(skill, gameCharacter);
                            updatePassiveWisdom(gameCharacter);
                        }
                    });
        }
    }

    private class RollHitDiceSubscriber extends Subscriber<GameCharacter> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(GameCharacter gameCharacter) {
            Dice hitDice = gameCharacter.getDefenseStats().getHitDice();
            final int roll = hitDice.roll();
            final int maxHp = gameCharacter.getDefenseStats().getMaxHp();

            updateMaxHp(maxHp + roll);

            // Display the change to the user.
            String updateToast = String.format(getString(R.string.toast_max_hp_updated_format), roll);
            if (getView() != null) {
                SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), updateToast, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateMaxHp(maxHp);
                                updateHealthSummary();
                            }
                        }));
            }

            unsubscribe();
        }
    }

    public class UpdateWisSubscriber extends Subscriber<GameCharacter> {

        private int mWis;

        public UpdateWisSubscriber(int wis) {
            mWis = wis;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(GameCharacter gameCharacter) {
            Log.d(TAG, "Update wisdom");
            gameCharacter.getDefenseStats().setWisScore(mWis);
            updatePassiveWisdom(gameCharacter);

            unsubscribe();
        }
    }

    private class HitDicePickerObserver implements Observer<Dice> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(final Dice dice) {
            mCompositeSubscription.add(mCharacterDAO.getActiveCharacter()
                    .subscribe(new Subscriber<GameCharacter>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(GameCharacter gameCharacter) {
                            gameCharacter.getDefenseStats().setHitDice(dice);
                            mDiceIndicator.setImageResource(DiceHelper.getDiceDrawable(dice));
                            unsubscribe();
                        }
                    }));
        }
    }

    // endregion

}