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
import android.os.Build;
import com.lyraf.oneavatarplease.BuildConfig;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.interactors.ImageInteractor;
import com.lyraf.oneavatarplease.interactors.NetworkInteractor;
import com.lyraf.oneavatarplease.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class AvatarGeneratorPresenterTest {
  private final String mTestIdentifier = "Identifier";
  private final String mTestUrl = "Url";
  private final Uri mTestUri = Uri.parse(mTestUrl);
  private final Bitmap mTestAvatar = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
  @Captor ArgumentCaptor<ImageInteractor.OnImageSavedCallback> mOnImageSavedCaptor;
  @Captor ArgumentCaptor<NetworkInteractor.OnConnectivityCheckedCallback>
      mOnConnectivityCheckedCaptor;
  private AvatarGeneratorContract.Presenter mPresenter;
  @Mock private AvatarGeneratorContract.View mView;
  @Mock private ImageInteractor mImageInteractor;
  @Mock private NetworkInteractor mNetworkInteractor;

  @Before public void setupAvatarGeneratorPresenter() {
    initMocks(this);

    mPresenter = new AvatarGeneratorPresenter(mImageInteractor, mNetworkInteractor);
    mPresenter.setView(mView);
  }

  @Test public void emptyIdentifier() {
    mPresenter.validateIdentifier("");

    verify(mView).showAvatarIdentifierError();
  }

  @Test public void generateAvatar() {
    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).showProgress();
    verify(mView).hideSave();
    verify(mView).showSnackbar(R.string.message_avatar_generating);
    verify(mView).showSnackbar(eq(R.string.message_avatar_generating));
    verify(mView).hideAvatarIdentifierError();
    verify(mView).loadAvatar();
  }

  @Test public void generateAvatarAlreadyGenerated() {
    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).showProgress();
    verify(mView).hideSave();
    verify(mView).showSnackbar(R.string.message_avatar_generating);
    verify(mView).showSnackbar(eq(R.string.message_avatar_generating));
    verify(mView).hideAvatarIdentifierError();
    verify(mView).loadAvatar();

    mPresenter.generatedAvatar(mTestAvatar, mTestIdentifier);
    verify(mView).hideProgress();
    verify(mView).showSave();

    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).showSnackbar(R.string.error_avatar_already_generated);
    verify(mView).showSnackbar(eq(R.string.error_avatar_already_generated));
  }

  @Test public void saveAvatar() {
    mPresenter.generatedAvatar(mTestAvatar, mTestIdentifier);
    verify(mView).hideProgress();
    verify(mView).showSave();

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveSaved(mTestIdentifier, mTestUrl);

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved);
    verify(mView).showGalleryActionSnackbar(eq(R.string.message_avatar_saved));

    mPresenter.openGallery();

    verify(mView).showGallery(mTestUri);
    verify(mView).showGallery(eq(mTestUri));
  }

  @Test public void saveAvatarNoPermission() {
    mPresenter.generatedAvatar(mTestAvatar, mTestIdentifier);
    verify(mView).hideProgress();
    verify(mView).showSave();

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveNoPermission();

    verify(mView).requestWriteExternalPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);

    verify(mView).requestWriteExternalPermission(
        Matchers.eq(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        Matchers.eq(Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE));
  }

  @Test public void saveAvatarNotSaved() {
    mPresenter.generatedAvatar(mTestAvatar, mTestIdentifier);
    verify(mView).hideProgress();
    verify(mView).showSave();

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveNotSaved();

    verify(mView).showSnackbar(R.string.error_avatar_not_saved);
    verify(mView).showSnackbar(eq(R.string.error_avatar_not_saved));
  }

  @Test public void saveAvatarAlreadySaved() {
    mPresenter.generatedAvatar(mTestAvatar, mTestIdentifier);
    verify(mView).hideProgress();
    verify(mView).showSave();

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveSaved(mTestIdentifier, mTestUrl);

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved);
    verify(mView).showGalleryActionSnackbar(eq(R.string.message_avatar_saved));

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mView).showGalleryActionSnackbar(R.string.error_avatar_already_saved);
    verify(mView).showGalleryActionSnackbar(eq(R.string.error_avatar_already_saved));
  }

  @Test public void connectivityAvailable() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityAvailable();

    verify(mView).hideProgress();
    verify(mView).generateAvatar();
  }

  @Test public void connectivityNoInternet() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityNoInternet();

    verify(mView).hideProgress();
    verify(mView).showSnackbar(R.string.error_no_internet);
    verify(mView).showSnackbar(eq(R.string.error_no_internet));
  }

  @Test public void connectivityNoNetwork() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityNoNetwork();

    verify(mView).hideProgress();
    verify(mView).showSnackbar(R.string.error_no_network);
    verify(mView).showSnackbar(eq(R.string.error_no_network));
  }
}
