package com.temoa.startor2.listener;

/**
 * Created by Temoa
 * on 2016/10/14 14:54
 */

public interface UpVideosWithPagesListener extends UpVideosListener {
    /**
     * 得到Up主的上传视频总页数
     *
     * @param totallyPage 总页数
     */
    void getPages(int totallyPage);
}
