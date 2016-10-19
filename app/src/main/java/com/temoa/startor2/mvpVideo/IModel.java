package com.temoa.startor2.mvpVideo;

import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoSrc;
import com.temoa.startor2.listener.VideoCommonListener;

/**
 * Created by Temoa
 * on 2016/10/17 16:53
 */

public interface IModel {
    void getVideoInfo(int aid, VideoCommonListener<VideoInfo> listener);

    void getVideoSrc(int cid, String type, VideoCommonListener<VideoSrc> listener);

    void getVideoRecommend(int aid, VideoCommonListener<VideoRecommend> listener);
}
