package com.paragonfervour.charactersheet.application;


import com.orm.SugarApp;
import com.paragonfervour.charactersheet.injection.ActivityInjectorTracker;
import com.paragonfervour.charactersheet.injection.DaggerAppComponent;
import com.paragonfervour.charactersheet.injection.Injectors;

public class PaperlessApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        Injectors.setAppComponent(DaggerAppComponent.builder()
                .build());
        registerActivityLifecycleCallbacks(new ActivityInjectorTracker());
    }
}
