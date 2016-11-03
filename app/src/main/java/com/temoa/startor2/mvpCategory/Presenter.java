package com.temoa.startor2.mvpCategory;

import android.content.Context;

import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.listener.UpVideosWithPagesListener;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/12 20:11
 */

public class Presenter implements UpVideosWithPagesListener {

    public static final int FLAG_MORE_DATA = 1;
    public static final int FLAG_NO_MODE_DATA = 0;
    public static final int FLAG_ERROR = -1;

    private IView mView;
    private IModel mModel;
    private Context mContext;

    public Presenter(Context context, IView iView) {
        mContext = context;
        mView = iView;
        mModel = new ModelImpl();
    }

    public void onCreate(String type) {
        if (mModel != null)
            mModel.getData(type, this);
    }

    public void onDestroy() {
        if (mContext != null)
            mContext = null;

        if (mView != null)
            mView = null;

        if (mModel != null)
            mModel = null;
    }

    public void getMoreData(String videoCategory, int page) {
        if (mModel != null)
            mModel.getMoreData(videoCategory, page, this);
    }


    @Override
    public void onSucceed(List<VideoList> data, int flag) {
        if (mView == null)
            return;

        switch (flag) {
            case ModelImpl.NEW_DATA:
                mView.getData(data);
                break;
            case ModelImpl.ADD_DATA:
                mView.getMoreData(data);
                if (data.size() == 0)
                    mView.changeProgressBarStatus(FLAG_NO_MODE_DATA);
                else
                    mView.changeProgressBarStatus(FLAG_MORE_DATA);
                break;
        }
    }

    @Override
    public void onError(String error) {
        if (mView != null){
            mView.showToast(error);
            mView.changeProgressBarStatus(FLAG_ERROR);
        }
    }

    @Override
    public void getPages(int totallyPage) {
        if (mView != null) {
            mView.getPages(totallyPage);
        }
    }
}
