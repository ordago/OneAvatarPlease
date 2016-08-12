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
