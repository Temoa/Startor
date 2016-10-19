package com.temoa.startor2.mvpMain;

import com.temoa.startor2.beans.UpVideos.Data.VideoList;

import java.util.List;


/**
 * Created by Temoa
 * on 2016/10/11 22:55
 */

public interface IView {

    void getSectionData(List<VideoList> newData);

    void getRollPagerData(List<VideoList> data);

    void hideProgress();

    void showToast(String msg);
}
