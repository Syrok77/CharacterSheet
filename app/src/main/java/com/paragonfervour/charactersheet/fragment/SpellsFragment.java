package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.R;

// TODO: This is a stub.
public class SpellsFragment extends ComponentBaseFragment {

    public static SpellsFragment newInstance() {
        return new SpellsFragment();
    }

    public SpellsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.spells_fragment, container, false);
    }
}

