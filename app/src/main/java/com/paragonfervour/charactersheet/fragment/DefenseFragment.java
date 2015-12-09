package com.paragonfervour.charactersheet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.R;

// TODO: This is a stub.
public class DefenseFragment extends ComponentBaseFragment {

    public static DefenseFragment newInstance() {
        return new DefenseFragment();
    }

    public DefenseFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.defense_fragment, container, false);
    }
}
