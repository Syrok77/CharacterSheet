package com.paragonfervour.charactersheet.injection;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Activity lifecycle tracker that monitors the Activity scope {@link ActivityModule}.
 */
public class ActivityInjectorTracker implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // Bump this scope to the top of the stack
        Injectors.activityComponent(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Bump this scope to the top of the stack
        Injectors.activityComponent(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Release this scope when the Activity is destroyed
        Injectors.releaseActivityScope(activity);
    }
}
