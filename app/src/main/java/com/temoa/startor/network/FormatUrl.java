package com.temoa.startor.network;

import com.temoa.startor.BilibiliKey;
import com.temoa.startor.utils.CommonUtils;
import com.temoa.startor.utils.MD5Utils;

/**
 * Created by Temoa
 * on 2016/9/5 22:36
 */
public class FormatUrl {

    /**
     * 获取某个UP主的上传的视频
     *
     * @param mid  up主号
     * @param page 某页面数
     * @return 链接
     */
    public static String getVideoListUrl(int mid, int page) {
        return "http://space.bilibili.com/ajax/member/getSubmitVideos?mid=" +
                mid +
                "&page=" +
                page;
    }

    /**
     * 获取某视频的详细信息
     *
     * @param aid 视频AV号
     * @return 链接
     */
    public static String getVideoInfoUrl(int aid) {
        String url = "http://api.bilibili.com/view?";
        String data = "appkey=" + BilibiliKey.APPKEY +
                "&id=" + aid +
                "&page=1" +
                "&type=json";
        String md5Str = MD5Utils.strToMD5(data + BilibiliKey.APPSEC);
        return url + data + "&sign=" + md5Str;
    }

    /**
     * 获取某视频的播放链接
     *
     * @param cid  视频CV号
     * @param type mp4 or flv
     * @return 链接
     */
    public static String getVideoPlayUrl(int cid, String type) {
        String url = "http://interface.bilibili.com/playurl?";
        String data = "_appver=424000"
                + "&_device=android"
                + "&_down=0"
                + "&_hwid=" + CommonUtils.randomString(16)
                + "&_p=1"
                + "&_tid=0"
                + "&appkey=" + BilibiliKey.NEW_APPKEY
                + "&cid=" + cid
                + "&otype=json"
                + "&platform=android"
                + "&quality=3"
                + "&type=" + type;
        String md5Str = MD5Utils.strToMD5(data + BilibiliKey.NEW_APPSEC);
        return url + data + "&sign=" + md5Str;
    }
}
