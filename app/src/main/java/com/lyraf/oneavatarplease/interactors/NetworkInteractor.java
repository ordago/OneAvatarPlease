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
