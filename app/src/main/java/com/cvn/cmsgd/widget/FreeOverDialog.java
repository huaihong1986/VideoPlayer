package com.cvn.cmsgd.widget;


import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cvn.cmsgd.R;

public class FreeOverDialog {

    private android.app.AlertDialog mDialog;
    private TextView mMsg;
    private TextView mButton;

    public FreeOverDialog(Context context) {
        mDialog = new android.app.AlertDialog.Builder(context).create();
        mDialog.show();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.free_over_dialog);
        mMsg = (TextView) window.findViewById(R.id.msg);
        mButton = (TextView) window.findViewById(R.id.positive_button);
    }

    public void setMessage(String msg) {
        String t = "对不起，免费时长" + msg + "秒！";
        mMsg.setText(t);
    }

    public void setPositiveButton(final View.OnClickListener listener) {
        mButton.setOnClickListener(listener);
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
