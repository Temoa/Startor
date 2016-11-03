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

    static final int FLAG_CACHE = 0;
    private static final int FLAG_NEW_DATA = 1;

    static final int ROLL_PAGER_DATA = 2;
    static final int SECTION_DATA = 3;

    private IView mView;
    private IModel mModel;
    private Context mContext;

    public Presenter(Context context, IView iView) {
        mContext = context;
        mView = iView;
        mModel = new ModelImpl(context);
    }

    public void onCreate() {
        if (mModel != null)
            // 从缓存获取
            mModel.getAllData(FLAG_CACHE, this);
    }

    public void onDestroy() {
        if (mContext != null)
            mContext = null;

        if (mView != null)
            mView = null;

        if (mModel != null) {
            mModel.close();
            mModel = null;
        }
    }


    public void getNewData() {
        if (mModel != null)
            mModel.getAllData(FLAG_NEW_DATA, this);
    }

    @Override
    public void onSucceed(List<VideoList> data, int flag) {
        if (mView == null) {
            return;
        }

        switch (flag) {
            case ROLL_PAGER_DATA:
                mView.getRollPagerData(data);
                break;
            case SECTION_DATA:
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
