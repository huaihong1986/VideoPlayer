package com.cvn.cmsgd.fragment;

import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.adapter.ProgramListAdapter;
import com.cvn.cmsgd.base.BaseHttpRequestFragment;
import com.cvn.cmsgd.model.ProgramData;
import com.cvn.cmsgd.net.OkHttpClientManager;
import com.cvn.cmsgd.storage.OfflineDataCache;
import com.cvn.cmsgd.util.Utils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


public class ProgramListFragment extends BaseHttpRequestFragment {

    private static final String TAG = "ProgramListFragment";

    public static final String KEY_CATEGORY_URL = "CATEGORY_URL";
    private String mProgramUrl;

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private ProgramListAdapter mAdapter;
    private ImageView mEmptyTip;
    private ProgramData mData;
    private String mCacheData;

    public static ProgramListFragment newInstance(String categoryUrl) {
        ProgramListFragment fragment = new ProgramListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY_URL, categoryUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public ProgramListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program_list, container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mEmptyTip = (ImageView) rootView.findViewById(R.id.empty);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

        mAdapter = new ProgramListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mProgramUrl = getArguments().getString(KEY_CATEGORY_URL);

//        fillData("", true);
        refresh();

        getActivity().invalidateOptionsMenu();
    }

    private void refresh() {
        if (getActivity() == null || TextUtils.isEmpty(mProgramUrl)) {
            return;
        }
        mSwipeRefreshWidget.setRefreshing(true);
        OkHttpClientManager.getInstance().asyncGet(mProgramUrl, this);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String data = response.body().string();

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshWidget.setRefreshing(false);
                    boolean isOk = fillData(data, false);

                    if (!isOk && getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.loading_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean fillData(String data, boolean fromCache) {
        boolean isOk = false;

        if (!TextUtils.isEmpty(data)) {
            Log.d(TAG, "json fromCache[" + fromCache + "]=" + data);

            try {
                mData = Utils.fromJson("{\"data\":" + data + "}", ProgramData.class);
                if (mData != null && mData.getData() != null) {
                    mAdapter.changeData((ArrayList<ProgramData.DataEntity>) mData.getData());
                    isOk = true;
                    if (getActivity() != null && !fromCache) {
//                        OfflineDataCache.saveProgramList(getActivity(), data);
                    }
                    return isOk;
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        if (fromCache) {
            try {
                mCacheData =  OfflineDataCache.getProgramList(getActivity());
                mData = Utils.fromJson("{\"data\":" +mCacheData + "}", ProgramData.class);
                if (mData != null && mData.getData() != null) {
                    mAdapter.changeData((ArrayList<ProgramData.DataEntity>) mData.getData());
                    isOk = true;
                    return isOk;
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return isOk;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        super.onFailure(call, e);
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshWidget.setRefreshing(false);
                }
            });
        }
    }

}
