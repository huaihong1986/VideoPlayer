package com.cvn.cmsgd;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cvn.cmsgd.fragment.EPGListFragment;
import com.cvn.cmsgd.model.EPGData;
import com.cvn.cmsgd.model.LoginResponseData;
import com.cvn.cmsgd.model.User;
import com.cvn.cmsgd.net.OkHttpClientManager;
import com.cvn.cmsgd.net.Urls;
import com.cvn.cmsgd.player.MediaController;
import com.cvn.cmsgd.player.VideoView;
import com.cvn.cmsgd.storage.OfflineDataCache;
import com.cvn.cmsgd.util.LoginManager;
import com.cvn.cmsgd.util.RotateManager;
import com.cvn.cmsgd.util.Utils;
import com.cvn.cmsgd.widget.FreeOverDialog;
import com.cvn.cmsgd.widget.LoginDialog;
import com.cvn.cmsgd.util.DensityUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class PlayerActivity extends AppCompatActivity implements Callback {

    private static final String TAG = "PlayerActivity";

    public static final String KEY_PLAY_URL = "play_url";
    public static final String KEY_PLAY_FREE_TIME = "play_free_time";
    public static final String KEY_TITLE = "title";
    public static final int PLAY_FREE_TIME = 10;
    public static final int DAYS_COUNT = 6;

    private static final int WHAT_CHECK_FREE_VIDEO = 8001;
    private int mRequestType = LoginManager.TYPE_REGISTER;

    private long mTimeFree = 10;

    private VideoView mPlayer;
    private MediaController mMediaController;
    private UpdateHandler mUpdateHandler;

    private boolean mIsLoading = false;
    private FreeOverDialog mFreeOverDialog;
    private LoginDialog mLoginDialog;
    private boolean mIsStopPlayback = false;

    private String mTitle;
    private User mUser;
    private String mCurrentEpg;
    private LinearLayout mShow;
    private LinearLayout mShowepgs;
    private RelativeLayout mPlayerout;

    private int mLandPlayeroutwidth;
    private int mLandPlayeroutwheight;
    private int mPortraitPlayeroutwidth;
    private int mPortraitPlayeroutwheight;

    private RotateManager mRotateMgr;

    static class UpdateHandler extends Handler {

        private PlayerActivity mActivity;

        public UpdateHandler(PlayerActivity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.checkFree()) {
                removeMessages(WHAT_CHECK_FREE_VIDEO);
                sendEmptyMessageDelayed(WHAT_CHECK_FREE_VIDEO, 1000);
            }
        }
    }

    private boolean checkFree() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            int currentPosition = mPlayer.getCurrentPosition();
            Log.d(TAG, "currentPosition =" + (currentPosition / 1000));
            if (currentPosition / 1000 >= PLAY_FREE_TIME) {
                showLogin();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        mLandPlayeroutwidth = height;
        mLandPlayeroutwheight = width;
        mPortraitPlayeroutwidth = width;
        mPortraitPlayeroutwheight = DensityUtil.dip2px(this, 230);
        init();
    }

    private void init() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player_port);

        mPlayerout = (RelativeLayout) findViewById(R.id.video_player_container);

        mShow = (LinearLayout) findViewById(R.id.loading);
        mShowepgs = (LinearLayout) findViewById(R.id.epg_list);
        mUpdateHandler = new UpdateHandler(this);
        mUser = OfflineDataCache.getUser(this);

        Intent it = getIntent();
        String url = it.getStringExtra(KEY_PLAY_URL);
        if (TextUtils.isEmpty(url)) {
            mShow.setVisibility(View.GONE);
            Toast.makeText(this, R.string.loading_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mTimeFree = it.getLongExtra(KEY_PLAY_FREE_TIME, PLAY_FREE_TIME);
        mTimeFree = mTimeFree >= PLAY_FREE_TIME ? mTimeFree : PLAY_FREE_TIME;
        mTitle = it.getStringExtra(KEY_TITLE);

        mPlayer = (VideoView) findViewById(R.id.video_player);
        mMediaController = new MediaController(this);
        mMediaController.setMediaPlayer(mPlayer);
        mMediaController.setTitle(mTitle);
        if (!TextUtils.isEmpty(mCurrentEpg)) {
            mMediaController.setEPG(getString(R.string.current_play_epg, mCurrentEpg));
        }

        mMediaController.setonFullScreenListener(new MediaController.onFullScreenListener() {
            @Override
            public void fullScreen() {
                // TODO Auto-generated method stub
                int orientation = getRequestedOrientation();
                if (orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        && orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    mRotateMgr.landscape();
                } else {
                    mRotateMgr.portrait();
                }
            }
        });
        mPlayer.setMediaController(mMediaController);
        mPlayer.setVideoPath(url);

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayer.start();
                mShow.setVisibility(View.GONE);

/*                if (!User.isLogin(mUser)) {
                    mUpdateHandler.removeMessages(WHAT_CHECK_FREE_VIDEO);
                    mUpdateHandler.sendEmptyMessageDelayed(WHAT_CHECK_FREE_VIDEO, 1000);

                    mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                           checkFree();
                        }
                    });
                }*/
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mShow.setVisibility(View.GONE);
                finish();
                return false;
            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        if (viewPager != null) {
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(sectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
            }
        }
        mRotateMgr = new RotateManager(this);
//        mRotateMgr.setOnChangeListener(mChangeListener);
    }
/*    private RotateManager.OnChangeListener mChangeListener = new RotateManager.OnChangeListener() {
        @Override
        public void onChange(int orientation) {
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                ;
            } else {
              ;
            }
        }
    };*/

    //横竖屏切换
    private void initConfigurationChanged() {
        int type = getResources().getConfiguration().orientation;
        if (type == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayerout.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mPlayerout.getLayoutParams();
                    lp.width = mLandPlayeroutwidth;
                    lp.height = mLandPlayeroutwheight;
                    mPlayerout.setLayoutParams(lp);
                    mPlayer.post(new Runnable() {
                        @Override
                        public void run() {
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPlayer.getLayoutParams();
                            lp.width = mLandPlayeroutwidth;
                            lp.height = mLandPlayeroutwheight;
                            mPlayer.setLayoutParams(lp);
                        }
                    });
                }
            });
            mShowepgs.setVisibility(View.GONE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (type == Configuration.ORIENTATION_PORTRAIT) {

            mPlayerout.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mPlayerout.getLayoutParams();
                    lp.width = mPortraitPlayeroutwidth;
                    lp.height = mPortraitPlayeroutwheight;
                    mPlayerout.setLayoutParams(lp);
                    mPlayer.post(new Runnable() {
                        @Override
                        public void run() {
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mPlayer.getLayoutParams();
                            lp.width = mPortraitPlayeroutwidth;
                            lp.height = mPortraitPlayeroutwheight;
                            mPlayer.setLayoutParams(lp);
                        }
                    });
                }
            });
            mShowepgs.setVisibility(View.VISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mMediaController = new MediaController(this);
        mMediaController.setMediaPlayer(mPlayer);
        mMediaController.setTitle(mTitle);
        if (!TextUtils.isEmpty(mCurrentEpg)) {
            mMediaController.setEPG(getString(R.string.current_play_epg, mCurrentEpg));
        }
        mMediaController.setonFullScreenListener(new MediaController.onFullScreenListener() {
            @Override
            public void fullScreen() {
                // TODO Auto-generated method stub
                int orientation = getRequestedOrientation();
                if (orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        && orientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    mRotateMgr.landscape();
                } else {
                    mRotateMgr.portrait();
                }
            }
        });
        mPlayer.setMediaController(mMediaController);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initConfigurationChanged();
        getEPG();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsStopPlayback) {
            if (mPlayer != null) {
                mPlayer.stopPlayback();
            }
        }
        mRotateMgr.resume();
        getEPG();
    }

    @Override
    protected void onPause() {
        mRotateMgr.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRotateMgr.destroy();
        mRotateMgr = null;
        super.onDestroy();
    }

    private void getEPG() {
        OkHttpClientManager.getInstance().asyncGet(Urls.GET_EPG_LIST + "c=" + mTitle + "&d=0", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = "{\"data\":" + response.body().string() + "}";
//                Log.d(TAG, "epg data =" + data);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(data)) {
                            EPGData epg = Utils.fromJson(data, EPGData.class);
                            if (epg != null && epg.getData() != null && epg.getData().size() > 0) {
                                /*StringBuilder builder = new StringBuilder();
                                for (EPGData.DataEntity item : epg.getData()) {
                                    builder.append(item.getStartTime());
                                    builder.append("-");
                                    builder.append(item.getEndTime());
                                    builder.append(" ");
                                    builder.append(item.getTitle());
                                    builder.append("    ");
                                }
                                mMediaController.setEPG(builder.toString());*/
                                EPGData.DataEntity item = epg.getData().get(0);
                                mCurrentEpg = item.getTitle();
                                mMediaController.setEPG(getString(R.string.current_play_epg, mCurrentEpg));
                            } else {
                                mMediaController.setEPG("");
                            }
                        } else {
                            mMediaController.setEPG("");
                        }
                    }
                });
            }
        });
    }

    private void showLogin() {
        if (mPlayer != null) {
            mPlayer.stopPlayback();
            mIsStopPlayback = true;
        }
        showFreeOverDialog();
    }

    private void showFreeOverDialog() {
        if (mFreeOverDialog != null) {
            mFreeOverDialog.dismiss();
            mFreeOverDialog = null;
        }
        mFreeOverDialog = new FreeOverDialog(this);
        mFreeOverDialog.setMessage(String.valueOf(mTimeFree));
        mFreeOverDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
                mFreeOverDialog.dismiss();
            }
        });
    }

    private void showLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
            mLoginDialog = null;
        }
        mLoginDialog = new LoginDialog(this);
        mLoginDialog.changeState(mRequestType);
        mLoginDialog.setPositiveButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLoading) {
                    mIsLoading = true;
                    LoginManager.login(PlayerActivity.this,
                            mLoginDialog.getInputUser(), mLoginDialog.getInputPassword(), mLoginDialog.getInputConfirmPassword(),
                            mRequestType, PlayerActivity.this);
                }
            }
        });
        mLoginDialog.setNegativeButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIsLoading = false;
                showToast();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String data = response.body().string();
//        Log.d(TAG, "data =" + data);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(data)) {
                    LoginResponseData msg = Utils.fromJson(data, LoginResponseData.class);

                    if (msg != null) {
                        Toast.makeText(PlayerActivity.this, msg.getMsgCondition(), Toast.LENGTH_SHORT).show();
                        if (msg.getMsg().equals("1")) {
                            switch (mRequestType) {
                                case LoginManager.TYPE_REGISTER:
                                    mRequestType = LoginManager.TYPE_LOGIN;
                                    mLoginDialog.changeState(mRequestType);
                                    break;

                                case LoginManager.TYPE_LOGIN:
                                    OfflineDataCache.saveUser(PlayerActivity.this, mLoginDialog.getInputUser(), mLoginDialog.getInputPassword());
                                    mLoginDialog.dismiss();

                                    mUser = OfflineDataCache.getUser(PlayerActivity.this);
                                    if (User.isLogin(mUser)) {
                                        if (mIsStopPlayback) {
                                            mIsStopPlayback = false;
                                            if (mPlayer != null) {
                                                mPlayer.resume();
                                            }
                                        }
                                    }
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

    private void showToast() {
        switch (mRequestType) {
            case LoginManager.TYPE_REGISTER:
                Toast.makeText(PlayerActivity.this, R.string.register_error, Toast.LENGTH_SHORT).show();
                break;

            case LoginManager.TYPE_LOGIN:
                Toast.makeText(PlayerActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                break;

            case LoginManager.TYPE_FORGET_PASSWORD:
                break;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return EPGListFragment.newInstance(mTitle, position);
        }

        @Override
        public int getCount() {
            return DAYS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            long time = System.currentTimeMillis() + position * 86400000;
            return Utils.getWeek(time) + "\n" + Utils.parserTime(time);
        }
    }


}
