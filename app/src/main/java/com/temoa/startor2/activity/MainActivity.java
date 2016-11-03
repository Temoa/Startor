package com.temoa.startor2.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor2.R;
import com.temoa.startor2.adapter.HomeAdapter;
import com.temoa.startor2.adapter.RollPagerAdapter;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.mvpMain.IView;
import com.temoa.startor2.mvpMain.Presenter;
import com.temoa.startor2.utils.ToastUtils;
import com.temoa.startor2.utils.WXHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView,
        NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private Presenter mPresenter;

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private SwipeRefreshLayout mRefreshLayout;
    private RollPagerView mRollPagerView;

    private RollPagerAdapter mPagerAdapter;
    private HomeAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawer();
        initRollPagerView();
        initScrollLayout();

        mPresenter = new Presenter(MainActivity.this, this);
        if (!isFirstTime())
            mPresenter.onCreate();
        mPresenter.getNewData();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }

        if (mRecyclerAdapter != null)
            mRecyclerAdapter = null;

        FileDownloader.getImpl().pauseAll();// 停止所有下载任务

        if ((WXHelper.wxApi) != null)
            WXHelper.wxApi.unregisterApp();

        super.onDestroy();
    }

    private void initDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_download) {
            DownloadActivity.launch(MainActivity.this);
        } else if (id == R.id.nav_about) {
            AboutActivity.launch(MainActivity.this);
        }

        return true;
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.video_toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans.ttf"));
    }

    private void initRollPagerView() {
        mRollPagerView = (RollPagerView) findViewById(R.id.main_pager);
        mPagerAdapter = new RollPagerAdapter(this, new ArrayList<VideoList>());
        mRollPagerView.setAnimationDurtion(1500);// 设置切换动画时间
        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                VideoList video = mPagerAdapter.getData(position);
                if (video != null)
                    VideoActivity.launch(MainActivity.this, video.getAid(), video.getPic());
            }
        });
        mRollPagerView.setAdapter(mPagerAdapter);
    }

    private void initScrollLayout() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_swipe);
        mRefreshLayout.setOnRefreshListener(this);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.main_recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        recycler.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new HomeAdapter(this, recycler, null);
        mRecyclerAdapter.addOnItemClickListener(new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos, VideoList video) {
                VideoActivity.launch(MainActivity.this, video.getAid(), video.getPic());
            }
        });
        mRecyclerAdapter.addOnItemChildClickListener(new HomeAdapter.ItemChildClickListener() {
            @Override
            public void onItemChildClickListener(View view, String type) {
                CategoryActivity.launch(MainActivity.this, type);
            }
        });
        recycler.setAdapter(mRecyclerAdapter);
    }

    private boolean isFirstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        boolean isFirst = sharedPreferences.getBoolean("isFirst", true);
        if (isFirst) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst", false);
            editor.apply();
        }
        return isFirst;
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null)
            mPresenter.getNewData();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    showToast(getString(R.string.main_twice_exit));
                    exitTime = System.currentTimeMillis();
                } else {
                    this.finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_download) {
            DownloadActivity.launch(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getSectionData(List<VideoList> newData) {
        if (mRecyclerAdapter != null)
            mRecyclerAdapter.setNewData(newData);
    }

    @Override
    public void getRollPagerData(List<VideoList> data) {
        mPagerAdapter = new RollPagerAdapter(this, data);
        mRollPagerView.setAdapter(mPagerAdapter);
    }

    @Override
    public void hideProgress() {
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(this, msg);
    }
}
