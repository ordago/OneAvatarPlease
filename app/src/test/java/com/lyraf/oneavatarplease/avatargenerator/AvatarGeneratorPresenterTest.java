package com.lyraf.oneavatarplease.avatargenerator;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import com.lyraf.oneavatarplease.BuildConfig;
import com.lyraf.oneavatarplease.R;
import com.lyraf.oneavatarplease.interactors.ConnectivityChecker;
import com.lyraf.oneavatarplease.interactors.ImageSaver;
import com.lyraf.oneavatarplease.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class AvatarGeneratorPresenterTest {
  @Mock private AvatarGeneratorContract.View mView;
  @Mock private ImageSaver mImageSaver;
  @Mock private ConnectivityChecker mConnectivityChecker;

  private AvatarGeneratorContract.Presenter mPresenter;

  private String mTestIdentifier = "Identifier";
  private String mEmpty = "";

  private Bitmap mTestAvatar = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

  @Before public void setupAvatarGeneratorPresenter() {
    initMocks(this);

    mPresenter = new AvatarGeneratorPresenter(mImageSaver, mConnectivityChecker);
    mPresenter.setView(mView);
  }

  @Test public void emptyIdentifier() {
    mPresenter.validateIdentifier(mEmpty);

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
    when(mImageSaver.saveImage(mTestAvatar, mTestIdentifier)).thenReturn("URL");

    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageSaver).saveImage(Matchers.eq(mTestAvatar), Matchers.eq(mTestIdentifier));

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved,
        R.string.action_avatar_show);
  }

  @Test public void saveAvatarNoPermission() {
    when(mImageSaver.saveImage(mTestAvatar, mTestIdentifier)).thenReturn(mEmpty);

    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageSaver).saveImage(Matchers.eq(mTestAvatar), Matchers.eq(mTestIdentifier));

    verify(mView).requestWriteExternalPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE);

    verify(mView).requestWriteExternalPermission(
        Matchers.eq(Manifest.permission.WRITE_EXTERNAL_STORAGE),
        Matchers.eq(Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE));
  }

  @Test public void saveAvatarNotSaved() {
    when(mImageSaver.saveImage(mTestAvatar, mTestIdentifier)).thenReturn(null);

    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageSaver).saveImage(Matchers.eq(mTestAvatar), Matchers.eq(mTestIdentifier));

    verify(mView).showSnackbar(R.string.error_avatar_not_saved);
  }

  @Test public void saveAvatarNotGenerated() {
    mPresenter.setGeneratedAvatar(null);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mView).showSnackbar(R.string.error_avatar_not_generated);
  }

  @Test public void saveAvatarAlreadySaved() {
    when(mImageSaver.saveImage(mTestAvatar, mTestIdentifier)).thenReturn("URL");

    mPresenter.setGeneratedAvatar(mTestAvatar);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mImageSaver).saveImage(Matchers.eq(mTestAvatar), Matchers.eq(mTestIdentifier));

    verify(mView).showGalleryActionSnackbar(R.string.message_avatar_saved,
        R.string.action_avatar_show);

    mPresenter.saveAvatar(mTestIdentifier);

    verify(mView).showGalleryActionSnackbar(R.string.error_avatar_already_saved,
        R.string.action_avatar_show);
  }

  @Test public void connectivityNoInternet() {
    when(mConnectivityChecker.isConnectivityAvailable()).thenReturn(
        Constants.RESULT_CONNECTIVITY_NO_INTERNET);

    mPresenter.checkConnectivity();

    verify(mView).showSnackbar(R.string.error_no_internet);

    verify(mView).showSnackbar(Matchers.eq(R.string.error_no_internet));
  }

  @Test public void connectivityNoNetwork() {
    when(mConnectivityChecker.isConnectivityAvailable()).thenReturn(
        Constants.RESULT_CONNECTIVITY_NO_NETWORK);

    mPresenter.checkConnectivity();

    verify(mView).showSnackbar(R.string.error_no_network);

    verify(mView).showSnackbar(Matchers.eq(R.string.error_no_network));
  }

  @Test public void openGallery() {
    mPresenter.openGallery();

    verify(mView).showGallery(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    verify(mView).showGallery(
        Matchers.eq(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
  }
}
