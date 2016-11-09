package com.paragonfervour.charactersheet.injection;


import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Module for creating Per Activity scoped injection.
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    Context provideContext() {
        return mActivity;
    }
}
