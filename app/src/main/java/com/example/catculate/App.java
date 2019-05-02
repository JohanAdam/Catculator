package com.example.catculate;

import android.app.Application;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.example.catculate.di.component.ApplicationComponent;
import com.example.catculate.di.component.DaggerApplicationComponent;
import com.example.catculate.di.module.ApplicationModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class App extends Application {

  private static App app;
  private ApplicationComponent component;

  public static App getApp() {
    return app;
  }

  @Override
  public void onCreate() {
    super.onCreate();

    //Initialize application context.
    app = this;

    //Initialized logging tools.
    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      Timber.plant(new CrashlyticsTree());
    }

    //Initialized App component.
    buildComponent();

  }

  public ApplicationComponent getApplicationComponent(){
    return component;
  }

  private void buildComponent() {
    component = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule())
        .build();
  }

  private class CrashlyticsTree extends Timber.Tree {

    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message,
        @Nullable Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
        return;
      }

      Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
      Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
      Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

      if (t == null) {
        Crashlytics.logException(new Exception(message));
      } else {
        Crashlytics.logException(t);
      }
    }
  }
}
