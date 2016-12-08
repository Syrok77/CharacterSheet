package com.paragonfervour.charactersheet.spells.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.spells.activity.AddSpellActivity;
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

    public SpellsAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    // region recycler adapter overrides -----------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SpellViewHolder.VIEW_TYPE) {
            return new SpellViewHolder(mLayoutInflater.inflate(R.layout.spells_fragment_spell_item, parent, false));
        } else {
            return new HeaderViewHolder(new ListSubHeaderComponent(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SpellViewHolder) {
            SpellViewHolder spell = (SpellViewHolder) holder;
            spell.bind(mSpellData.get(position).getSpell());
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder header = (HeaderViewHolder) holder;
            header.bind(mSpellData.get(position).getSpellLevel());
        }
    }

    @Override
    public int getItemCount() {
        return mSpellData.size();
    }

    @Override
    public int getItemViewType(int position) {
        StupidDataModel data = mSpellData.get(position);
        if (data.getSpell() == null) {
            return HeaderViewHolder.VIEW_TYPE;
        } else {
            return SpellViewHolder.VIEW_TYPE;
        }
    }

    // endregion

    public void setSpells(List<Spell> spells) {
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

    class SpellViewHolder extends RecyclerView.ViewHolder {
        private static final int VIEW_TYPE = 1;

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
        private static final int VIEW_TYPE = 2;

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
