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
import android.widget.TextView;

import com.cvn.cmsgd.R;
import com.cvn.cmsgd.adapter.EPGListAdapter;
import com.cvn.cmsgd.base.BaseHttpRequestFragment;
import com.cvn.cmsgd.model.EPGData;
import com.cvn.cmsgd.net.OkHttpClientManager;
import com.cvn.cmsgd.net.Urls;
import com.cvn.cmsgd.util.Utils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;


public class EPGListFragment extends BaseHttpRequestFragment {

    private static final String TAG = "EPGListFragment";

    public static final String KEY_TITLE = "title";
    public static final String KEY_DAY_OFFSET = "DAY_OFFSET";
    private String mTitle;
    private int mDayOffset;

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private EPGListAdapter mAdapter;
    private TextView mEmptyTip;


    public static EPGListFragment newInstance(String title, int dayOffset) {
        EPGListFragment fragment = new EPGListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_DAY_OFFSET, dayOffset);
        fragment.setArguments(args);
        return fragment;
    }

    public EPGListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_epg_list, container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mEmptyTip = (TextView) rootView.findViewById(R.id.empty);

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

        mAdapter = new EPGListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mTitle = getArguments().getString(KEY_TITLE);
        mDayOffset = getArguments().getInt(KEY_DAY_OFFSET, 0);

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (getActivity() == null) {
            return;
        }
        mSwipeRefreshWidget.setRefreshing(false);
        OkHttpClientManager.getInstance().asyncGet(Urls.GET_EPG_LIST + "c=" + mTitle + "&d=" + mDayOffset, this);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String data = "{\"data\":" + response.body().string() + "}";
        Log.d(TAG, "epg data ====" + data);

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshWidget.setRefreshing(false);
                    boolean isOk = fillData(data);

                    if (getActivity() != null) {
                        mEmptyTip.setVisibility(isOk ? View.GONE : View.VISIBLE);
                    }
                }
            });
        }
    }

    private boolean fillData(String data) {
        boolean isOk = false;

        if (!TextUtils.isEmpty(data)) {
            Log.d(TAG, "json =" + data);

            try {
                EPGData epg = Utils.fromJson(data, EPGData.class);
                if (epg != null && epg.getData() != null && epg.getData().size() > 0) {
                    mAdapter.changeData((ArrayList<EPGData.DataEntity>) epg.getData());
                    isOk = true;
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
