package com.cvn.cmsgd.model;

import android.text.TextUtils;

public class User {

    public String userName;

    public String password;

    public static boolean isLogin(User user) {
        return user != null && !TextUtils.isEmpty(user.userName) && !TextUtils.isEmpty(user.password);
    }

}
