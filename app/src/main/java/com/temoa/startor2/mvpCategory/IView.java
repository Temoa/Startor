package com.temoa.startor2.mvpCategory;

import com.temoa.startor2.beans.UpVideos.Data.VideoList;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/12 20:11
 */

public interface IView {

    void getData(List<VideoList> data);

    void getMoreData(List<VideoList> data);

    void getPages(int pages);

    void changeProgressBarStatus(int flag);

    void showToast(String msg);
}
