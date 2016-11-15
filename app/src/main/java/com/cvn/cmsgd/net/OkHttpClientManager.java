package com.cvn.cmsgd.net;

import com.cvn.cmsgd.CMSGDApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpClientManager {

    private static final String TAG = "OkHttpClientManager";

    private static final String CACHE_DIR = "Cache";

    private static OkHttpClientManager sOkHttpClientManager;

    private OkHttpClient mClient;
    private String mDownloadDir;

    private OkHttpClientManager() {
        OkHttpClient client = new OkHttpClient();
        mClient = client.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        mDownloadDir = CMSGDApplication.getInstance().getExternalCacheDir() + File.separator + CACHE_DIR + File.separator;
    }

    public synchronized static OkHttpClientManager getInstance() {
        if (sOkHttpClientManager == null) {
            sOkHttpClientManager = new OkHttpClientManager();
        }
        return sOkHttpClientManager;
    }

    public Call asyncGet(String url, final Callback responseCallback) {
        Request request = new Request.Builder().url(url).build();
        Call call = mClient.newCall(request);
        call.enqueue(responseCallback);

        return call;
    }

    public Call asyncPost(String url, RequestBody body, final Callback responseCallback) {
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = mClient.newCall(request);
        call.enqueue(responseCallback);

        return call;
    }

    public String getDownloadDir() {
        return mDownloadDir;
    }

}
