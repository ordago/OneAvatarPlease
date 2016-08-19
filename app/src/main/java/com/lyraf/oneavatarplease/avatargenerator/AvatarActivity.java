/*
 * MIT License
 *
 * Copyright (c) 2016 Felipe Lyra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
          .add(R.id.content_avatar, fragment, Constants.TAG_AVATAR_GENERATOR_FRAGMENT)
          .commit();
    }
  }
}
