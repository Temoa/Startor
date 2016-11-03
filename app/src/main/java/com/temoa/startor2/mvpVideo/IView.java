package com.temoa.startor2.mvpVideo;

import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;

/**
 * Created by Temoa
 * on 2016/10/17 16:53
 */

public interface IView {
    void getVideoInfo(VideoInfo data);

    void getVideoSrc(String data, int flag);

    void getVideoRecommend(VideoRecommend data);

    void showToast(String msg);
}
