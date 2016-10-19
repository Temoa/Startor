package com.temoa.startor2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.temoa.startor2.adapter.VideoRecyclerAdapter;
import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoRecommend.video;
import com.temoa.startor2.beans.VideoSrc;
import com.temoa.startor2.mvpVideo.IView;
import com.temoa.startor2.mvpVideo.ModelImpl;
import com.temoa.startor2.mvpVideo.Presenter;
import com.temoa.startor2.utils.ToastUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity implements IView {

    private static final String EXTRA_AID = "aid";
    private static final String EXTRA_PIC = "pic";

    private View mRootView;
    private TextView mTitleTv;
    private FloatingActionButton mFab;
    private AppBarLayout mAppBarLayout;
    private RecyclerView mRecyclerView;
    private JCVideoPlayerStandard mPlayer;
    private VideoRecyclerAdapter mRecyclerAdapter;

    private Presenter mPresenter;

    private int aid;
    private int cid;
    private String pic;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        if (intent != null) {
            aid = intent.getIntExtra(EXTRA_AID, 0);
            pic = intent.getStringExtra(EXTRA_PIC);
        } else {
            if (savedInstanceState != null) {
                aid = savedInstanceState.getInt(EXTRA_AID);
                pic = savedInstanceState.getString(EXTRA_PIC);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_AID, aid);
        outState.putString(EXTRA_PIC, pic);
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
                if (mPresenter != null)
                    mPresenter.getVideoSrc(cid, ModelImpl.VIDEO_TYPE_MP4);
            }
        });

        ImageView faceImg = (ImageView) findViewById(R.id.video_img);
        Glide.with(this).load(pic).centerCrop().dontAnimate().into(faceImg);

        mPlayer = (JCVideoPlayerStandard) findViewById(R.id.video_player);

        mRootView = findViewById(android.R.id.content);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.video_appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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
                Animation fabAnimation = new RotateAnimation(
                        0f, 359f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                fabAnimation.setDuration(500);
                fabAnimation.setRepeatCount(-1);
                LinearInterpolator linearInterpolator = new LinearInterpolator();
                fabAnimation.setInterpolator(linearInterpolator);
                mFab.startAnimation(fabAnimation);

                if (mPresenter != null)
                    mPresenter.getVideoSrc(cid, ModelImpl.VIDEO_TYPE_MP4);
            }
        });
    }

    private void initRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.common_content_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new VideoRecyclerAdapter(this, null, null);
        mRecyclerAdapter.setOnItemClickListener(new VideoRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int pos, video data) {
                VideoActivity.launch(VideoActivity.this, data.getAid(), data.getCover());
            }
        });
        mRecyclerAdapter.setOnItemChildClickListener(new VideoRecyclerAdapter.ItemChildClickListener() {
            @Override
            public void onItemChildClickListener(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.video_share:
                        break;
                    case R.id.video_coin:
                        String url = "http://www.bilibili.com/video/av" + aid;
                        Intent coinIntent = new Intent("android.intent.action.VIEW");
                        coinIntent.setData(Uri.parse(url));
                        startActivity(coinIntent);
                        break;
                    case R.id.video_download:
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void getVideoInfo(VideoInfo data) {
        if (mRecyclerAdapter != null) {
            mRecyclerAdapter.setNewHeaderData(data);
            cid = data.getCid();
            title = data.getTitle();
        }
    }

    @Override
    public void getVideoSrc(final VideoSrc data) {
        String videoUrl = data.getDurl().get(0).getUrl();
        if (mFab != null) {
            mFab.clearAnimation();
        }
        JCVideoPlayerStandard.startFullscreen(
                VideoActivity.this,
                JCVideoPlayerStandard.class,
                videoUrl,
                title);
    }


    @Override
    public void getVideoRecommend(VideoRecommend data) {
        if (mRecyclerAdapter != null)
            mRecyclerAdapter.setNewData(data.getList());

    }

    @Override
    public void showToast(String msg) {
        ToastUtils.show(this, msg);
    }

    public static void launch(Activity activity, int aid, String pic) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra(EXTRA_AID, aid);
        intent.putExtra(EXTRA_PIC, pic);
        activity.startActivity(intent);
    }
}
