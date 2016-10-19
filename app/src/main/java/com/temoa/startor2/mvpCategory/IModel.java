package com.temoa.startor2.mvpCategory;

import com.temoa.startor2.listener.UpVideosWithPagesListener;

/**
 * Created by Temoa
 * on 2016/10/12 20:10
 */

public interface IModel {

    void getData(String videoCategory, UpVideosWithPagesListener listener);

    void getMoreData(String videoCategory, int page, UpVideosWithPagesListener listener);
}
