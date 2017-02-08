package com.paragonfervour.charactersheet.stats.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.Dice;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Skill;
import com.paragonfervour.charactersheet.component.DicePickerViewComponent;
import com.paragonfervour.charactersheet.fragment.ComponentBaseFragment;
import com.paragonfervour.charactersheet.helper.SnackbarHelper;
import com.paragonfervour.charactersheet.injection.Injectors;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;
import com.paragonfervour.charactersheet.stats.widget.SkillDialogFactory;
import com.paragonfervour.charactersheet.view.DeathSaveViewComponent;
import com.paragonfervour.charactersheet.view.SkillValueViewComponent;
import com.paragonfervour.charactersheet.view.StatValueViewComponent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;

/**
 * Fragment containing a view that shows the user's stats. Stats include HP, character scores, skills,
 * etc.
 */
public class StatsFragment extends ComponentBaseFragment {

    @Inject
    CharacterDao mCharacterDao;

    @Inject
    SkillDialogFactory mSkillDialogFactory;

    // region injected views -----------------------------------------------------------------------

    @BindView(R.id.stats_speed)
    TextView mSpeed;

    @BindView(R.id.stats_inspriation)
    CheckBox mInspiration;

    @BindView(R.id.stats_initiative)
    TextView mInitiative;

    @BindView(R.id.stats_health_stat_component)
    StatValueViewComponent mHealthComponent;

    @BindView(R.id.stats_temp_health_stat_component)
    StatValueViewComponent mTempHpComponent;

    @BindView(R.id.stats_max_health_stat_component)
    StatValueViewComponent mMaxHealthComponent;

    @BindView(R.id.stats_death_saves)
    DeathSaveViewComponent mDeathSaveComponent;

    @BindView(R.id.stats_health_summary_value)
    TextView mHealthSummary;

    @BindView(R.id.stats_health_dice_indicator)
    DicePickerViewComponent mHitDicePickerComponent;

    @BindView(R.id.stats_score_str_control)
    StatValueViewComponent mStrength;

    @BindView(R.id.stats_score_str_mod)
    TextView mStrengthModifier;

    @BindView(R.id.stats_score_con_control)
    StatValueViewComponent mConstitution;

    @BindView(R.id.stats_score_con_mod)
    TextView mConstitutionModifier;

    @BindView(R.id.stats_score_dex_control)
    StatValueViewComponent mDexterity;

    @BindView(R.id.stats_score_dex_mod)
    TextView mDexterityModifier;

    @BindView(R.id.stats_score_int_control)
    StatValueViewComponent mIntelligence;

    @BindView(R.id.stats_score_int_mod)
    TextView mIntelligenceModifier;

    @BindView(R.id.stats_score_wis_control)
    StatValueViewComponent mWisdom;

    @BindView(R.id.stats_score_wis_mod)
    TextView mWisdomModifier;

    @BindView(R.id.stats_score_cha_control)
    StatValueViewComponent mCharisma;

    @BindView(R.id.stats_score_cha_mod)
    TextView mCharismaModifier;

    @BindView(R.id.stats_skill_section)
    ViewGroup mSkillsSection;

    @BindView(R.id.stats_skill_passive_wis)
    TextView mPassiveWisdom;

    @BindView(R.id.stats_add_skill_button)
    Button mAddSkillButton;

    // endregion

    // region constructors -------------------------------------------------------------------------

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    public StatsFragment() {
    }

    // endregion

    private static final String TAG = StatsFragment.class.getSimpleName();

    private String mModifierFormat;

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private Unbinder mUnbinder;
    private AlertDialog mActiveAlert;
    private AddUpdateSkillListener mSkillListener = new AddUpdateSkillListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.currentActivityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stats_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        mModifierFormat = getString(R.string.stat_ability_score_modifier_format);

        bindHealthValues();
        bindAbilityScores();
        bindAttributes();

        mCompositeSubscription.add(mCharacterDao.getActiveCharacterStream()
                .subscribe(this::initializeView, this::onGameCharacterLoadError));

        // dumb view compat stuff
        //noinspection deprecation
        int color = getResources().getColor(R.color.secondary_accent);
        ViewCompat.setBackgroundTintList(mAddSkillButton, ColorStateList.valueOf(color));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeSubscription.clear();
        mUnbinder.unbind();

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

        mHealthComponent.initializeValue(character.getDefenseStats().getHitPoints());
        mTempHpComponent.initializeValue(character.getDefenseStats().getTempHp());
        mHitDicePickerComponent.setDice(character.getDefenseStats().getHitDice());
        mHitDicePickerComponent.getDiceObservable().subscribe(new HitDicePickerObserver());
        updateDeathSummary(character);

        mMaxHealthComponent.initializeValue(character.getDefenseStats().getMaxHp());
        updateHealthSummary();

        int strength = character.getDefenseStats().getStrScore();
        mStrength.initializeValue(strength);
        updateCounterModifier(strength, mStrengthModifier);

        int constitution = character.getDefenseStats().getConScore();
        mConstitution.initializeValue(constitution);
        updateCounterModifier(constitution, mConstitutionModifier);

        int dexterity = character.getDefenseStats().getDexScore();
        mDexterity.initializeValue(dexterity);
        updateCounterModifier(dexterity, mDexterityModifier);

        int intelligence = character.getDefenseStats().getIntScore();
        mIntelligence.initializeValue(intelligence);
        updateCounterModifier(intelligence, mIntelligenceModifier);

        int wisdom = character.getDefenseStats().getWisScore();
        mWisdom.initializeValue(wisdom);
        updateCounterModifier(wisdom, mWisdomModifier);
        updatePassiveWisdom(character);

        int charisma = character.getDefenseStats().getChaScore();
        mCharisma.initializeValue(charisma);
        updateCounterModifier(charisma, mCharismaModifier);

        buildSkillsView(character.getSkills());
    }

    /**
     * Remove a skill from UI and game character
     *
     * @param skill         skill to remove from ui and character.
     * @param gameCharacter game character from which to remove the skill.
     */
    private void removeSkill(final Skill skill, final GameCharacter gameCharacter) {
        skill.delete();

        // Show an 'undo' snackbar for this action.
        if (getView() != null) {
            SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), R.string.toast_skill_removed, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, v -> saveSkill(skill, gameCharacter)));
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
        saveSkill(skill, gameCharacter);
        if (getView() != null) {
            String snackbarText = String.format(getString(R.string.toast_skill_updated), skill.getName());
            SnackbarHelper.showSnackbar(getActivity(), Snackbar.make(getView(), snackbarText, Snackbar.LENGTH_LONG));
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

    private void saveSkill(Skill skill, GameCharacter gameCharacter) {
        if (mCharacterDao.saveSkill(skill, gameCharacter)) {
            addSkillView(skill);
        }

        updatePassiveWisdom(gameCharacter);
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
        skillView.setComponentColor(getResources().getColor(R.color.tertiary_accent));

        //android:layout_gravity="end"
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        skillView.setLayoutParams(params);

        skillView.setSkillClickListener(v -> {
            mActiveAlert = mSkillDialogFactory.updateSkillDialog(mSkillListener, skill);
            mActiveAlert.show();
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
        mInspiration.setOnCheckedChangeListener((buttonView, isChecked) ->
                mCharacterDao.getActiveCharacter()
                        .subscribe(gameCharacter -> gameCharacter.setIsInspired(isChecked),
                                this::onGameCharacterLoadError));
    }

    /**
     * Bind stat components for ability scores to the active GameCharacter model.
     */
    private void bindAbilityScores() {
        mStrength.getValueObservable().subscribe(strength -> {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter ->
                                    gameCharacter.getDefenseStats().setStrScore(strength),
                            this::onGameCharacterLoadError);

            updateCounterModifier(strength, mStrengthModifier);
        });

        mConstitution.getValueObservable().subscribe(constitution -> {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setConScore(constitution),
                            this::onGameCharacterLoadError);

            updateCounterModifier(constitution, mConstitutionModifier);
        });

        mDexterity.getValueObservable().subscribe(dexterity -> {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setDexScore(dexterity),
                            this::onGameCharacterLoadError);

            updateCounterModifier(dexterity, mDexterityModifier);

            updateInitiative(dexterity);
        });

        mIntelligence.getValueObservable().subscribe(intelligence -> {
            mCharacterDao.getActiveCharacter().subscribe(gameCharacter ->
                            gameCharacter.getDefenseStats().setIntScore(intelligence),
                    this::onGameCharacterLoadError);

            updateCounterModifier(intelligence, mIntelligenceModifier);
        });

        mWisdom.getValueObservable().subscribe(wisdom -> {
            Log.d(TAG, "Updating wisdom: " + wisdom);
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> {
                        Log.d(TAG, "Update wisdom");
                        gameCharacter.getDefenseStats().setWisScore(wisdom);
                        updatePassiveWisdom(gameCharacter);
                    }, this::onGameCharacterLoadError);

            updateCounterModifier(wisdom, mWisdomModifier);
        });

        mCharisma.getValueObservable().subscribe(charisma -> {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setChaScore(charisma),
                            this::onGameCharacterLoadError);

            updateCounterModifier(charisma, mCharismaModifier);
        });

    }

    /**
     * Bind stat components for health stats to the active GameCharacter model.
     */
    private void bindHealthValues() {
        mHealthComponent.getValueObservable().subscribe(hitPoints -> {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> {
                        gameCharacter.getDefenseStats().setHitPoints(hitPoints);
                        updateDeathSummary(gameCharacter);
                    }, this::onGameCharacterLoadError);
            updateHealthSummary();
        });

        mTempHpComponent.getValueObservable()
                .subscribe(tempHitPoints -> {
                    mCharacterDao.getActiveCharacter()
                            .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setTempHp(tempHitPoints),
                                    this::onGameCharacterLoadError);
                    updateHealthSummary();
                });

        mMaxHealthComponent.getValueObservable()
                .subscribe(maxHealth -> {
                    mCharacterDao.getActiveCharacter()
                            .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setMaxHp(maxHealth),
                                    this::onGameCharacterLoadError);
                    updateHealthSummary();
                });

        mCompositeSubscription.add(Observable.combineLatest(mDeathSaveComponent.getFailuresObservable(), mCharacterDao.getActiveCharacterStream(), (failCount, gameCharacter) -> {
            gameCharacter.getDefenseStats().setFailAttempts(failCount);
            return Observable.just(gameCharacter);
        }).subscribe());
    }

    private void onGameCharacterLoadError(Throwable e) {
        Log.i(TAG, "There was an error loading the character", e);
    }

    /**
     * Update a modifier's text view for the given score.
     *
     * @param score        score to get modifier from.
     * @param modifierView TextView that displays the modifier.
     */
    private void updateCounterModifier(int score, TextView modifierView) {
        String strMod = String.format(mModifierFormat, StatHelper.getScoreModifierString(score));
        modifierView.setText(strMod);
    }

    /**
     * Update the health summary text, which shows effective hp / max hp.
     */
    private void updateHealthSummary() {
        int effectiveHp = mTempHpComponent.getValue() + mHealthComponent.getValue();
        String healthSummary = getString(R.string.stat_health_summary_format, effectiveHp, mMaxHealthComponent.getValue());

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
     * Update the death save section for the game character. This will hide/reset the view if the
     * character is alive, and show if the character has <= 0 hit points.
     *
     * @param gameCharacter active game character.
     */
    private void updateDeathSummary(GameCharacter gameCharacter) {
        int hitPoints = gameCharacter.getDefenseStats().getHitPoints();

        if (hitPoints <= 0 && mDeathSaveComponent.getVisibility() != View.VISIBLE) {
            mDeathSaveComponent.setVisibility(View.VISIBLE);
            mDeathSaveComponent.reset(gameCharacter.getDefenseStats().getFailAttempts());
        } else if (hitPoints > 0 && mDeathSaveComponent.getVisibility() == View.VISIBLE) {
            mDeathSaveComponent.setVisibility(View.GONE);
        }
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

    @OnClick(R.id.stats_add_skill_button)
    void onAddSkillClick() {
        mActiveAlert = mSkillDialogFactory.createSkillDialog(mSkillListener);
        mActiveAlert.show();
    }

    private class AddUpdateSkillListener implements SkillDialogFactory.SkillListener {
        @Override
        public void onSkillCreated(final Skill skill) {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> saveSkill(skill, gameCharacter),
                            StatsFragment.this::onGameCharacterLoadError);
        }

        @Override
        public void onSkillUpdated(final Skill skill) {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> {
                        updateSkill(skill, gameCharacter);
                        updatePassiveWisdom(gameCharacter);
                    }, StatsFragment.this::onGameCharacterLoadError);
        }

        @Override
        public void onSkillDeleted(final Skill skill) {
            mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> {
                        removeSkill(skill, gameCharacter);
                        updatePassiveWisdom(gameCharacter);
                    }, StatsFragment.this::onGameCharacterLoadError);
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
            mCompositeSubscription.add(mCharacterDao.getActiveCharacter()
                    .subscribe(gameCharacter -> gameCharacter.getDefenseStats().setHitDice(dice),
                            StatsFragment.this::onGameCharacterLoadError));
        }
    }

    // endregion

}