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

import android.graphics.Bitmap;
import android.net.Uri;

public interface AvatarGeneratorContract {
  interface View {
    void showSnackbar(int messageResId);

    void showGalleryActionSnackbar(int messageResId);

    void requestWriteExternalPermission(String permission, int requestCode);

    void generateAvatar();

    void loadAvatar();

    void showAvatar(Bitmap avatar);

    void showProgress();

    void hideProgress();

    void showSave();

    void hideSave();

    void showAvatarIdentifierError();

    void hideAvatarIdentifierError();

    void showGallery(Uri imageUri);
  }

  interface Presenter {
    void setView(View view);

    void saveAvatar(String identifier);

    void generatedAvatar(Bitmap generatedAvatar, String identifier);

    void restoreGeneratedAvatar();

    void validateIdentifier(String identifier);

    void openGallery();

    void checkConnectivity();
  }
}
