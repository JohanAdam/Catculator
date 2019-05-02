package com.example.catculate.di.component;

import com.example.catculate.ui.activities.MainActivity;
import com.example.catculate.di.module.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

  void inject(MainActivity mainActivity);

}
