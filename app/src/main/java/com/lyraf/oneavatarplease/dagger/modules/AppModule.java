package com.lyraf.oneavatarplease.dagger.modules;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AppModule {
  Application mApplication;

  public AppModule(Application application) {
    mApplication = application;
  }

  @Provides @Singleton public Application providesApplication() {
    return mApplication;
  }
}
