/*
 * MIT License
 *
 * Copyright (c) 2016 Felipe Lyra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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

  @Override public void generatedAvatar(Bitmap generatedAvatar, String identifier) {
    if (generatedAvatar != null) {
      mAvatarGeneratorView.hideProgress();
      mAvatarGeneratorView.showSave();
      mGeneratedAvatar = generatedAvatar;
      mLastIdentifier = identifier;
    }
  }

  @Override public void restoreGeneratedAvatar() {
    if (mGeneratedAvatar != null) {
      mAvatarGeneratorView.showSave();
      mAvatarGeneratorView.showAvatar(mGeneratedAvatar);
    }
  }

  @Override public void validateIdentifier(String identifier) {
    if (TextUtils.isEmpty(identifier)) {
      mAvatarGeneratorView.showAvatarIdentifierError();
    } else if (TextUtils.equals(identifier, mLastIdentifier)) {
      mAvatarGeneratorView.showSnackbar(R.string.error_avatar_already_generated);
    } else {
      mAvatarGeneratorView.showProgress();
      mAvatarGeneratorView.hideSave();

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

  @Override public void onConnectivityNoInternet() {
    mAvatarGeneratorView.hideProgress();
    mAvatarGeneratorView.showSnackbar(R.string.error_no_internet);
  }

  @Override public void onConnectivityNoNetwork() {
    mAvatarGeneratorView.hideProgress();
    mAvatarGeneratorView.showSnackbar(R.string.error_no_network);
  }

  @Override public void onConnectivityAvailable() {
    mAvatarGeneratorView.hideProgress();
    mAvatarGeneratorView.generateAvatar();
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
}
