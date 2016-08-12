package com.lyraf.oneavatarplease.injection.modules;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {
  private final Application mApplication;

  public AppModule(Application application) {
    mApplication = application;
  }

  @Provides @Singleton public Application providesApplication() {
    return mApplication;
  }
}
