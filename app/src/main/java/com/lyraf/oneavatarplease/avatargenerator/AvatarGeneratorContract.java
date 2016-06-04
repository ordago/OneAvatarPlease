package com.lyraf.oneavatarplease.avatargenerator;

import android.graphics.Bitmap;

public interface AvatarGeneratorContract {
  interface View {
    void showSnackbar(int messageResId);

    void requestWriteExternalPermission(String permission, int requestCode);

    void showAvatar();

    void showAvatarIdentifierError();

    void hideAvatarIdentifierError();
  }

  interface Presenter {
    void setView(View view);

    void saveAvatar(Bitmap avatar, String avatarName);

    void validateAvatarIdentifier(String identifier);

    void checkConnectivity();
  }
}
