package com.lyraf.oneavatarplease.avatargenerator;

import android.graphics.Bitmap;
import android.net.Uri;

public interface AvatarGeneratorContract {
  interface View {
    void showSnackbar(int messageResId);

    void showGalleryActionSnackbar(int messageResId, int actionResId);

    void requestWriteExternalPermission(String permission, int requestCode);

    void loadAvatar();

    void showAvatar(Bitmap avatar);

    void hideSave();

    void showSave();

    void showAvatarIdentifierError();

    void hideAvatarIdentifierError();

    void showGallery(Uri uri);
  }

  interface Presenter {
    void setView(View view);

    void saveAvatar(String identifier);

    void setGeneratedAvatar(Bitmap generatedAvatar);

    void restoreGeneratedAvatar();

    void validateIdentifier(String identifier);

    void openGallery();

    void checkConnectivity();
  }
}
