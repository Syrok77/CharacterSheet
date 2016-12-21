package com.paragonfervour.charactersheet.spells.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDao;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.character.model.Spell;
import com.paragonfervour.charactersheet.fragment.ComponentBaseFragment;
import com.paragonfervour.charactersheet.injection.Injectors;
import com.paragonfervour.charactersheet.spells.adapter.SpellsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * Displays the user's Spell list, grouped by spell casting level.
 */
public class SpellsFragment extends ComponentBaseFragment {

    @Inject
    CharacterDao mCharacterDao;

    @BindView(R.id.spells_fragment_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.spells_fragment_empty_spell_list)
    TextView mEmptySpellListView;

    private static final String TAG = SpellsFragment.class.getSimpleName();

    public static SpellsFragment newInstance() {
        return new SpellsFragment();
    }

    private SpellsAdapter mSpellsAdapter;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private Unbinder mUnbinder;

    // region lifecycle methods --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.currentActivityComponent().inject(this);
        mSpellsAdapter = new SpellsAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spells_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        // set up recycler view
        mRecyclerView.setAdapter(mSpellsAdapter);

        mCompositeSubscription.add(mCharacterDao.getActiveCharacter()
                .subscribe(this::onActiveCharacter, this::onActiveCharacterError));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeSubscription.clear();
        mUnbinder.unbind();
    }

    // endregion

    private void onActiveCharacter(GameCharacter gameCharacter) {
        // update view to display the current character
        List<Spell> spells = gameCharacter.getSpells();
        mSpellsAdapter.setSpells(spells, gameCharacter);
        mSpellsAdapter.notifyDataSetChanged();

        // show or hide the empty spell list text
        if (spells.isEmpty()) {
            mEmptySpellListView.setVisibility(View.VISIBLE);
        } else {
            mEmptySpellListView.setVisibility(View.GONE);
        }
    }

    private void onActiveCharacterError(Throwable e) {
        // handle error loading character
        Log.e(TAG, "Error loading character!", e);
    }
}

