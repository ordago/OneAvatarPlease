package com.lyraf.oneavatarplease.dagger.components;

import com.lyraf.oneavatarplease.avatargenerator.AvatarGeneratorFragment;
import com.lyraf.oneavatarplease.dagger.modules.AppModule;
import com.lyraf.oneavatarplease.dagger.modules.AvatarGeneratorModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { AppModule.class, AvatarGeneratorModule.class })
public interface AvatarGeneratorComponent {
  void inject(AvatarGeneratorFragment avatarGeneratorFragment);
}
