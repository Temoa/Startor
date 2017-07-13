package com.temoa.startor2.network;

import com.temoa.startor2.Config;
import com.temoa.startor2.utils.CommonUtils;
import com.temoa.startor2.utils.MD5Utils;

/**
 * Created by Temoa
 * on 2016/9/5 22:36
 */
public class UrlHelper {

    /**
     * 获取某个UP主的上传的视频
     *
     * @param mid  up主号
     * @param page 某页面数
     */
    public static String getUpVideos(int mid, int page) {
        return "https://space.bilibili.com/ajax/member/getSubmitVideos?mid=" +
                mid +
                "&page=" +
                page;
    }

    /**
     * 获取某视频的详细信息
     *
     * @param aid 视频AV号
     */
    public static String getVideoInfo(int aid) {
        String url = "https://api.bilibili.com/view?";
        String data = "appkey=" + Config.NEW_APPKEY +
                "&id=" + aid +
                "&page=1" +
                "&type=json";
        String md5Str = MD5Utils.strToMD5(data + Config.NEW_APPSEC);
        return url + data + "&sign=" + md5Str;
    }

    /**
     * 获取某视频的播放链接
     *
     * @param cid  视频CV号
     * @param type mp4 or flv
     */
    public static String getVideoSrc(int cid, String type) {
        String url = "https://interface.bilibili.com/playurl?";
        String data = "_appver=424000"
                + "&_device=android"
                + "&_down=0"
                + "&_hwid=" + CommonUtils.randomString(16)
                + "&_p=1"
                + "&_tid=0"
                + "&appkey=" + Config.NEW_APPKEY
                + "&cid=" + cid
                + "&otype=json"
                + "&platform=android"
                + "&quality=3"
                + "&type=" + type;
        String md5Str = MD5Utils.strToMD5(data + Config.NEW_APPSEC);
        return url + data + "&sign=" + md5Str;
    }

    /**
     * 获取相关视频
     *
     * @param aid 视频AV号
     */
    public static String getVideoRecommend(int aid) {
        return "https://api.bilibili.cn/author_recommend?aid=" + aid;
    }
}
