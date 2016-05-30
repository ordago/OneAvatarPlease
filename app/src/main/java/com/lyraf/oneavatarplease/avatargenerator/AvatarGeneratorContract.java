package com.lyraf.oneavatarplease.avatargenerator;

import android.graphics.Bitmap;

public interface AvatarGeneratorContract {
  interface View {
    void showSnackbar(int messageResId);

    void requestWriteExternalPermission(String permission, int requestCode);
  }

  interface Presenter {
    void shareAvatar();

    void saveAvatar(Bitmap avatar, String avatarName);

    void setView(View view);
  }
}
