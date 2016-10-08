package com.temoa.startor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor.Constants;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.fragment.HomeFragment;
import com.temoa.startor.fragment.CommonFragment;
import com.temoa.startor.utils.NetUtils;
import com.temoa.startor.utils.SnackbarUtils;

public class HomeActivity extends AppCompatActivity {

    private static final int FIRST_PAGE = 0;
    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private View rootView;
    private Class mClazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rootView = findViewById(android.R.id.content);

        initToolbar();
        initDrawerLayout();
        firstTimeToOpen();

        if (NetUtils.isNetConnection(this)) {
            if (!NetUtils.isWifiConnection(this)) {
                SnackbarUtils.show(rootView, "正在使用移动网络,请注意流量");
            }
        } else {
            SnackbarUtils.show(rootView, "网络未连接");
        }
        if (savedInstanceState == null) {
            animateToolbar();
        }

        mFragmentManager = getSupportFragmentManager();
        fragmentTransaction(FIRST_PAGE);
    }

    private void fragmentTransaction(int type) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment fragment;
        if (type == 0) {
            fragment = new HomeFragment();
        } else {
            fragment = new CommonFragment().getInstance(type);
        }
        fragmentTransaction.replace(R.id.home_content_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
        MyApplication.releaseAllTask();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayShowTitleEnabled(false);
        TextView title = (TextView) findViewById(R.id.home_text_toolbar_title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Neue.ttf"));
    }

    private void animateToolbar() {
        View t = mToolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;
            title.setAlpha(0f);
            title.setScaleX(0.8f);
            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900)
                    .setInterpolator(new FastOutSlowInInterpolator());
        }
    }

    private void initDrawerLayout() {
        mDrawer = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle
                = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.trans, R.string.trans) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // 解决drawerLayout点击Item跳转页面关闭时出现卡顿
                if (mClazz != null) {
                    startActivity(mClazz);
                }
            }
        };
        actionBarDrawerToggle.syncState();
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        NavigationView nav = (NavigationView) findViewById(R.id.home_nav_view);
        TextView navTitle = (TextView) nav.getHeaderView(0).findViewById(R.id.nav_head_text_app_name);
        navTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Neue.ttf"));
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        fragmentTransaction(FIRST_PAGE);
                        mClazz = null;
                        break;
                    case R.id.action_boss:
                        fragmentTransaction(Constants.MID_起小点);
                        mClazz = null;
                        break;
                    case R.id.action_girl:
                        fragmentTransaction(Constants.MID_长歌);
                        mClazz = null;
                        break;
                    case R.id.action_boy:
                        fragmentTransaction(Constants.MID_起小D);
                        mClazz = null;
                        break;
                    case R.id.action_download_manager:
                        mClazz = DownloadActivity.class;
                        break;
                    case R.id.action_about:
                        mClazz = AboutActivity.class;
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void firstTimeToOpen() {
        SharedPreferences sharedPreferences = getSharedPreferences("first", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);
        if (isFirstTime) {
            mDrawer.openDrawer(GravityCompat.START);
            editor.putBoolean("firstTime", false);
            editor.apply();
        }
    }

    private void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    SnackbarUtils.show(rootView, "再按一下退出应用");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                    System.exit(0);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
