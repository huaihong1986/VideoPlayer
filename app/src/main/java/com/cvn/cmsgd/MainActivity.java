package com.cvn.cmsgd;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvn.cmsgd.fragment.MeFragment;
import com.cvn.cmsgd.fragment.ProgramListFragment;
import com.cvn.cmsgd.fragment.VideoFragment;
import com.cvn.cmsgd.model.CategoryData;
import com.cvn.cmsgd.net.OkHttpClientManager;
import com.cvn.cmsgd.net.Urls;
import com.cvn.cmsgd.util.Utils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Callback, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CategoryData mData;

    private static final int LIVE_INDEX = 1;
    private static final int VIDEO_INDEX = 2;
    private static final int INTERACTION_INDEX = 3;
    private static final int ME_INDEX = 4;
    private int mSelectedIndex = LIVE_INDEX;

    private TextView mLiveBtn;
    private TextView mVideoBtn;
    private TextView mInteractionBtn;
    private TextView mMeBtn;

    private LinearLayout mFragmentContainer;
    private VideoFragment mVideoFragment;
    private MeFragment mMeFragment;

    private Drawable mDrawableLive;
    private Drawable mDrawableVideo;
    private Drawable mDrawableInteraction;
    private Drawable mDrawableMe;
    private Drawable mDrawableLiveUnselected;
    private Drawable mDrawableVideoUnselected;
    private Drawable mDrawableInteractionUnselected;
    private Drawable mDrawableMeUnselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.mipmap.icon_title);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        assert viewPager != null;
        viewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        mLiveBtn = (TextView) findViewById(R.id.live_btn);
        mVideoBtn = (TextView) findViewById(R.id.video_btn);
        mInteractionBtn = (TextView) findViewById(R.id.interaction_btn);
        mMeBtn = (TextView) findViewById(R.id.me_btn);
        mLiveBtn.setOnClickListener(this);
        mVideoBtn.setOnClickListener(this);
        mInteractionBtn.setOnClickListener(this);
        mMeBtn.setOnClickListener(this);

        mFragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);

        initDrawables();
//        fillData(, true);

        refreshCategory();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        mDrawableLive = getResources().getDrawable(R.mipmap.ic_action_video);
        assert mDrawableLive != null;
        mDrawableLive.setBounds(0, 0, mDrawableLive.getMinimumWidth(), mDrawableLive.getMinimumHeight());
        mDrawableVideo = getResources().getDrawable(R.mipmap.ic_action_youtube);
        assert mDrawableVideo != null;
        mDrawableVideo.setBounds(0, 0, mDrawableVideo.getMinimumWidth(), mDrawableVideo.getMinimumHeight());
        mDrawableInteraction = getResources().getDrawable(R.mipmap.ic_action_share_2);
        assert mDrawableInteraction != null;
        mDrawableInteraction.setBounds(0, 0, mDrawableInteraction.getMinimumWidth(), mDrawableInteraction.getMinimumHeight());
        mDrawableMe = getResources().getDrawable(R.mipmap.ic_action_user);
        assert mDrawableMe != null;
        mDrawableMe.setBounds(0, 0, mDrawableMe.getMinimumWidth(), mDrawableMe.getMinimumHeight());

        mDrawableLiveUnselected = getResources().getDrawable(R.mipmap.ic_action_video_us);
        assert mDrawableLiveUnselected != null;
        mDrawableLiveUnselected.setBounds(0, 0, mDrawableLiveUnselected.getMinimumWidth(), mDrawableLiveUnselected.getMinimumHeight());
        mDrawableVideoUnselected = getResources().getDrawable(R.mipmap.ic_action_youtube_us);
        assert mDrawableVideoUnselected != null;
        mDrawableVideoUnselected.setBounds(0, 0, mDrawableVideoUnselected.getMinimumWidth(), mDrawableVideoUnselected.getMinimumHeight());
        mDrawableInteractionUnselected = getResources().getDrawable(R.mipmap.ic_action_share_2_us);
        assert mDrawableInteractionUnselected != null;
        mDrawableInteractionUnselected.setBounds(0, 0, mDrawableInteractionUnselected.getMinimumWidth(), mDrawableInteractionUnselected.getMinimumHeight());
        mDrawableMeUnselected = getResources().getDrawable(R.mipmap.ic_action_user_us);
        assert mDrawableMeUnselected != null;
        mDrawableMeUnselected.setBounds(0, 0, mDrawableMeUnselected.getMinimumWidth(), mDrawableMeUnselected.getMinimumHeight());
    }

    private void showVideoFragment() {
        if (mVideoFragment == null) {
            mVideoFragment = VideoFragment.newInstance();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, mVideoFragment);
        ft.commit();
        mFragmentContainer.setVisibility(View.VISIBLE);
    }

    private void showMeFragment() {
        if (mMeFragment == null) {
            mMeFragment = MeFragment.newInstance();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, mMeFragment);
        ft.commit();
        mFragmentContainer.setVisibility(View.VISIBLE);
    }

    private void refreshCategory() {
        OkHttpClientManager.getInstance().asyncGet(Urls.GET_CATEGORY, this);
    }

    private boolean fillData(String data, boolean fromCache) {
        boolean isOk = false;

        if (!TextUtils.isEmpty(data)) {
            Log.d(TAG, "json fromCache[" + fromCache + "]=" + data);

            try {
                mData = Utils.fromJson("{\"data\":" + data + "}", CategoryData.class);
                if (mData != null || fromCache) {
                    mSectionsPagerAdapter.notifyDataSetChanged();
                    isOk = true;
                    if (!fromCache) {
//                        OfflineDataCache.saveProgramList(this, data);
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return isOk || fromCache;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, R.string.loading_data_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String data = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fillData(data, false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_btn:
                if (mSelectedIndex == LIVE_INDEX) {
                    return;
                }
                mSelectedIndex = LIVE_INDEX;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentById(R.id.fragment_container);
                if (mVideoFragment != null && fragment != null && fragment instanceof VideoFragment) {
                    ft.remove(mVideoFragment);
                    ft.commit();
                }
                mFragmentContainer.setVisibility(View.GONE);

                mLiveBtn.setCompoundDrawables(null, mDrawableLive, null, null);
                mVideoBtn.setCompoundDrawables(null, mDrawableVideoUnselected, null, null);
                mInteractionBtn.setCompoundDrawables(null, mDrawableInteractionUnselected, null, null);
                mMeBtn.setCompoundDrawables(null, mDrawableMeUnselected, null, null);
                break;

            case R.id.video_btn:
                if (mSelectedIndex == VIDEO_INDEX) {
                    return;
                }
                mSelectedIndex = VIDEO_INDEX;
                showVideoFragment();
                mLiveBtn.setCompoundDrawables(null, mDrawableLiveUnselected, null, null);
                mVideoBtn.setCompoundDrawables(null, mDrawableVideo, null, null);
                mInteractionBtn.setCompoundDrawables(null, mDrawableInteractionUnselected, null, null);
                mMeBtn.setCompoundDrawables(null, mDrawableMeUnselected, null, null);
                break;

            case R.id.interaction_btn:
                if (mSelectedIndex == INTERACTION_INDEX) {
                    return;
                }
                mSelectedIndex = INTERACTION_INDEX;
                showVideoFragment();
                mLiveBtn.setCompoundDrawables(null, mDrawableLiveUnselected, null, null);
                mVideoBtn.setCompoundDrawables(null, mDrawableVideoUnselected, null, null);
                mInteractionBtn.setCompoundDrawables(null, mDrawableInteraction, null, null);
                mMeBtn.setCompoundDrawables(null, mDrawableMeUnselected, null, null);
                break;

            case R.id.me_btn:
                if (mSelectedIndex == ME_INDEX) {
                    return;
                }
                mSelectedIndex = ME_INDEX;
                showMeFragment();
                mLiveBtn.setCompoundDrawables(null, mDrawableLiveUnselected, null, null);
                mVideoBtn.setCompoundDrawables(null, mDrawableVideoUnselected, null, null);
                mInteractionBtn.setCompoundDrawables(null, mDrawableInteractionUnselected, null, null);
                mMeBtn.setCompoundDrawables(null, mDrawableMe, null, null);
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ProgramListFragment.newInstance(mData.getData().get(position).getCol_resource());
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : (mData.getData() == null ? 0 : mData.getData().size());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mData.getData().get(position).getCol_name();
        }
    }
}
