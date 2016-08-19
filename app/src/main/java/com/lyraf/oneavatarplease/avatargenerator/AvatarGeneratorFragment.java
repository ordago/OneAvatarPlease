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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.lyraf.oneavatarplease.App;
import com.lyraf.oneavatarplease.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class AvatarGeneratorFragment extends Fragment implements AvatarGeneratorContract.View {
  @Inject AvatarGeneratorPresenter presenter;
  @BindView(R.id.avatar_generator_root) LinearLayout root;
  @BindView(R.id.image_avatar) ImageView imageAvatar;
  @BindView(R.id.progress_avatar) CircularProgressView progressAvatar;
  @BindView(R.id.floating_text_avatar_identifier) TextInputLayout inputAvatarIdentifier;
  @BindView(R.id.text_avatar_identifier) EditText textAvatarIdentifier;
  @BindView(R.id.button_avatar_save) Button buttonSaveAvatar;

  private Unbinder mUnbinder;

  public AvatarGeneratorFragment() {
  }

  @OnClick(R.id.button_avatar_generate) void generateAvatarClick() {
    presenter.checkConnectivity();
  }

  @OnClick(R.id.button_avatar_save) void saveAvatarClick() {
    presenter.saveAvatar(textAvatarIdentifier.getText().toString());
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);

    App.getAppComponent(getActivity()).inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_avatar_generator, container, false);

    mUnbinder = ButterKnife.bind(this, view);
    presenter.setView(this);

    if (savedInstanceState != null) {
      presenter.restoreGeneratedAvatar();
    }

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
    presenter.setView(null);
  }

  @Override public void showSnackbar(int messageResId) {
    Snackbar.make(root, messageResId, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void showGalleryActionSnackbar(int messageResId) {
    Snackbar.make(root, messageResId, Snackbar.LENGTH_LONG)
        .setAction(R.string.action_avatar_show, view -> {
          presenter.openGallery();
        })
        .show();
  }

  @Override public void requestWriteExternalPermission(String permission, int requestCode) {
    requestPermissions(new String[] { permission }, requestCode);
  }

  @Override public void generateAvatar() {
    presenter.validateIdentifier(textAvatarIdentifier.getText().toString());
  }

  @Override public void loadAvatar() {
    Picasso.with(getActivity())
        .load(String.format(getResources().getString(R.string.url_avatar),
            Uri.encode(textAvatarIdentifier.getText().toString())))
        .fit()
        .centerCrop()
        .into(imageAvatar, new Callback() {
          @Override public void onSuccess() {
            presenter.generatedAvatar(((BitmapDrawable) imageAvatar.getDrawable()).getBitmap(),
                textAvatarIdentifier.getText().toString());
          }

          @Override public void onError() {
            imageAvatar.setImageResource(R.drawable.ic_avatar_empty);

            presenter.checkConnectivity();
          }
        });
  }

  @Override public void showAvatar(Bitmap avatar) {
    imageAvatar.setImageBitmap(avatar);
  }

  @Override public void showProgress() {
    progressAvatar.setVisibility(View.VISIBLE);
  }

  @Override public void hideProgress() {
    progressAvatar.setVisibility(View.GONE);
  }

  @Override public void showSave() {
    buttonSaveAvatar.setVisibility(View.VISIBLE);
  }

  @Override public void hideSave() {
    buttonSaveAvatar.setVisibility(View.GONE);
  }

  @Override public void showAvatarIdentifierError() {
    inputAvatarIdentifier.setError(getContext().getString(R.string.error_no_identifier));
    inputAvatarIdentifier.setErrorEnabled(true);
  }

  @Override public void hideAvatarIdentifierError() {
    inputAvatarIdentifier.setError(null);
    inputAvatarIdentifier.setErrorEnabled(false);
  }

  @Override public void showGallery(Uri imageUri) {
    Intent intent = new Intent(Intent.ACTION_VIEW, imageUri);
    startActivity(intent);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    for (int result : grantResults) {
      if (result == PackageManager.PERMISSION_GRANTED) {
        presenter.saveAvatar(textAvatarIdentifier.getText().toString());
      } else {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      }
    }
  }
}
