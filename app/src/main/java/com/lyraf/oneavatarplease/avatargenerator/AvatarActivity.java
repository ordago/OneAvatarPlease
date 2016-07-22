package com.lyraf.oneavatarplease.avatargenerator;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.utils.Constants;

public class AvatarActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_avatar);

    initFragment();
  }

  private void initFragment() {
    FragmentManager manager = getSupportFragmentManager();

    AvatarGeneratorFragment fragment = (AvatarGeneratorFragment) manager.findFragmentByTag(
        Constants.TAG_AVATAR_GENERATOR_FRAGMENT);

    if (fragment == null) {
      fragment = new AvatarGeneratorFragment();
      manager.beginTransaction()
          .add(R.id.avatar_content, fragment, Constants.TAG_AVATAR_GENERATOR_FRAGMENT)
          .commit();
    }
  }
}
