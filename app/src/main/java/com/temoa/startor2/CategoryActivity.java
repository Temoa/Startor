package com.temoa.startor2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.temoa.startor2.adapter.CategoryRecyclerAdapter;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.mvpCategory.IView;
import com.temoa.startor2.mvpCategory.Presenter;
import com.temoa.startor2.utils.ToastUtils;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements IView {

    private static final String EXTRA_CATEGORY = "videoCategory";

    private Presenter mPresenter;
    private CategoryRecyclerAdapter mRecyclerAdapter;

    private String videoCategory;
    private int page;
    private int pages = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Intent intent = getIntent();
        if (intent != null) {
            videoCategory = intent.getStringExtra(EXTRA_CATEGORY);
        } else {
            if (savedInstanceState != null)
                videoCategory = savedInstanceState.getString(EXTRA_CATEGORY);
        }

        mPresenter = new Presenter(this);
        mPresenter.onCreate(videoCategory);
        page = 2;

        initToolbar();
        initRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CATEGORY, videoCategory);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.video_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(videoCategory);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 获取返回箭头点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        final RecyclerView recycler = (RecyclerView) findViewById(R.id.common_content_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new CategoryRecyclerAdapter(this, recycler, null);
        mRecyclerAdapter.addItemClickListener(new CategoryRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos, VideoList video) {
                VideoActivity.launch(CategoryActivity.this, video.getAid(), video.getPic());
            }
        });
        if (!videoCategory.equals(Constants.OTHER_VIDEO)) {
            mRecyclerAdapter.setLoadMore(true);
            mRecyclerAdapter.addLoadMoreListener(new CategoryRecyclerAdapter.LoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMoreData(videoCategory, page);
                }
            });
        }
        recycler.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void getData(List<VideoList> data) {
        if (mRecyclerAdapter == null) {
            return;
        }
        mRecyclerAdapter.setNewData(data);
        // 如果不能填满一屏, 添加更多
        if (data.size() < 7 && mPresenter != null)
            mPresenter.getMoreData(videoCategory, page);
    }

    @Override
    public void getMoreData(List<VideoList> data) {
        if (mRecyclerAdapter == null) {
            return;
        }
        if (page < pages)
            page++;
        else
            mRecyclerAdapter.loadCompleted();
        mRecyclerAdapter.addData(data);

    }

    /**
     * 这里起小点将不同种类的视频上传到不同的帐号
     * 获取每个帐号上传视频的总页数
     * 但是有些分类的视频在后面的页数没有出现
     * 为了不破坏正常的逻辑, 所以就有下面hideProgressBar()的switch方法
     * 一旦返回的list 的个数为0就认为后面的页数没有该类的视频
     * 然后调用adapter的loadCompleted()
     *
     * @param pages 上传视频总页数
     */
    @Override
    public void getPages(int pages) {
        this.pages = pages;
    }

    @Override
    public void changeProgressBarStatus(int flag) {
        if (mRecyclerAdapter == null) {
            return;
        }
        switch (flag) {
            case Presenter.FLAG_MORE_DATA:
                mRecyclerAdapter.setLoadMoreStatus(CategoryRecyclerAdapter.STATUS_DISMISS);
                break;
            case Presenter.FLAG_NO_MODE_DATA:
                mRecyclerAdapter.loadCompleted();
                break;
            case Presenter.FLAG_ERROR:
                mRecyclerAdapter.setLoadMoreStatus(CategoryRecyclerAdapter.STATUS_ERROR);
                break;
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.show(this, msg);
    }

    public static void launch(Activity activity, String type) {
        Intent intent = new Intent(activity, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY, type);
        activity.startActivity(intent);
    }
}
