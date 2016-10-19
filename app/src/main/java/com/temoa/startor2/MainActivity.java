package com.temoa.startor2;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.temoa.startor2.adapter.HomeRecyclerAdapter;
import com.temoa.startor2.adapter.RollPagerAdapter;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.mvpMain.IView;
import com.temoa.startor2.mvpMain.Presenter;
import com.temoa.startor2.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements IView, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private Presenter mPresenter;

    private Toolbar mToolbar;
    private DrawerLayout drawer;
    private SwipeRefreshLayout mRefreshLayout;
    private RollPagerView mRollPagerView;

    private RollPagerAdapter mPagerAdapter;
    private HomeRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new Presenter(MainActivity.this, this);
        mPresenter.onCreate();
        if (!BuildConfig.DEBUG) {
            mPresenter.getNewData();
        }

        initToolbar();
        initDrawer();
        initRollPagerView();
        initScrollLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
    }

    private void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_boss) {

        } else if (id == R.id.nav_girl) {

        } else if (id == R.id.nav_boy) {

        } else if (id == R.id.nav_download) {

        } else if (id == R.id.nav_about) {

        }
        drawer.closeDrawer(GravityCompat.START);
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
        // 设置切换动画时间
        mRollPagerView.setAnimationDurtion(1500);
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
        mRecyclerAdapter = new HomeRecyclerAdapter(this, recycler, null);
        mRecyclerAdapter.addOnItemClickListener(new HomeRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos, VideoList video) {
                VideoActivity.launch(MainActivity.this, video.getAid(), video.getPic());
            }
        });
        mRecyclerAdapter.addOnItemChildClickListener(new HomeRecyclerAdapter.ItemChildClickListener() {
            @Override
            public void onItemChildClickListener(View view, String type) {
                CategoryActivity.launch(MainActivity.this, type);
            }
        });
        recycler.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null)
            mPresenter.getNewData();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
        ToastUtils.show(this, msg);
    }
}
