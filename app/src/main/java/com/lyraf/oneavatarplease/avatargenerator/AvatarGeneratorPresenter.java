package com.lyraf.oneavatarplease.avatargenerator;

import android.Manifest;
import android.graphics.Bitmap;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.interactors.ImageSaver;
import com.lyraf.oneavatarplease.utils.Constants;

public class AvatarGeneratorPresenter implements AvatarGeneratorContract.Presenter {
  private AvatarGeneratorContract.View mAvatarGeneratorView;
  private ImageSaver mImageSaver;

  public AvatarGeneratorPresenter(ImageSaver imageSaver) {
    mImageSaver = imageSaver;
  }

  @Override public void setView(AvatarGeneratorContract.View avatarGeneratorView) {
    mAvatarGeneratorView = avatarGeneratorView;
  }

  @Override public void shareAvatar() {

  }

  @Override public void saveAvatar(Bitmap avatar, String avatarName) {
    int result = mImageSaver.saveImage(avatar, avatarName);

    switch (result) {
      case Constants.RESULT_IMAGE_NO_WRITE_EXTERNAL_PERMISSION:
        mAvatarGeneratorView.requestWriteExternalPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        break;

      case Constants.RESULT_IMAGE_SAVED:
        mAvatarGeneratorView.showSnackbar(R.string.message_avatar_saved);
        break;

      case Constants.RESULT_IMAGE_NOT_SAVED:
        mAvatarGeneratorView.showSnackbar(R.string.message_avatar_not_saved);
        break;
    }
  }
}
