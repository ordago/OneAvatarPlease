package com.lyraf.oneavatarplease.avatargenerator;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.interactors.ImageInteractor;
import com.lyraf.oneavatarplease.interactors.NetworkInteractor;
import com.lyraf.oneavatarplease.utils.Constants;
import javax.inject.Inject;

public class AvatarGeneratorPresenter
    implements AvatarGeneratorContract.Presenter, ImageInteractor.OnImageSavedCallback,
    NetworkInteractor.OnConnectivityCheckedCallback {
  private final ImageInteractor mImageInteractor;
  private final NetworkInteractor mNetworkInteractor;
  private AvatarGeneratorContract.View mAvatarGeneratorView;
  private Bitmap mGeneratedAvatar;
  private String mLastIdentifier;
  private String mLastSavedAvatarIdentifier;
  private String mLastSavedAvatarUrl;

  @Inject public AvatarGeneratorPresenter(ImageInteractor imageInteractor,
      NetworkInteractor networkInteractor) {
    mImageInteractor = imageInteractor;
    mNetworkInteractor = networkInteractor;
  }

  @Override public void setView(AvatarGeneratorContract.View avatarGeneratorView) {
    mAvatarGeneratorView = avatarGeneratorView;
  }

  @Override public void saveAvatar(String identifier) {
    if (TextUtils.equals(identifier, mLastSavedAvatarIdentifier)) {
      mAvatarGeneratorView.showGalleryActionSnackbar(R.string.error_avatar_already_saved);
    } else {
      mImageInteractor.saveImage(mGeneratedAvatar, identifier, this);
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
    mAvatarGeneratorView.showGallery(Uri.parse(mLastSavedAvatarUrl));
  }

  @Override public void checkConnectivity() {
    mNetworkInteractor.isConnectivityAvailable(this);
  }

  @Override public void OnImageSaveNoPermission() {
    mAvatarGeneratorView.requestWriteExternalPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
  }

  @Override public void OnImageSaveNotSaved() {
    mAvatarGeneratorView.showSnackbar(R.string.error_avatar_not_saved);
  }

  @Override public void OnImageSaveSaved(String identifier, String url) {
    mLastSavedAvatarIdentifier = identifier;
    mLastSavedAvatarUrl = url;

    mAvatarGeneratorView.showGalleryActionSnackbar(R.string.message_avatar_saved);
  }

  @Override public void onConnectivityNoInternet() {
    mAvatarGeneratorView.showSnackbar(R.string.error_no_internet);
  }

  @Override public void onConnectivityNoNetwork() {
    mAvatarGeneratorView.showSnackbar(R.string.error_no_network);
  }

  @Override public void onConnectivityAvailable() {
    mAvatarGeneratorView.generateAvatar();
  }
}
