package com.paragonfervour.charactersheet.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.fragment.DefenseFragment;
import com.paragonfervour.charactersheet.fragment.OffenseFragment;
import com.paragonfervour.charactersheet.spells.fragment.SpellsFragment;
import com.paragonfervour.charactersheet.stats.fragment.StatsFragment;

public class CharacterPagerAdapter extends FragmentPagerAdapter {

    /**
     * Enum which matches the adapter position to the page type. The order of this enum
     * dictates the order of Items created by the Adapter: the adapter's position is equal to the
     * {@link #ordinal()} of the Index.
     */
    private enum Index {
        STATS,
        OFFENSE,
        SPELLS,
        DEFENSE
    }

    public static final int INDEX_SPELLS = Index.SPELLS.ordinal();

    private Context mContext;

    public CharacterPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        Index index = Index.values()[position];
        switch (index) {
            case STATS:
                return StatsFragment.newInstance();
            case OFFENSE:
                return OffenseFragment.newInstance();
            case SPELLS:
                return SpellsFragment.newInstance();
            case DEFENSE:
                return DefenseFragment.newInstance();
            default:
                return null;
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
        Index index = Index.values()[position];
        switch (index) {
            case STATS:
                return mContext.getString(R.string.nav_stats_title);
            case OFFENSE:
                return mContext.getString(R.string.nav_offense_title);
            case SPELLS:
                return mContext.getString(R.string.nav_spells_title);
            case DEFENSE:
                return mContext.getString(R.string.nav_defense_title);
            default:
                return null;
        }
    }
}
