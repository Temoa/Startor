package com.temoa.startor2.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor2.Constants;
import com.temoa.startor2.R;
import com.temoa.startor2.adapter.VideoAdapter;
import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoRecommend.video;
import com.temoa.startor2.mvpVideo.IView;
import com.temoa.startor2.mvpVideo.Presenter;
import com.temoa.startor2.utils.TaskManager;
import com.temoa.startor2.utils.ToastUtils;
import com.temoa.startor2.widget.JCPlayerNoFullscreenBtn;
import com.temoa.startor2.widget.ShareDialog;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity implements IView {

    private static final String EXTRA_AID = "aid";
    private static final String EXTRA_PIC = "videoCover";
    private static final int REQUEST_PERMISSION_CODE = 101;

    private TextView mTitleTv;
    private FloatingActionButton mFab;
    private JCVideoPlayerStandard mPlayer;
    private VideoAdapter mRecyclerAdapter;

    private Presenter mPresenter;

    private int aid;
    private int cid;
    private String videoName;
    private String videoDesc;
    private String videoCover;
    private String videoUrl;
    private boolean isDownloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        if (intent != null) {
            aid = intent.getIntExtra(EXTRA_AID, 0);
            videoCover = intent.getStringExtra(EXTRA_PIC);
        } else {
            if (savedInstanceState != null) {
                aid = savedInstanceState.getInt(EXTRA_AID);
                videoCover = savedInstanceState.getString(EXTRA_PIC);
            }
        }

        initViews();
        initRecycler();

        mPresenter = new Presenter(this);
        if (aid != 0)
            mPresenter.onCreate(aid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_AID, aid);
        outState.putString(EXTRA_PIC, videoCover);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }

        if (mRecyclerAdapter != null)
            mRecyclerAdapter = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()) {
            return;
        }

        super.onBackPressed();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.video_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTitleTv = (TextView) findViewById(R.id.video_toolbar_title);
        mTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(videoUrl);
            }
        });

        ImageView faceImg = (ImageView) findViewById(R.id.video_img);
        Glide.with(this).load(videoCover).centerCrop().dontAnimate().into(faceImg);

        mPlayer = (JCVideoPlayerStandard) findViewById(R.id.video_player);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.video_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    // 展开状态
                    mTitleTv.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    // 折叠状态
                    mTitleTv.setVisibility(View.VISIBLE);
                } else {
                    // 中间状态
                    mTitleTv.setVisibility(View.GONE);
                }
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.video_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animFab();
                playVideo(videoUrl);
            }
        });
    }

    private void animFab() {
        RotateAnimation rotate = new RotateAnimation(
                0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(-1);
        rotate.setInterpolator(new LinearInterpolator());
        mFab.startAnimation(rotate);
    }

    private void initRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.common_content_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerAdapter = new VideoAdapter(this, null, null);
        mRecyclerAdapter.setOnItemClickListener(new VideoAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int pos, video data) {
                VideoActivity.launch(VideoActivity.this, data.getAid(), data.getCover());
            }
        });
        mRecyclerAdapter.setOnItemChildClickListener(new VideoAdapter.ItemChildClickListener() {
            @Override
            public void onItemChildClickListener(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.video_share:
                        share();
                        break;
                    case R.id.video_coin:
                        insertCoins(aid);
                        break;
                    case R.id.video_download:
                        downloadVideo(videoName, videoUrl);
                        break;
                }
            }
        });

        recyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * 分享
     */
    public void share() {
        if (mPresenter != null) {
            if (!mPresenter.checkAppPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mPresenter.requestNeedPermission(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                return;
            }
        } else {
            return;
        }

        if (aid == 0)
            return;

        String url = Constants.BILIBILI_URL + aid;
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show();
        shareDialog.setData(url, videoName, videoDesc, videoCover);
    }

    /**
     * 打开Bilibili进行投币.
     * 官方登录做不到,只能直接跳转到其网页, 或者已经安装了B站的客户端跳转到客户端
     */
    public void insertCoins(int aid) {
        if (aid == 0)
            return;
        String url = Constants.BILIBILI_URL + aid;
        Intent coinIntent = new Intent(Intent.ACTION_VIEW);
        coinIntent.setData(Uri.parse(url));
        startActivity(coinIntent);
    }

    /**
     * 播放视频
     *
     * @param url 如果url 不为null 的话, 该视频已缓存, 可直接播放
     *            如果为null 的话, 需要请求得到视频的网络地址
     */
    public void playVideo(String url) {
        if (url == null) {
            if (mPresenter != null)
                mPresenter.getVideoSrc(cid, Presenter.VIDEO_TYPE_MP4, Presenter.FLAG_PLAY_VIDEO);
            return;
        }

        if (mFab != null)
            mFab.clearAnimation();

        JCVideoPlayer.startFullscreen(this, JCPlayerNoFullscreenBtn.class, url, videoName);
    }

    /**
     * 下载视频
     * 首先检查权限. 权限可以后如果该视频已缓存, 提示可直接播放
     * 没有缓存的话加入缓存队列
     *
     * @param title 视频名字
     * @param url   视频链接
     */
    public void downloadVideo(String title, String url) {
        if (mPresenter != null) {
            if (!mPresenter.checkAppPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mPresenter.requestNeedPermission(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                return;
            }
        } else {
            return;
        }

        if (isDownloaded) {
            showToast(getString(R.string.video_video_is_loaded));
            return;
        }

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(title)) {
            if (mPresenter != null) {
                mPresenter.getVideoSrc(cid, Presenter.VIDEO_TYPE_MP4, Presenter.FLAG_DOWNLOAD_VIDEO);
            }
            return;
        }

        boolean addSucceed = TaskManager.getImpl().addTask(this, title, url) != null;
        boolean startSucceed = FileDownloader.getImpl()
                .create(url).setPath(TaskManager.getImpl().getDiskCacheVideoDir(this, title)).start() != 0;

        if (addSucceed && startSucceed)
            showToast(getString(R.string.video_task_is_add));
    }

    @Override
    public void getVideoInfo(VideoInfo data) {
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.setNewHeaderData(data);

            cid = data.getCid();
            videoName = data.getTitle();
            videoDesc = data.getDescription();

            if (mPresenter != null) {
                if ((videoUrl = mPresenter.isVideoExists(this, videoName)) != null)
                    isDownloaded = true;
            }
        }
    }

    @Override
    public void getVideoSrc(String data, int flag) {
        videoUrl = data;
        if (flag == Presenter.FLAG_PLAY_VIDEO) {
            playVideo(videoUrl);
        } else {
            downloadVideo(videoName, videoUrl);
        }
    }


    @Override
    public void getVideoRecommend(VideoRecommend data) {
        if (mRecyclerAdapter != null)
            mRecyclerAdapter.setNewData(data.getList());
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(this, msg);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(getString(R.string.permission_is_request_succeed));
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    ToastUtils.showLong(this, getString(R.string.permission_request_on_you));
            }
        }
    }

    public static void launch(Activity activity, int aid, String pic) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(EXTRA_AID, aid);
        intent.putExtra(EXTRA_PIC, pic);
        activity.startActivity(intent);
    }
}
