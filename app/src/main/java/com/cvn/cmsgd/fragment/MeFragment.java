package com.cvn.cmsgd.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.model.LoginResponseData;
import com.cvn.cmsgd.model.User;
import com.cvn.cmsgd.storage.OfflineDataCache;
import com.cvn.cmsgd.util.LoginManager;
import com.cvn.cmsgd.util.Utils;
import com.cvn.cmsgd.widget.LoginDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MeFragment extends Fragment implements Callback {

    private static final String TAG = "MeFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTip;

    private LinearLayout mLogoutContainer;
    private TextView mLoginUser;
    private Button mLogout;

    private LinearLayout mLoginContainer;
    private Button mRegister;
    private Button mLogin;
    private LoginDialog mLoginDialog;
    private int mRequestType = LoginManager.TYPE_REGISTER;
    private boolean mIsLoading = false;


    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mEmptyTip = (TextView) rootView.findViewById(R.id.empty);

        mLogoutContainer = (LinearLayout) rootView.findViewById(R.id.logout_container);
        mLoginUser = (TextView) rootView.findViewById(R.id.login_user);
        mLogout = (Button) rootView.findViewById(R.id.logout);

        mLoginContainer = (LinearLayout) rootView.findViewById(R.id.login_container);
        mRegister = (Button) rootView.findViewById(R.id.register);
        mLogin = (Button) rootView.findViewById(R.id.login);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        };
        mSwipeRefreshWidget.setOnRefreshListener(onRefreshListener);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineDataCache.saveUser(getActivity(), "", "");
                refresh();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestType = LoginManager.TYPE_REGISTER;
                showLoginDialog();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestType = LoginManager.TYPE_LOGIN;
                showLoginDialog();
            }
        });

        refresh();
        getActivity().invalidateOptionsMenu();
    }

    public void refresh() {
        if (mSwipeRefreshWidget != null) {
            mSwipeRefreshWidget.setRefreshing(false);
        }

        if (getActivity() != null && mLoginUser != null) {
            User user = OfflineDataCache.getUser(getActivity());
            if (user != null && !TextUtils.isEmpty(user.userName)) {
                mLoginContainer.setVisibility(View.GONE);
                mLogoutContainer.setVisibility(View.VISIBLE);
                mLoginUser.setText(getString(R.string.hello_login_user, user.userName));
            } else {
                mLoginContainer.setVisibility(View.VISIBLE);
                mLogoutContainer.setVisibility(View.GONE);
            }
        }
    }

    private void showLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
            mLoginDialog = null;
        }
        mLoginDialog = new LoginDialog(getActivity());
        mLoginDialog.changeState(mRequestType);
        mLoginDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLoading) {
                    mIsLoading = true;
                    LoginManager.login(getActivity(),
                            mLoginDialog.getInputUser(), mLoginDialog.getInputPassword(), mLoginDialog.getInputConfirmPassword(),
                            mRequestType, MeFragment.this);
                }
            }
        });
        mLoginDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIsLoading = false;
                    showToast();
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String data = response.body().string();
        Log.d(TAG, "data =" + data);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(data)) {
                        LoginResponseData msg = Utils.fromJson(data, LoginResponseData.class);

                        if (msg != null) {
                            Toast.makeText(getActivity(), msg.getMsgCondition(), Toast.LENGTH_SHORT).show();
                            if (msg.getMsg().equals("1")) {
                                switch (mRequestType) {
                                    case LoginManager.TYPE_REGISTER:
                                        mRequestType = LoginManager.TYPE_LOGIN;
                                        mLoginDialog.changeState(mRequestType);
                                        break;

                                    case LoginManager.TYPE_LOGIN:
                                        OfflineDataCache.saveUser(getActivity(), mLoginDialog.getInputUser(), mLoginDialog.getInputPassword());
                                        mLoginDialog.dismiss();
                                        refresh();
                                        break;

                                    case LoginManager.TYPE_FORGET_PASSWORD:
                                        break;
                                }
                            }
                        }
                    } else {
                        showToast();
                    }
                    mIsLoading = false;
                }
            });
        }
    }

    private void showToast() {
        switch (mRequestType) {
            case LoginManager.TYPE_REGISTER:
                Toast.makeText(getActivity(), R.string.register_error, Toast.LENGTH_SHORT).show();
                break;

            case LoginManager.TYPE_LOGIN:
                Toast.makeText(getActivity(),R.string.login_error, Toast.LENGTH_SHORT).show();
                break;

            case LoginManager.TYPE_FORGET_PASSWORD:
                break;
        }
    }

}
