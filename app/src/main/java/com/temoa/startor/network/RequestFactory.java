package com.temoa.startor.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.temoa.startor.beans.VideoInfoBean;
import com.temoa.startor.beans.VideoUrlBean;
import com.temoa.startor.beans.VideosBean;
import com.temoa.startor.callback.ICallback;

/**
 * Created by Temoa
 * on 2016/8/30 17:56
 */
public class RequestFactory {

    /**
     * 返回UP主上传视频请求体
     * @param mid UP主号
     * @param page 某页面
     * @param callback 回调
     * @return GsonRequest
     */
    public static GsonRequest<VideosBean> getVideoList(int mid, int page, final ICallback<VideosBean> callback) {
        String url = FormatUrl.getVideoListUrl(mid, page);
        final GsonRequest<VideosBean> request = new GsonRequest<>(url, VideosBean.class, new Response.Listener<VideosBean>() {
            @Override
            public void onResponse(VideosBean response) {
                if (response.isStatus())
                    callback.onNetworkSucceed(response);
                else
                    callback.onNetworkError("返回数据失败: status -> false");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onNetworkError(VolleyErrorHelper.getMessage(error));
            }
        });
        request.setShouldCache(true);
        return request;
    }

    /**
     * 返回视频信息请求体
     * @param aid AV号
     * @param callback 回调
     * @return GsonRequest
     */
    public static GsonRequest<VideoInfoBean> getVideoInfo(int aid, final ICallback<VideoInfoBean> callback) {
        String url = FormatUrl.getVideoInfoUrl(aid);
        GsonRequest<VideoInfoBean> request = new GsonRequest<>(url, VideoInfoBean.class, new Response.Listener<VideoInfoBean>() {
            @Override
            public void onResponse(VideoInfoBean response) {
                if (response != null)
                    callback.onNetworkSucceed(response);
                else
                    callback.onNetworkError("返回数据失败: status -> false");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onNetworkError(VolleyErrorHelper.getMessage(error));
            }
        });
        request.setShouldCache(true);
        return request;
    }

    /**
     * 返回视频播放信息请求体
     * @param cid CV号
     * @param type mp4 or flv
     * @param callback 回调
     * @return GsonRequest
     */
    public static GsonRequest<VideoUrlBean> getVideoPlay(int cid, String type, final ICallback<VideoUrlBean> callback) {
        String url = FormatUrl.getVideoPlayUrl(cid, type);
        GsonRequest<VideoUrlBean> request = new GsonRequest<>(url, VideoUrlBean.class, new Response.Listener<VideoUrlBean>() {
            @Override
            public void onResponse(VideoUrlBean response) {
                if (response != null)
                    callback.onNetworkSucceed(response);
                else
                    callback.onNetworkError("返回数据失败: status -> false");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onNetworkError(VolleyErrorHelper.getMessage(error));
            }
        });
        request.setShouldCache(true);
        return request;
    }
}
