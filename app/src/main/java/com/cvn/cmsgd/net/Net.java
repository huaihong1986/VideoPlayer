package com.cvn.cmsgd.net;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.cvn.cmsgd.CMSGDApplication;
import com.cvn.cmsgd.R;

import org.json.JSONObject;

import java.util.Map;


public class Net {

    public static final int VOLLEY_REQUEST_TIMEOUT = 15000;

    private static Net sInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static boolean sDebug = false;

    private Net() {
        mRequestQueue = Volley.newRequestQueue(CMSGDApplication.getInstance());
        mImageLoader = new ImageLoader(mRequestQueue, BitmapCache.getInstance());
    }

    public static void init(boolean isDebug) {
        sDebug = isDebug;
    }

    public static Net getInstance() {
        if (sInstance == null) {
            sInstance = new Net();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static void loadImage(String url, ImageView view) {
        ImageListener listener = ImageLoader.getImageListener(view, R.mipmap.default_thumb, R.mipmap.default_thumb);
        Net.getInstance().getImageLoader().get(url, listener);
    }

    public static void loadImage(String url, ImageView view, int resId) {
        ImageListener listener = ImageLoader.getImageListener(view, resId, resId);
        Net.getInstance().getImageLoader().get(url, listener);
    }

    public static void loadImage(String url, ImageListener listener) {
        Net.getInstance().getImageLoader().get(url, listener);
    }

    public static void loadImage(String url, NetworkImageView view) {
        view.setTag(url);
        view.setImageUrl(url, Net.getInstance().getImageLoader());
    }

    public static ImageListener getRoundedImageListener(final Resources resources, final ImageView view) {
        return new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bitmap = imageContainer.getBitmap();
                if (bitmap != null && resources != null) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
                    drawable.setCornerRadius(bitmap.getWidth() / 2);
                    drawable.setAntiAlias(true);
                    if (view != null) {
                        view.setImageDrawable(drawable);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        };
    }

    public static JsonObjectRequest getJSON(String url, final VolleyResponseListener listener, final int requestCode) {
        RequestQueue requestQueue = Net.getInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (sDebug) {
                            Log.d("getJSON", "response =" + response);
                        }

                        if (listener != null) {
                            listener.onDataSuccessResponse(response, requestCode);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (sDebug) {
                    Log.d("getJSON", "error = " + error.getMessage());
                }

                if (listener != null) {
                    listener.onDataErrorResponse(error, requestCode);
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (listener != null) {
            listener.onDataStartGetRequest(jsonObjectRequest);
        }

        requestQueue.add(jsonObjectRequest);

        return jsonObjectRequest;
    }

    public static VolleyPostRequest postJSON(String url, final VolleyResponseListener listener, final Map<String, String> params, final int requestCode) {
        RequestQueue requestQueue = Net.getInstance().getRequestQueue();
        final VolleyPostRequest request = new VolleyPostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        if (sDebug) {
                            Log.d("postJSON", "success =" + response);
                        }

                        if (listener != null) {
                            listener.onDataSuccessResponse(response, requestCode);
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (sDebug) {
                    Log.d("postJSON", "error = " + error.getMessage());
                }

                if (listener != null) {
                    listener.onDataErrorResponse(error, requestCode);
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (listener != null) {
            listener.onDataStartPostRequest(request);
        }

        requestQueue.add(request);

        return request;
    }

    public static VolleyPostRequest postJSON(String url, final Map<String, String> params) {
        RequestQueue requestQueue = Net.getInstance().getRequestQueue();
        final VolleyPostRequest request = new VolleyPostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        if (sDebug) {
                            Log.d("postJSON", "success =" + response);
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (sDebug) {
                    Log.d("postJSON", "error = " + error.getMessage());
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

        return request;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static final int NET_TYPE_WIFI = 0x01;
    public static final int NET_TYPE_CMWAP = 0x02;
    public static final int NET_TYPE_CMNET = 0x03;

    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NET_TYPE_CMNET;
                } else {
                    netType = NET_TYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NET_TYPE_WIFI;
        }
        return netType;
    }

    public static boolean isNetworkWIFI(Context context) {
        return getNetworkType(context) == NET_TYPE_WIFI;
    }

}
