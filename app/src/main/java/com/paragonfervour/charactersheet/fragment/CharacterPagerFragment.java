package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.BaseToolbarActivity;
import com.paragonfervour.charactersheet.adapter.CharacterPagerAdapter;

import roboguice.inject.InjectView;


public class CharacterPagerFragment extends ComponentBaseFragment {

    @InjectView(R.id.character_view_pager)
    private ViewPager mViewPager;

    public CharacterPagerFragment() {
    }

    public static CharacterPagerFragment newInstance() {
        return new CharacterPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CharacterPagerAdapter pagerAdapter = new CharacterPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(pagerAdapter);

        BaseToolbarActivity activity = (BaseToolbarActivity) getActivity();
        TabLayout tabLayout = activity.getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        BaseToolbarActivity activity = (BaseToolbarActivity) getActivity();
        TabLayout tabLayout = activity.getTabLayout();
        tabLayout.setVisibility(View.GONE);
        super.onDestroyView();
    }

}
