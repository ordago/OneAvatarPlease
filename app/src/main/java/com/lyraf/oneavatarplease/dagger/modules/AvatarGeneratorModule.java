package com.lyraf.oneavatarplease.dagger.modules;

import android.app.Application;
import com.lyraf.oneavatarplease.avatargenerator.AvatarGeneratorContract;
import com.lyraf.oneavatarplease.avatargenerator.AvatarGeneratorPresenter;
import com.lyraf.oneavatarplease.interactors.ImageSaver;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class AvatarGeneratorModule {
  public AvatarGeneratorModule() {
  }

  @Provides @Singleton public ImageSaver providesImageSaver(Application application) {
    return new ImageSaver(application);
  }

  @Provides @Singleton public AvatarGeneratorContract.Presenter presenter(ImageSaver imageSaver) {
    return new AvatarGeneratorPresenter(imageSaver);
  }
}
