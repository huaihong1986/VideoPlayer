package com.cvn.cmsgd.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.net.Net;
import com.cvn.cmsgd.net.VolleyPostRequest;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BaseHttpRequestFragment extends Fragment implements Callback {

    protected ProgressDialog mDialog;
    public BaseHttpRequestFragment() {
    }

    protected void createProgressDialog(VolleyPostRequest request) {
        dismissProgressDialog();
        mDialog = createProgressDialog(getActivity(), request);
    }

    protected void showProgressDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public static ProgressDialog createProgressDialog(Context context, final VolleyPostRequest request) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("");
        progressDialog.setMessage(""+R.string.loading_data);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (request != null) {
                    request.cancel();
                }
            }
        });

        return progressDialog;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Net.isNetworkConnected(getActivity())) {
                        Toast.makeText(getActivity(),R.string.loading_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
