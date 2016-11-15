package com.cvn.cmsgd.util;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.net.OkHttpClientManager;
import com.cvn.cmsgd.net.Urls;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginManager {

    public static final int TYPE_REGISTER = 8001;
    public static final int TYPE_LOGIN = 8002;
    public static final int TYPE_FORGET_PASSWORD = 8003;

    public static void login(Context context, String userName, String password, String confirmPassword, int type, final Callback responseCallback) {
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(context, R.string.user_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.password_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (type == TYPE_REGISTER) {
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(context, R.string.password_error, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(context, R.string.passwords_diff, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String typeString = null;
        switch (type) {
            case TYPE_REGISTER:
                typeString = "add";
                break;

            case TYPE_LOGIN:
                typeString = "login";
                break;

            case TYPE_FORGET_PASSWORD:
                typeString = "update";
                break;
        }
        if (TextUtils.isEmpty(typeString)) {
            Toast.makeText(context, R.string.loginstyle_error, Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody formBody = new FormBody.Builder()
                .add("username", userName)
                .add("password", password)
                .add("type", typeString)
                .add("addFrom", "app")
                .build();
        OkHttpClientManager.getInstance().asyncPost(Urls.LOGIN, formBody, responseCallback);
    }

}
