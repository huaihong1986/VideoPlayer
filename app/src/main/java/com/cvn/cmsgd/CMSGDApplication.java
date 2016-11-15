package com.cvn.cmsgd;


import android.app.Application;

import com.cvn.cmsgd.net.Net;
import com.cvn.cmsgd.storage.OfflineDataCache;


public class CMSGDApplication extends Application {
    private static CMSGDApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        OfflineDataCache.init(this);
        Net.init(true);
    }

    public static CMSGDApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
