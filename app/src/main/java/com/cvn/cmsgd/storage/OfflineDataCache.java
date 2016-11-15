package com.cvn.cmsgd.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cvn.cmsgd.model.User;

public class OfflineDataCache {

    private static final String KEY_APP_FIRST_STARTUP = "APP_FIRST_STARTUP";
    private static final String KEY_NET_DOWNLOAD_ONLY_WIFI = "DOWNLOAD_ONLY_WIFI";

    private static final String KEY_USER_NAME = "user";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_SHARED_PREFERENCES = "OFFLINE_DATA";
    private static final String KEY_RECOMMEND_LIST = "RECOMMEND_LIST";
    private static final String KEY_PROGRAM_LIST = "PROGRAM_LIST";

    public static void init(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean first = preferences.getBoolean(KEY_APP_FIRST_STARTUP, true);
        if (first) {
            Editor et = preferences.edit();
            et.putBoolean(KEY_NET_DOWNLOAD_ONLY_WIFI, true);
            et.putBoolean(KEY_APP_FIRST_STARTUP, false);
            et.apply();
        }
    }

    public static void saveUser(Context context, String userName, String password) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Editor et = preferences.edit();
        et.putString(KEY_USER_NAME, userName);
        et.putString(KEY_PASSWORD, password);
        et.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String userName = preferences.getString(KEY_USER_NAME, "");
        String password = preferences.getString(KEY_PASSWORD, "");
        User user = new User();
        user.userName = userName;
        user.password = password;
        return user;
    }

    public static void saveNetSetting(Context context, boolean onlyWifi) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Editor et = preferences.edit();
        et.putBoolean(KEY_NET_DOWNLOAD_ONLY_WIFI, onlyWifi);
        et.apply();
    }

    public static boolean getNetSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_NET_DOWNLOAD_ONLY_WIFI, true);
    }

    public static void saveRecommendList(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Editor et = preferences.edit();
        et.putString(KEY_RECOMMEND_LIST, json);
        et.apply();
    }

    public static String getRecommendList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(KEY_RECOMMEND_LIST, "");
    }

    public static void saveProgramList(Context context, String json) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Editor et = preferences.edit();
        et.putString(KEY_PROGRAM_LIST, json);
        et.apply();
    }

    public static String getProgramList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(KEY_PROGRAM_LIST, "");
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Editor et = preferences.edit();
        et.clear();
        et.apply();
    }

}
