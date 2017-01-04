package com.paragonfervour.charactersheet.fragment;

import android.content.Intent;
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
import com.paragonfervour.charactersheet.component.FloatingActionButtonComponent;
import com.paragonfervour.charactersheet.injection.Injectors;
import com.paragonfervour.charactersheet.offense.activity.AddWeaponActivity;
import com.paragonfervour.charactersheet.spells.activity.AddSpellActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CharacterPagerFragment extends ComponentBaseFragment {

    @Inject
    FloatingActionButtonComponent mFloatingActionButtonComponent;

    @BindView(R.id.character_view_pager)
    ViewPager mViewPager;

    private Unbinder mUnbinder;

    public static CharacterPagerFragment newInstance() {
        return new CharacterPagerFragment();
    }

    // region lifecycle methods --------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.currentActivityComponent().inject(this);
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
        mViewPager.addOnPageChangeListener(new PageChangeListener());

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

    // endregion

    // region listeners ----------------------------------------------------------------------------

    /**
     * Handle FAB click in the spell fragment. This will open the {@link AddSpellActivity}.
     *
     * @param v view that was clicked.
     */
    private void onSpellsFabClick(View v) {
        startActivity(new Intent(getActivity(), AddSpellActivity.class));
    }

    /**
     * Handle FAB click in the offense fragment. This will open the {@link AddWeaponActivity}.
     *
     * @param v view that was clicked.
     */
    private void onOffenseFabClick(View v) {
        startActivity(new Intent(getActivity(), AddWeaponActivity.class));
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == CharacterPagerAdapter.INDEX_SPELLS) {
                mFloatingActionButtonComponent.showFab(CharacterPagerFragment.this::onSpellsFabClick);
            } else if (position == CharacterPagerAdapter.INDEX_OFFENSE) {
                mFloatingActionButtonComponent.showFab(CharacterPagerFragment.this::onOffenseFabClick);
            } else {
                mFloatingActionButtonComponent.hideFab();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    // endregion

}
