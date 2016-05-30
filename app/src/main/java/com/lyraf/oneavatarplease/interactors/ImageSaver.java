package com.lyraf.oneavatarplease.interactors;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import com.lyraf.oneavatarplease.utils.Constants;

public class ImageSaver {
  Application mApplication;

  public ImageSaver(Application application) {
    mApplication = application;
  }

  public int saveImage(Bitmap image, String title) {
    if (ContextCompat.checkSelfPermission(mApplication, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      return Constants.RESULT_IMAGE_NO_WRITE_EXTERNAL_PERMISSION;
    }

    String result =
        MediaStore.Images.Media.insertImage(mApplication.getContentResolver(), image, title, "");

    if (result != null) {
      return Constants.RESULT_IMAGE_SAVED;
    } else {
      return Constants.RESULT_IMAGE_NOT_SAVED;
    }
  }
}
