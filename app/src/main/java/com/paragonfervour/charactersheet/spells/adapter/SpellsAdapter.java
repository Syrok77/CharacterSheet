package com.paragonfervour.charactersheet.spells.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.character.model.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that displays a given list of Spells, grouped by casting level. Each group is separated by
 * a header describing the group.
 */
public class SpellsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArray<List<Spell>> mSpells = new SparseArray<>();

    // region recycler adapter overrides -----------------------------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mSpells != null) {
            return mSpells.size() + getHeaderCount();
        } else {
            return 0;
        }
    }

    // endregion

    /**
     * Get the number of headers to display. Since spells are grouped by level, this will be equal
     * to the number of different spell levels in the character's spell book. For example, if the player
     * knows X level 0 spells and Y level 2 spells, this will return 2;
     *
     * @return the number of headers to be displayed by this adapter.
     */
    private int getHeaderCount() {
        if (mSpells != null) {
            return mSpells.size();
        }
        return 0;
    }

    public void setSpells(List<Spell> spells) {
        for (Spell spell : spells) {
            if (mSpells.get(spell.getLevel()) == null) {
                mSpells.put(spell.getLevel(), new ArrayList<>());
            }

            List<Spell> spellsForLevel = mSpells.get(spell.getLevel());
            spellsForLevel.add(spell);
        }
    }

}
