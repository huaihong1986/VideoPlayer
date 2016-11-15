package com.cvn.cmsgd.widget;


import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.util.LoginManager;


public class LoginDialog {

    private android.app.AlertDialog mDialog;
    private TextView mTitle;
    private EditText mInputUser;
    private EditText mInputPassword;
    private EditText mInputConfirmPassword;
    private LinearLayout mButtonsContainer;

    public LoginDialog(Context context) {
        mDialog = new android.app.AlertDialog.Builder(context).create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.login_dialog);
        mTitle = (TextView) window.findViewById(R.id.title);
        mInputUser = (EditText) window.findViewById(R.id.input_user);
        mInputPassword = (EditText) window.findViewById(R.id.input_password);
        mInputConfirmPassword = (EditText) window.findViewById(R.id.input_confirm_password);
        mButtonsContainer = (LinearLayout) window.findViewById(R.id.btn_container);
    }

    public String getInputUser() {
        return mInputUser.getText().toString().trim();
    }

    public String getInputPassword() {
        return mInputPassword.getText().toString().trim();
    }

    public String getInputConfirmPassword() {
        return mInputConfirmPassword.getText().toString().trim();
    }

    public void changeState(int type) {
        TextView button = (TextView) mButtonsContainer.findViewById(R.id.positive_button);
        if (type == LoginManager.TYPE_LOGIN) {
            mTitle.setText("登陆");
            mInputConfirmPassword.setVisibility(View.GONE);
            button.setText("登陆");
        } else if (type == LoginManager.TYPE_REGISTER) {
            mTitle.setText("注册");
            mInputConfirmPassword.setVisibility(View.VISIBLE);
            button.setText("注册");
        }
    }

    public void setPositiveButton(final View.OnClickListener listener) {
        TextView button = (TextView) mButtonsContainer.findViewById(R.id.positive_button);
        button.setOnClickListener(listener);
    }

    public void setNegativeButton(final View.OnClickListener listener) {
        TextView button = (TextView) mButtonsContainer.findViewById(R.id.negative_button);
        button.setOnClickListener(listener);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
