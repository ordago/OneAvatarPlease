package com.lyraf.oneavatarplease;

import android.app.Application;
import android.content.Context;
import com.lyraf.oneavatarplease.dagger.components.AppComponent;
import com.lyraf.oneavatarplease.dagger.components.DaggerAppComponent;
import com.lyraf.oneavatarplease.dagger.modules.AppModule;

public class App extends Application {
  private AppComponent mAppComponent;

  public static AppComponent getAppComponent(Context context) {
    App app = (App) context.getApplicationContext();
    return app.mAppComponent;
  }

  @Override public void onCreate() {
    super.onCreate();

    mAppComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
  }
}
