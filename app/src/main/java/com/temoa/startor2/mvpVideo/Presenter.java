package com.temoa.startor2.mvpVideo;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoSrc;
import com.temoa.startor2.listener.VideoCommonListener;
import com.temoa.startor2.utils.PermissionsHelper;
import com.temoa.startor2.utils.TaskManager;

import java.io.File;

/**
 * Created by Temoa
 * on 2016/10/17 16:54
 */

public class Presenter implements VideoCommonListener {

    public static final String VIDEO_TYPE_MP4 = "mp4";
    public static final String VIDEO_TYPE_FLV = "flv";

    public static final int FLAG_PLAY_VIDEO = 0;
    public static final int FLAG_DOWNLOAD_VIDEO = 1;

    private IView mView;
    private IModel mModel;

    public Presenter(IView iView) {
        mView = iView;
        mModel = new ModelImpl();
    }

    public void onCreate(int aid) {
        getVideoInfo(aid);
        getVideoRecommend(aid);
    }

    public void onDestroy() {
        if (mView != null)
            mView = null;
        if (mModel != null)
            mModel = null;
    }

    public String isVideoExists(Context context, String videoName) {
        if (TextUtils.isEmpty(videoName)) {
            return null;
        }

        String path = TaskManager.getImpl().getDiskCacheVideoDir(context, videoName);
        File videoFile = new File(path);
        if (videoFile.exists()) {
            return "file://" + path;
        }

        return null;
    }

    public boolean checkAppPermission(Activity activity, String permission) {
        return PermissionsHelper.checkPermissions(activity, permission);
    }

    public void requestNeedPermission(Activity activity, String[] permissions, int requestCode) {
        PermissionsHelper.requestPermissions(activity, permissions, requestCode);
    }

    public void getVideoInfo(int aid) {
        if (mModel != null)
            mModel.getVideoInfo(aid, this);
    }

    public void getVideoSrc(int cid, String type, int flag) {
        if (mModel != null)
            mModel.getVideoSrc(cid, type, flag, this);
    }

    public void getVideoRecommend(int aid) {
        if (mModel != null)
            mModel.getVideoRecommend(aid, this);
    }

    @Override
    public void onSucceed(Object data, int flag) {
        if (mView == null)
            return;
        if (data instanceof VideoInfo) {
            mView.getVideoInfo((VideoInfo) data);
        } else if (data instanceof VideoSrc) {
            String videoUrl = ((VideoSrc) data).getDurl().get(0).getUrl();
            mView.getVideoSrc(videoUrl, flag);
        } else if (data instanceof VideoRecommend) {
            mView.getVideoRecommend((VideoRecommend) data);
        }
    }

    @Override
    public void onError(String error) {
        if (mView != null)
            mView.showToast(error);
    }
}
