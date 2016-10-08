package com.temoa.startor.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.beans.VideoInfoBean;
import com.temoa.startor.beans.VideoTaskBean;
import com.temoa.startor.beans.VideoUrlBean;
import com.temoa.startor.callback.ICallback;
import com.temoa.startor.network.GsonRequest;
import com.temoa.startor.network.RequestFactory;
import com.temoa.startor.utils.FileUtils;
import com.temoa.startor.utils.LogUtils;
import com.temoa.startor.utils.SnackbarUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VideoActivity";
    private static final String VideoSavePath
            = Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + "/startor/";
    private static final String VIDEO_TYPE_MP4 = "mp4";
    private static final String VIDEO_TYPE_FLY = "fly";


    private TextView titleTv, playTv, createTimeTv, descTv;
    private JCVideoPlayerStandard mPlayer;
    private FloatingActionMenu fabMenu;
    private RequestQueue mQueue;
    private View rootView;

    private String videoPlayUrl;
    private String videoTitle;
    private int aid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        rootView = this.findViewById(android.R.id.content);
        mQueue = MyApplication.getQueue();
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        Intent intent = getIntent();
        Bundle dataBundle = intent.getBundleExtra("videoInfo");
        aid = dataBundle.getInt("aid");

        mPlayer = (JCVideoPlayerStandard) findViewById(R.id.video_video_view);

        titleTv = (TextView) findViewById(R.id.video_text_view_title);
        playTv = (TextView) findViewById(R.id.video_text_view_play);
        createTimeTv = (TextView) findViewById(R.id.video_text_view_create);
        descTv = (TextView) findViewById(R.id.video_text_view_desc);

        fabMenu = (FloatingActionMenu) findViewById(R.id.video_fab_menu);
        FloatingActionButton shareFab = (FloatingActionButton) findViewById(R.id.video_fab_share);
        FloatingActionButton coinFab = (FloatingActionButton) findViewById(R.id.video_fab_coin);
        FloatingActionButton downloadFab
                = (FloatingActionButton) findViewById(R.id.video_fab_download);
        shareFab.setOnClickListener(this);
        coinFab.setOnClickListener(this);
        downloadFab.setOnClickListener(this);

        getVideoInfo(aid);
    }

    private void setText(String title, String desc, String time, String play) {
        titleTv.setText(title);
        playTv.setText(play);
        createTimeTv.setText(time);
        descTv.setText(desc);
    }

    /**
     * 获取视频的信息
     *
     * @param aid 视频AV号
     */
    private void getVideoInfo(int aid) {
        GsonRequest<VideoInfoBean> request = RequestFactory.getVideoInfo(
                aid, new ICallback<VideoInfoBean>() {
                    @Override
                    public void onNetworkSucceed(VideoInfoBean data) {
                        // 视频CV号
                        int cid = data.getCid();
                        // 视频图
                        String pic = data.getPic();
                        // 视频标题
                        videoTitle = data.getTitle();
                        // 视频详情
                        String desc = data.getDescription();
                        // 发布时间
                        String crateTime = data.getCreated_at();
                        String formTime = " " + (crateTime != null ? crateTime.substring(0, 11) : null);
                        // 播放次数
                        String play = " " + String.valueOf(data.getPlay());
                        setText(videoTitle, desc, formTime, play);
                        getVideoPlayUrl(cid, pic);
                    }

                    @Override
                    public void onNetworkError(String error) {
                        SnackbarUtils.show(rootView, error);
                    }
                });
        mQueue.add(request);
    }

    /**
     * 获取视频地址
     *
     * @param cid 视频CV号
     * @param pic 视频缩略图
     */
    private void getVideoPlayUrl(int cid, final String pic) {
        // 获取视频的地址
        GsonRequest<VideoUrlBean> request = RequestFactory.getVideoPlay(
                cid, VIDEO_TYPE_MP4, new ICallback<VideoUrlBean>() {
                    @Override
                    public void onNetworkSucceed(VideoUrlBean data) {
                        videoPlayUrl = data.getDurl().get(0).getUrl();
                        initPlayer(videoPlayUrl, pic);
                    }

                    @Override
                    public void onNetworkError(String error) {
                        SnackbarUtils.show(rootView, error);
                    }
                });
        mQueue.add(request);
    }

    private void initPlayer(String videoPlayUrl, String pic) {
        LogUtils.i(videoPlayUrl);
        if (pic != null) {
            Glide.with(getApplicationContext()).load(pic).into(mPlayer.thumbImageView);
        }
        mPlayer.setUp(videoPlayUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.video_fab_share:
                // TODO: 2016/8/31 社会化分享
                Snackbar.make(view, "coming soon!!", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.video_fab_coin:
                openBilibili();
                break;
            case R.id.video_fab_download:
                checkPermissions();
                break;
        }
    }

    /**
     * 打开Bilibili视频界面进行投币
     */
    private void openBilibili() {
        String url = "http://www.bilibili.com/video/av" + aid;
        Uri uri = Uri.parse(url);
        Intent coinIntent = new Intent("android.intent.action.VIEW");
        coinIntent.setData(uri);
        startActivity(coinIntent);
    }

    /**
     * 缓存视频
     */
    private void downloadVideo() {
        if (videoPlayUrl == null || videoPlayUrl.equals("")) {
            LogUtils.i(TAG, "video url is null!!");
            SnackbarUtils.show(rootView, "视频下载地址出错");
            return;
        }
        final String filePath = VideoSavePath + videoTitle + ".mp4";
        if (FileUtils.fileExists(filePath)) {
            SnackbarUtils.showWithAction(rootView, "视频已缓存", "观看视频>", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playLocalVideo(filePath, videoTitle);
                }
            });
        } else {
            int taskId = FileDownloader.getImpl()
                    .create(videoPlayUrl)
                    .setPath(filePath)
                    .start();
            LogUtils.d("TASk ID ---> " + taskId);
            VideoTaskBean task = new VideoTaskBean();
            task.setTaskId(taskId);
            task.setTitle(videoTitle);
            task.setTaskId(taskId);
            task.setUrl(videoPlayUrl);
            task.setFilePath(filePath);
            task.setLoading(true);
            MyApplication.addTask(taskId, task);
            SnackbarUtils.showWithAction(rootView, "视频缓存中", "查看下载任务>", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(VideoActivity.this, DownloadActivity.class));
                }
            });
        }
    }

    /**
     * 播放本地视频
     *
     * @param videoPath  本地视频路径
     * @param videoTitle 视频名称
     */
    private void playLocalVideo(String videoPath, String videoTitle) {
        String path = "file://" + videoPath;
        mPlayer.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, videoTitle);
        JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, path, videoTitle);
    }

    /**
     * 请求存储读写权限
     */
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
                downloadVideo();
            }
        } else {
            downloadVideo();
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
                downloadVideo();
            } else {
                SnackbarUtils.show(rootView, "需要相关权限才能进行下载");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isOpened()) {
            fabMenu.close(true);
        } else {
            if (JCVideoPlayer.backPress()) {
                return;
            }
            super.onBackPressed();
        }
    }
}
