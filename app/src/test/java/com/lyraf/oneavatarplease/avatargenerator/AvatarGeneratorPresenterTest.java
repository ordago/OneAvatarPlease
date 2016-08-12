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
  @Mock private AvatarGeneratorContract.View mView;
  @Mock private ImageInteractor mImageInteractor;
  @Mock private NetworkInteractor mNetworkInteractor;
  private AvatarGeneratorContract.Presenter mPresenter;

  @Before public void setupAvatarGeneratorPresenter() {
    initMocks(this);

    mPresenter = new AvatarGeneratorPresenter(mImageInteractor, mNetworkInteractor);
    mPresenter.setView(mView);
  }

  @Test public void emptyIdentifier() {
    mPresenter.validateIdentifier("");

    verify(mView).showAvatarIdentifierError();
  }

  @Test public void sameIdentifier() {
    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).showSnackbar(R.string.message_avatar_generating);
    verify(mView).hideAvatarIdentifierError();
    verify(mView).loadAvatar();

    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).showSnackbar(R.string.error_avatar_already_generated);
  }

  @Test public void loadAvatar() {
    mPresenter.validateIdentifier(mTestIdentifier);

    verify(mView).hideSave();

    verify(mView).showSnackbar(R.string.message_avatar_generating);
    verify(mView).hideAvatarIdentifierError();
    verify(mView).loadAvatar();
  }

  @Test public void saveAvatar() {
    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveSaved(mTestIdentifier, mTestUrl);

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved);

    mPresenter.openGallery();

    verify(mView).showGallery(mTestUri);
  }

  @Test public void saveAvatarNoPermission() {
    mPresenter.setGeneratedAvatar(mTestAvatar);

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
    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveNotSaved();

    verify(mView).showSnackbar(R.string.error_avatar_not_saved);
  }

  @Test public void saveAvatarAlreadySaved() {
    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageInteractor).saveImage(eq(mTestAvatar), eq(mTestIdentifier),
        mOnImageSavedCaptor.capture());
    mOnImageSavedCaptor.getValue().OnImageSaveSaved(mTestIdentifier, mTestUrl);

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mView).showGalleryActionSnackbar(R.string.error_avatar_already_saved);
  }

  @Test public void connectivityAvailable() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityAvailable();

    verify(mView).generateAvatar();
  }

  @Test public void connectivityNoInternet() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityNoInternet();

    verify(mView).showSnackbar(R.string.error_no_internet);

    verify(mView).showSnackbar(Matchers.eq(R.string.error_no_internet));
  }

  @Test public void connectivityNoNetwork() {
    mPresenter.checkConnectivity();

    verify(mNetworkInteractor).isConnectivityAvailable(mOnConnectivityCheckedCaptor.capture());
    mOnConnectivityCheckedCaptor.getValue().onConnectivityNoNetwork();

    verify(mView).showSnackbar(R.string.error_no_network);

    verify(mView).showSnackbar(Matchers.eq(R.string.error_no_network));
  }
}
