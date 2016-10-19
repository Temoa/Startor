package com.temoa.startor2.mvpMain;

import com.temoa.startor2.beans.UpVideos;
import com.temoa.startor2.listener.UpVideosListener;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/12 15:38
 */

public interface IModel {
    void getAllData(int flag, UpVideosListener listener);

    void getPageData(UpVideosListener listener, List<UpVideos> origin);

    void getSectionData(UpVideosListener listener, List<UpVideos> origin);
}
