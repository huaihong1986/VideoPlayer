package com.cvn.cmsgd.net;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;


public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mCache;
    private static BitmapCache sBitmapCache = null;

    public static BitmapCache getInstance() {
        if (sBitmapCache == null) {
            sBitmapCache = new BitmapCache();
        }

        return sBitmapCache;
    }

    private BitmapCache() {
        mCache = new LruCache<String, Bitmap>(getDefaultLruCacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
