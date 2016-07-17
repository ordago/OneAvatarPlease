package com.lyraf.oneavatarplease.avatargenerator;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.interactors.ConnectivityChecker;
import com.lyraf.oneavatarplease.interactors.ImageSaver;
import com.lyraf.oneavatarplease.utils.Constants;
import javax.inject.Inject;

public class AvatarGeneratorPresenter implements AvatarGeneratorContract.Presenter {
  private AvatarGeneratorContract.View mAvatarGeneratorView;
  private ImageSaver mImageSaver;
  private ConnectivityChecker mConnectivityChecker;

  @Inject
  public AvatarGeneratorPresenter(ImageSaver imageSaver, ConnectivityChecker connectivityChecker) {
    mImageSaver = imageSaver;
    mConnectivityChecker = connectivityChecker;
  }

  @Override public void setView(AvatarGeneratorContract.View avatarGeneratorView) {
    mAvatarGeneratorView = avatarGeneratorView;
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

  @Override public void validateAvatarIdentifier(String identifier) {
    mAvatarGeneratorView.showSnackbar(R.string.message_avatar_generating);

    if (TextUtils.isEmpty(identifier)) {
      mAvatarGeneratorView.showAvatarIdentifierError();
    } else {
      mAvatarGeneratorView.hideAvatarIdentifierError();
      mAvatarGeneratorView.showAvatar();
    }
  }

  @Override public void checkConnectivity() {
    int result = mConnectivityChecker.isConnectivityAvailable();

    switch (result) {
      case Constants.RESULT_CONNECTIVITY_NO_NETWORK:
        mAvatarGeneratorView.showSnackbar(R.string.error_no_network);
        break;
      case Constants.RESULT_CONNECTIVITY_NO_INTERNET:
        mAvatarGeneratorView.showSnackbar(R.string.error_no_internet);
        break;
    }
  }
}
