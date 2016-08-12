package com.lyraf.oneavatarplease.injection.components;

import com.lyraf.oneavatarplease.avatargenerator.AvatarGeneratorFragment;
import com.lyraf.oneavatarplease.injection.modules.AppModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class }) public interface AppComponent {
  void inject(AvatarGeneratorFragment avatarGeneratorFragment);
}
