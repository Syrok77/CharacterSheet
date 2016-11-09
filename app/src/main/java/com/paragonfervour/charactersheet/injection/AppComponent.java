package com.paragonfervour.charactersheet.injection;


import com.paragonfervour.charactersheet.application.PaperlessApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    ActivityComponent plus(ActivityModule module);

    void inject(PaperlessApplication paperlessApplication);

}
