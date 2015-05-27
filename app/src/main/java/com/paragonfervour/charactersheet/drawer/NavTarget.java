package com.paragonfervour.charactersheet.drawer;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.paragonfervour.charactersheet.R;


public enum NavTarget {
    OFFENSE(R.string.nav_offense_title, R.drawable.abc_ic_clear_mtrl_alpha),
    DEFENSE(R.string.nav_defense_title, R.drawable.abc_edit_text_material),
    BIOGRAPHY(R.string.nav_bio_title, R.drawable.ic_drawer);

    private @StringRes int mTitleRes;
    private @DrawableRes int mIconRes;

    NavTarget(@StringRes int titleResId, @DrawableRes int iconResId) {
        mTitleRes = titleResId;
        mIconRes = iconResId;
    }

    @StringRes
    public int getTitleRes() {
        return mTitleRes;
    }

    @DrawableRes
    public int getIconRes() {
        return mIconRes;
    }
}
