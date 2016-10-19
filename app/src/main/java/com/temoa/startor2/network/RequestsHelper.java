package com.temoa.startor2.network;

import com.android.volley.Response;
import com.temoa.startor2.beans.UpVideos;
import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoSrc;

/**
 * Created by Temoa
 * on 2016/8/30 17:56
 */
public class RequestsHelper {

    /**
     * 获取UP主上传视频请求体
     *
     * @param mid  UP主号
     * @param page 某页面
     */
    public static GsonRequest<UpVideos> getUpVideos(
            int mid, int page, Response.Listener<UpVideos> listener, Response.ErrorListener errorListener) {
        String url = UrlHelper.getUpVideos(mid, page);
        return new GsonRequest<UpVideos>(url, UpVideos.class, listener, errorListener);
    }

    /**
     * 获取视频信息请求体
     *
     * @param aid AV号
     */
    public static GsonRequest<VideoInfo> getVideoInfo(
            int aid, Response.Listener<VideoInfo> listener, Response.ErrorListener errorListener) {
        String url = UrlHelper.getVideoInfo(aid);
        return new GsonRequest<VideoInfo>(url, VideoInfo.class, listener, errorListener);
    }

    /**
     * 获取视频播放信息请求体
     *
     * @param cid  CV号
     * @param type mp4 or flv
     */
    public static GsonRequest<VideoSrc> getVideoSrc(
            int cid, String type, Response.Listener<VideoSrc> listener, Response.ErrorListener errorListener) {
        String url = UrlHelper.getVideoSrc(cid, type);
        return new GsonRequest<VideoSrc>(url, VideoSrc.class, listener, errorListener);
    }

    /**
     * 获取相关视频请求体
     *
     * @param aid AV号
     */
    public static GsonRequest<VideoRecommend> getVideoRecommend(
            int aid, Response.Listener<VideoRecommend> listener, Response.ErrorListener errorListener) {
        String url = UrlHelper.getVideoRecommend(aid);
        return new GsonRequest<VideoRecommend>(url, VideoRecommend.class, listener, errorListener);
    }
}
