package com.lyraf.oneavatarplease;

import android.app.Application;
import android.content.Context;
import com.lyraf.oneavatarplease.dagger.components.AvatarGeneratorComponent;
import com.lyraf.oneavatarplease.dagger.components.DaggerAvatarGeneratorComponent;
import com.lyraf.oneavatarplease.dagger.modules.AppModule;
import com.lyraf.oneavatarplease.dagger.modules.AvatarGeneratorModule;

public class App extends Application {
  private AvatarGeneratorComponent mAvatarGeneratorComponent;

  public static AvatarGeneratorComponent getAvatarGeneratorComponent(Context context) {
    App app = (App) context.getApplicationContext();
    return app.mAvatarGeneratorComponent;
  }

  @Override public void onCreate() {
    super.onCreate();

    mAvatarGeneratorComponent = DaggerAvatarGeneratorComponent.builder()
        .appModule(new AppModule(this))
        .avatarGeneratorModule(new AvatarGeneratorModule())
        .build();
  }
}
