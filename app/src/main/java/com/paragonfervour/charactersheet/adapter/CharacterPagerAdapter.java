package com.paragonfervour.charactersheet.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.fragment.BioFragment;
import com.paragonfervour.charactersheet.fragment.DefenseFragment;
import com.paragonfervour.charactersheet.fragment.OffenseFragment;


public class CharacterPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    @Inject
    public CharacterPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return OffenseFragment.newInstance();
            // todo: spells fragment
            case 1: return DefenseFragment.newInstance();
            case 2: return BioFragment.newInstance();
            default: return null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return view == fragment.getView();
        }
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return mContext.getString(R.string.nav_offense_title);
            // todo: spells fragment
            case 1: return mContext.getString(R.string.nav_defense_title);
            case 2: return mContext.getString(R.string.nav_bio_title);
            default: return null;
        }
    }
}
