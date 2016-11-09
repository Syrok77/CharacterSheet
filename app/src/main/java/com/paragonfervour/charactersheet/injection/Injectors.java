package com.paragonfervour.charactersheet.injection;


import android.app.Activity;
import android.app.Application;

public class Injectors {

    private static AppComponent sAppComponent;
    private static final ScopeStack<Activity, ActivityComponent> sScopeStack = new ScopeStack<>();

    /**
     * Set the {@link AppComponent}. This should only be called in {@link Application#onCreate()}.
     *
     * @param appComponent app component that serves as the Application level injector.
     */
    public static void setAppComponent(AppComponent appComponent) {
        sAppComponent = appComponent;
    }

    /**
     * Release the scope of a given Activity. This should only be called within
     * {@link Activity#onDestroy()}. This can be done globally using {@link Application.ActivityLifecycleCallbacks}.
     *
     * @param activity activity to release scope.
     */
    public static void releaseActivityScope(Activity activity) {
        sScopeStack.releaseComponent(activity);
    }

    public static ActivityComponent activityComponent(Activity activity) {
        ActivityComponent component = sScopeStack.getComponent(activity);
        if (component == null) {
            sScopeStack.createComponentForScope(activity, () -> sAppComponent.plus(new ActivityModule(activity)));
        }
        return component;
    }

    public static AppComponent appComponent() {
        return sAppComponent;
    }

    public static ActivityComponent currentActivityComponent() {
        return sScopeStack.getTop();
    }
}
