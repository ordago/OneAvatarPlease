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

  private Bitmap mGeneratedAvatar;
  private String mLastIdentifier;
  private String mLastSavedAvatarIdentifier;

  @Inject
  public AvatarGeneratorPresenter(ImageSaver imageSaver, ConnectivityChecker connectivityChecker) {
    mImageSaver = imageSaver;
    mConnectivityChecker = connectivityChecker;
  }

  @Override public void setView(AvatarGeneratorContract.View avatarGeneratorView) {
    mAvatarGeneratorView = avatarGeneratorView;
  }

  @Override public void saveAvatar(String identifier) {
    if (mGeneratedAvatar == null) {
      mAvatarGeneratorView.showSnackbar(R.string.error_avatar_not_generated);
    } else if (TextUtils.equals(identifier, mLastSavedAvatarIdentifier)) {
      mAvatarGeneratorView.showGalleryActionSnackbar(R.string.error_avatar_already_saved,
          R.string.action_avatar_show);
    } else {
      String result = mImageSaver.saveImage(mGeneratedAvatar, identifier);

      if (result == null) {
        mAvatarGeneratorView.showSnackbar(R.string.error_avatar_not_saved);
      } else if (TextUtils.isEmpty(result)) {
        mAvatarGeneratorView.requestWriteExternalPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
      } else {
        mLastSavedAvatarIdentifier = identifier;
        mAvatarGeneratorView.showGalleryActionSnackbar(R.string.message_avatar_saved,
            R.string.action_avatar_show);
      }
    }
  }

  @Override public void setGeneratedAvatar(Bitmap generatedAvatar) {
    if (generatedAvatar != null) {
      mAvatarGeneratorView.showSave();
      mGeneratedAvatar = generatedAvatar;
    }
  }

  @Override public void restoreGeneratedAvatar() {
    if (mGeneratedAvatar != null) {
      mAvatarGeneratorView.showAvatar(mGeneratedAvatar);
    }
  }

  @Override public void validateIdentifier(String identifier) {
    if (TextUtils.isEmpty(identifier)) {
      mAvatarGeneratorView.showAvatarIdentifierError();
    } else if (TextUtils.equals(identifier, mLastIdentifier)) {
      mAvatarGeneratorView.showSnackbar(R.string.error_avatar_already_generated);
    } else {
      mAvatarGeneratorView.hideSave();

      mLastIdentifier = identifier;
      mAvatarGeneratorView.showSnackbar(R.string.message_avatar_generating);
      mAvatarGeneratorView.hideAvatarIdentifierError();
      mAvatarGeneratorView.loadAvatar();
    }
  }

  @Override public void openGallery() {
    mAvatarGeneratorView.showGallery(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
