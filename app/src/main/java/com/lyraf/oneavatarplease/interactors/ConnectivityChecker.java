package com.lyraf.oneavatarplease.interactors;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import com.lyraf.oneavatarplease.utils.Constants;

public class ConnectivityChecker {
  Application mApplication;

  public ConnectivityChecker(Application application) {
    mApplication = application;
  }

  public int isConnectivityAvailable() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager.getActiveNetworkInfo() != null) {
      if (!connectivityManager.getActiveNetworkInfo().isConnected()) {
        return Constants.RESULT_CONNECTIVITY_NO_INTERNET;
      }
    } else {
      return Constants.RESULT_CONNECTIVITY_NO_NETWORK;
    }
    return Constants.RESULT_CONNECTIVITY_AVAILABLE;
  }
}
