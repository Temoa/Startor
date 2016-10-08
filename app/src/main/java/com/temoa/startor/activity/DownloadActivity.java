package com.temoa.startor.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.temoa.startor.DownloadManager;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.adapter.DownloadVideoAdapter;
import com.temoa.startor.beans.VideoTaskBean;
import com.temoa.startor.utils.Divider;
import com.temoa.startor.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class DownloadActivity extends AppCompatActivity {

    private static final String TAG = "DownloadActivity";
    private static final String VideoSavePath
            = Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + "/startor/";

    private View rootView;
    private DownloadVideoAdapter mAdapter;
    private DownloadManager mManager;
    private ArrayList<VideoTaskBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        rootView = this.findViewById(android.R.id.content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initToolbar();
        initRecyclerView();
        checkPermissions();
//        getVideoFile();
//        mManager = new DownloadManager(mAdapter);
//        mManager.pauseAll();
//        startTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        SparseArray<VideoTaskBean> tasks = MyApplication.getTask();
        if (tasks.size() == 0) {
            mManager.closeManger();
        }
        super.onDestroy();
    }

    private void initRecyclerView() {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.download_recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new Divider(this, Divider.VERTICAL_LIST));
        mAdapter = new DownloadVideoAdapter(R.layout.recycler_view_download_item, mList);
        recycler.setAdapter(mAdapter);
        recycler.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                if (mManager == null) {
                    return;
                }
                int childId = view.getId();
                VideoTaskBean task = mAdapter.getItem(i);
                switch (childId) {
                    case R.id.item_btn_play:
                        if (task.isLoading()) {
                            if (task.isPause()) {
                                mManager.startTask(task, i);
                            } else {
                                mManager.pauseTask(task.getTaskId());
                            }
                        } else {
                            playVideo(task.getFilePath(), task.getTitle());
                        }
                        break;
                    case R.id.item_btn_delete:
                        if (task.isLoading()) {
                            mManager.clearTask(i, task.getTaskId(), task.getFilePath());
                            MyApplication.releaseTask(task.getTaskId());
                        } else {
                            deleteVideoFile(task.getFilePath(), i);
                        }
                        break;
                }
            }
        });
    }

    private void getVideoFile() {
        File rootFile = new File(VideoSavePath);
        System.out.println(rootFile.listFiles()[0]);
        File[] files = rootFile.listFiles();
        mList.clear();
        if (files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.getName().contains("temp")) {
                continue;
            }
            VideoTaskBean info = new VideoTaskBean();
            String fileName = file.getName();
            String filePath = file.getPath();
            long size = file.length();
            info.setTitle(fileName);
            info.setTotalSize((int) size);
            info.setSoFarSize((int) size);
            info.setFilePath(filePath);
            mList.add(info);
        }
        mAdapter.setNewData(mList);
    }

    private void deleteVideoFile(final String path, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("删除")
                .setMessage("是否删除视频缓存")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FileUtils.deleteFile(path);
                        mAdapter.remove(pos);
                    }
                })
                .setNegativeButton("CANCEL", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startTask() {
        if (mManager == null) {
            return;
        }
        SparseArray<VideoTaskBean> tasks = MyApplication.getTask();
        if (tasks.size() == 0) {
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            VideoTaskBean task = tasks.valueAt(i);
            mAdapter.add(i, task);
            mManager.startTask(task, i);
        }
    }

    private void playVideo(String videoPath, String name) {
        if (videoPath == null) {
            return;
        }
        String path = "file://" + videoPath;
        JCVideoPlayerStandard jcVideoPlayerStandard
                = (JCVideoPlayerStandard) findViewById(R.id.download_video_view);
        jcVideoPlayerStandard.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, name);
        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, path, name);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                getVideoFile();
                mManager = new DownloadManager(mAdapter);
                mManager.pauseAll();
                startTask();
            }
        } else {
            getVideoFile();
            mManager = new DownloadManager(mAdapter);
            mManager.pauseAll();
            startTask();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            int grantResult1 = grantResults[0];
            int grantResult2 = grantResults[1];
            if (grantResult1 == PackageManager.PERMISSION_GRANTED
                    && grantResult2 == PackageManager.PERMISSION_GRANTED) {
                getVideoFile();
                mManager = new DownloadManager(mAdapter);
                mManager.pauseAll();
                startTask();
            } else {
                Toast.makeText(this, "需要相关的权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
