package com.temoa.startor2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor2.R;
import com.temoa.startor2.utils.TaskManager;
import com.temoa.startor2.adapter.DownloadAdapter;
import com.temoa.startor2.beans.Task;
import com.temoa.startor2.widget.JCPlayerNoFullscreenBtn;

import java.io.File;
import java.lang.ref.WeakReference;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class DownloadActivity extends AppCompatActivity {

    private DownloadAdapter mRecyclerAdapter;
    private TextView mShowTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        FileDownloader.getImpl().pauseAll();
        TaskManager.getImpl().onCreate(new WeakReference<>(this));

        initToolbar();
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        TaskManager.getImpl().onDestroy();
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_download));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        mShowTv = (TextView) findViewById(R.id.download_show);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.download_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new DownloadAdapter(this);
        mRecyclerAdapter.setPlayClickListener(new DownloadAdapter.PlayClickListener() {
            @Override
            public void onPlayClickListener(View v, Task task) {
                playVideo(task.getName(), task.getPath());
            }
        });
        mRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (mRecyclerAdapter.getItemCount() == 0) {
                    mShowTv.setVisibility(View.VISIBLE);
                } else {
                    mShowTv.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setAdapter(mRecyclerAdapter);

        if (mRecyclerAdapter.getItemCount() == 0)
            mShowTv.setVisibility(View.VISIBLE);

        JCVideoPlayerStandard player = (JCVideoPlayerStandard) findViewById(R.id.download_player);
    }

    public void postNotifyDataChanged() {
        if (mRecyclerAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerAdapter != null) {
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void playVideo(String name, String path) {
        if (TextUtils.isEmpty(path))
            return;

        File videoFile = new File(path);
        if (videoFile.exists()) {
            String playPath = "file://" + path;
            JCVideoPlayerStandard.startFullscreen(this, JCPlayerNoFullscreenBtn.class, playPath, name);
        }
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, DownloadActivity.class);
        activity.startActivity(intent);
    }
}
