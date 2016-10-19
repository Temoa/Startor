package com.temoa.startor2.mvpVideo;

import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoSrc;
import com.temoa.startor2.listener.VideoCommonListener;

/**
 * Created by Temoa
 * on 2016/10/17 16:54
 */

public class Presenter implements VideoCommonListener {

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

    public void getVideoInfo(int aid) {
        if (mModel != null)
            mModel.getVideoInfo(aid, this);
    }

    public void getVideoSrc(int cid, String type) {
        if (mModel != null)
            mModel.getVideoSrc(cid, type, this);
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
            mView.getVideoSrc((VideoSrc) data);
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
