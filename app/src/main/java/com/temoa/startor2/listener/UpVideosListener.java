package com.temoa.startor2.listener;

import com.temoa.startor2.beans.UpVideos.Data.VideoList;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/12 15:24
 */

public interface UpVideosListener {
    void onSucceed(List<VideoList> data, int flag);

    void onError(String error);
}
