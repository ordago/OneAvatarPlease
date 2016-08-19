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

package com.lyraf.oneavatarplease.interactors;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import javax.inject.Inject;

public class NetworkInteractor {
  private final Application mApplication;

  @Inject public NetworkInteractor(Application application) {
    mApplication = application;
  }

  public void isConnectivityAvailable(OnConnectivityCheckedCallback callback) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager.getActiveNetworkInfo() != null) {
      if (!connectivityManager.getActiveNetworkInfo().isConnected()) {
        callback.onConnectivityNoInternet();
      } else {
        callback.onConnectivityAvailable();
      }
    } else {
      callback.onConnectivityNoNetwork();
    }
  }

  public interface OnConnectivityCheckedCallback {
    void onConnectivityNoInternet();

    void onConnectivityNoNetwork();

    void onConnectivityAvailable();
  }
}
