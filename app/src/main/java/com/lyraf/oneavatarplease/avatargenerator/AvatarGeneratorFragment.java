package com.lyraf.oneavatarplease.avatargenerator;

import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.lyraf.oneavatarplease.App;
import com.lyraf.oneavatarplease.R;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

public class AvatarGeneratorFragment extends Fragment implements AvatarGeneratorContract.View {
  @Inject AvatarGeneratorContract.Presenter mPresenter;
  @BindView(R.id.avatar_generator_root) LinearLayout root;
  @BindView(R.id.button_avatar_save) Button saveAvatarButton;
  @BindView(R.id.image_avatar) ImageView avatarImage;
  @BindView(R.id.text_avatar_identifier) EditText avatarIdentifier;
  private Unbinder mUnbinder;

  public AvatarGeneratorFragment() {
  }

  @OnClick(R.id.button_avatar_generate) void generateAvatar() {
    Picasso.with(getActivity())
        .load(String.format(getResources().getString(R.string.url_avatar),
            avatarIdentifier.getText().toString()))
        .placeholder(R.drawable.ic_avatar_placeholder)
        .into(avatarImage);

    saveAvatarButton.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.button_avatar_save) void saveAvatar() {
    mPresenter.saveAvatar(((BitmapDrawable) avatarImage.getDrawable()).getBitmap(),
        avatarIdentifier.getText().toString());
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.getAvatarGeneratorComponent(getActivity()).inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_avatar_generator, container, false);

    mUnbinder = ButterKnife.bind(this, view);
    mPresenter.setView(this);

    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
    mPresenter.setView(null);
  }

  @Override public void showSnackbar(int messageResId) {
    Snackbar.make(root, messageResId, Snackbar.LENGTH_SHORT).show();
  }

  @Override public void requestWriteExternalPermission(String permission, int requestCode) {
    requestPermissions(new String[] { permission }, requestCode);
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    for (int result : grantResults) {
      if (result == PackageManager.PERMISSION_GRANTED) {
        mPresenter.saveAvatar(((BitmapDrawable) avatarImage.getDrawable()).getBitmap(),
            avatarIdentifier.getText().toString());
      } else {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      }
    }
  }
}
