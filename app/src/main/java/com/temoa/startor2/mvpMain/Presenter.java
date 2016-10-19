package com.temoa.startor2.mvpMain;

import android.content.Context;

import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.listener.UpVideosListener;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/11 23:15
 */

public class Presenter implements UpVideosListener {

    private IView mView;
    private IModel mModel;

    public Presenter(Context context, IView iView) {
        mView = iView;
        mModel = new ModelImpl(context);
    }

    public void onCreate() {
        if (mModel != null)
            // 首次从缓存获取
            mModel.getAllData(ModelImpl.FLAG_CACHE, this);
    }

    public void onDestroy() {
        if (mView != null)
            mView = null;
        if (mModel != null)
            mModel = null;
    }


    public void getNewData() {
        if (mModel != null)
            mModel.getAllData(ModelImpl.FLAG_NEW_DATA, this);
    }

    @Override
    public void onSucceed(List<VideoList> data, int flag) {
        if (mView == null) {
            return;
        }
        switch (flag) {
            case ModelImpl.ROLL_PAGER_DATA:
                mView.getRollPagerData(data);
                break;
            case ModelImpl.SECTION_DATA:
                mView.getSectionData(data);
                break;
        }
        mView.hideProgress();
    }

    @Override
    public void onError(String error) {
        if (mView != null) {
            mView.showToast(error);
            mView.hideProgress();
        }

    }
}
