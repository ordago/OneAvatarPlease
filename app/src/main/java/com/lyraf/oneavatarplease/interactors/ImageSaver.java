package com.lyraf.oneavatarplease.interactors;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import javax.inject.Inject;

public class ImageSaver {
  Application mApplication;

  @Inject public ImageSaver(Application application) {
    mApplication = application;
  }

  public String saveImage(Bitmap image, String title) {
    if (ContextCompat.checkSelfPermission(mApplication, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      return "";
    }

    return MediaStore.Images.Media.insertImage(mApplication.getContentResolver(), image, title, "");
  }
}
