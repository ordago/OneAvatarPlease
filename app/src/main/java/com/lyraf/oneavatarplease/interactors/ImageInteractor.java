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

package com.lyraf.oneavatarplease.interactors;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import javax.inject.Inject;

public class ImageInteractor {
  private final Application mApplication;

  @Inject public ImageInteractor(Application application) {
    mApplication = application;
  }

  public void saveImage(Bitmap image, String title, OnImageSavedCallback callback) {
    if (ContextCompat.checkSelfPermission(mApplication, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      callback.OnImageSaveNoPermission();
    } else {
      String imageUrl =
          MediaStore.Images.Media.insertImage(mApplication.getContentResolver(), image, title, "");
      if (imageUrl == null) {
        callback.OnImageSaveNotSaved();
      } else {
        callback.OnImageSaveSaved(title, imageUrl);
      }
    }
  }

  public interface OnImageSavedCallback {
    void OnImageSaveNoPermission();

    void OnImageSaveNotSaved();

    void OnImageSaveSaved(String identifier, String url);
  }
}
