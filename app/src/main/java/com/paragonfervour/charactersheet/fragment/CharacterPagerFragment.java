package com.paragonfervour.charactersheet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.activity.CharacterActivity;
import com.paragonfervour.charactersheet.adapter.CharacterPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CharacterPagerFragment extends ComponentBaseFragment {

    @BindView(R.id.character_view_pager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;

    public CharacterPagerFragment() {
    }

    public static CharacterPagerFragment newInstance() {
        return new CharacterPagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.character_pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        CharacterPagerAdapter pagerAdapter = new CharacterPagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(pagerAdapter);

        CharacterActivity activity = (CharacterActivity) getActivity();
        TabLayout tabLayout = activity.getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        CharacterActivity activity = (CharacterActivity) getActivity();
        TabLayout tabLayout = activity.getTabLayout();
        tabLayout.setVisibility(View.GONE);
        super.onDestroyView();

        mUnbinder.unbind();
    }

}
