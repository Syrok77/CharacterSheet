package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.R;

import roboguice.fragment.RoboFragment;

// TODO: This is a stub
public class StatsFragment extends RoboFragment {

    public static StatsFragment newInstance() {
        return new StatsFragment();
    }

    public StatsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}