package com.example.catculate.di.module;

import com.example.catculate.utils.SharedPreferencesManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ApplicationModule {

  public ApplicationModule() {
  }

  @Provides
  @Singleton
  public SharedPreferencesManager provideSharedPreferencesManager() {
    return new SharedPreferencesManager();
  }

}
