package com.paragonfervour.charactersheet.spells.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.Ability;
import com.paragonfervour.charactersheet.character.model.DefenseStats;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.spells.activity.AddSpellActivity;
import com.paragonfervour.charactersheet.stats.helper.StatHelper;
import com.paragonfervour.charactersheet.widget.ListSubHeaderComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that displays a given list of Spells, grouped by casting level. Each group is separated by
 * a header describing the group.
 */
public class SpellsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<StupidDataModel> mSpellData = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private GameCharacter mGameCharacter;

    private enum ViewType {
        CASTING_SUMMARY,
        HEADER,
        SPELL,
    }

    public SpellsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    // region recycler adapter overrides -----------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.SPELL.ordinal()) {
            return new SpellViewHolder(mLayoutInflater.inflate(R.layout.spells_fragment_spell_item, parent, false));
        } else if (viewType == ViewType.CASTING_SUMMARY.ordinal()) {
            return new CastingSummaryViewHolder(mLayoutInflater.inflate(R.layout.spell_casting_summary, parent, false));
        } else {
            return new HeaderViewHolder(new ListSubHeaderComponent(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SpellViewHolder) {
            SpellViewHolder spell = (SpellViewHolder) holder;
            spell.bind(mSpellData.get(position - 1).getSpell());
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder header = (HeaderViewHolder) holder;
            header.bind(mSpellData.get(position - 1).getSpellLevel());
        } else if (holder instanceof CastingSummaryViewHolder) {
            CastingSummaryViewHolder summaryHolder = (CastingSummaryViewHolder) holder;
            summaryHolder.bind(mGameCharacter);
        }
    }

    @Override
    public int getItemCount() {
        int size = mSpellData.size();
        if (mGameCharacter != null) {
            size += 1;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ViewType.CASTING_SUMMARY.ordinal();
        }

        final int dataPosition = position - 1;
        StupidDataModel data = mSpellData.get(dataPosition);
        if (data.getSpell() == null) {
            return ViewType.HEADER.ordinal();
        } else {
            return ViewType.SPELL.ordinal();
        }
    }

    // endregion

    public void setSpells(List<Spell> spells, GameCharacter gameCharacter) {
        mGameCharacter = gameCharacter;

        // organize spells by spell level
        Map<Integer, List<Spell>> spellsByLevel = new LinkedHashMap<>();
        for (Spell spell : spells) {
            //noinspection Java8CollectionsApi
            if (spellsByLevel.get(spell.getLevel()) == null) {
                spellsByLevel.put(spell.getLevel(), new ArrayList<>());
            }

            spellsByLevel.get(spell.getLevel()).add(spell);
        }

        // linearize data for the adapter
        mSpellData.clear();
        for (Integer level : spellsByLevel.keySet()) {
            mSpellData.add(new StupidDataModel(level, null));
            for (Spell spell : spellsByLevel.get(level)) {
                mSpellData.add(new StupidDataModel(level, spell));
            }
        }
    }

    class CastingSummaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.spell_casting_summary_attack)
        TextView mAttack;

        @BindView(R.id.spell_casting_summary_spell_dc)
        TextView mSpellDc;

        @BindView(R.id.spell_casting_summary_ability_spinner)
        Spinner mAbility;

        CastingSummaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GameCharacter gameCharacter) {
            // find index corresponding to user's casting ability.
            Ability ability = Ability.values()[gameCharacter.getInfo().getCastingAbility()];
            final String[] castingAbilities = itemView.getResources().getStringArray(R.array.spell_abilities);
            int index = 0;
            for (int i = 0; i < castingAbilities.length; ++i) {
                if (ability.getAbbreviation().equalsIgnoreCase(castingAbilities[i])) {
                    index = i;
                }
            }

            mAbility.setSelection(index);
            mAbility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Ability selectedAbility = Ability.abilityForAbbreviation(castingAbilities[position]);
                    mGameCharacter.getInfo().setCastingAbility(selectedAbility.ordinal());
                    mGameCharacter.getInfo().save();

                    // update modifiers accordingly
                    final int score;
                    DefenseStats stats = mGameCharacter.getDefenseStats();
                    if (selectedAbility == Ability.INT) {
                        score = stats.getIntScore();
                    } else if (selectedAbility == Ability.CHA) {
                        score = stats.getChaScore();
                    } else if (selectedAbility == Ability.WIS) {
                        score = stats.getWisScore();
                    } else {
                        score = 10;
                    }
                    int modifier = StatHelper.getScoreModifier(score) + CharacterHelper.getProficiencyBonus(mGameCharacter.getInfo().getLevel());
                    mAttack.setText(StatHelper.getStatIndicator(modifier) + modifier);
                    mSpellDc.setText(String.valueOf(8 + modifier));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    class SpellViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.spell_item_name)
        TextView mName;

        @BindView(R.id.spell_item_collapsing_section)
        View mCollapsingSection;

        @BindView(R.id.spell_item_edit_button)
        View mEditButton;

        @BindView(R.id.spell_item_range)
        TextView mRange;

        @BindView(R.id.spell_item_casting_time)
        TextView mCastingTime;

        @BindView(R.id.spell_item_components)
        TextView mComponents;

        @BindView(R.id.spell_item_duration)
        TextView mDuration;

        @BindView(R.id.spell_item_description)
        TextView mDescription;

        SpellViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Spell spell) {
            mName.setText(spell.getName());
            mRange.setText(spell.getRange());
            mCastingTime.setText(spell.getCastingTime());
            mComponents.setText(spell.getComponents());
            mDuration.setText(spell.getDuration());
            mDescription.setText(spell.getDescription());

            itemView.setOnClickListener(v -> {
                int visibility = mCollapsingSection.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
                mCollapsingSection.setVisibility(visibility);
                mEditButton.setVisibility(visibility);
            });
            mEditButton.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent editSpell = new Intent(context, AddSpellActivity.class);
                editSpell.putExtra(AddSpellActivity.KEY_SPELL_ID, spell.getId());
                context.startActivity(editSpell);
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private ListSubHeaderComponent mListSubHeaderComponent;

        HeaderViewHolder(ListSubHeaderComponent itemView) {
            super(itemView);
            mListSubHeaderComponent = itemView;
        }

        public void bind(int spellLevel) {
            String header;
            if (spellLevel == 0) {
                // Level 0 spells are called Cantrips
                header = itemView.getResources().getString(R.string.spell_header_zero);
            } else {
                header = itemView.getResources().getString(R.string.spell_header_level_format, spellLevel);
            }
            mListSubHeaderComponent.setHeaderText(header);
        }
    }

    /**
     * Store spell data for the view holders to bind to. This is stupid because linearizing non-linear
     * data into an easily used adapter data source is obnoxious, and it's dumb to have a model with
     * fields that you have to just assume are populated or not populated based on the corresponding
     * ViewType.
     */
    private class StupidDataModel {
        private final int mSpellLevel;
        private final Spell mSpell;

        StupidDataModel(int spellLevel, Spell spell) {
            mSpellLevel = spellLevel;
            mSpell = spell;
        }

        int getSpellLevel() {
            return mSpellLevel;
        }

        @Nullable
        Spell getSpell() {
            return mSpell;
        }
    }
}
