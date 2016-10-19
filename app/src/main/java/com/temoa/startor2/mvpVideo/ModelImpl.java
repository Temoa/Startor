package com.temoa.startor2.mvpVideo;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.temoa.startor2.MyApp;
import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend;
import com.temoa.startor2.beans.VideoSrc;
import com.temoa.startor2.listener.VideoCommonListener;
import com.temoa.startor2.network.RequestsHelper;
import com.temoa.startor2.network.VolleyErrorHelper;

/**
 * Created by Temoa
 * on 2016/10/17 16:53
 */

public class ModelImpl implements IModel {

    public static final String VIDEO_TYPE_MP4 = "mp4";
    public static final String VIDEO_TYPE_FLV = "flv";

    private RequestQueue mRequestQueue;

    public ModelImpl() {
        mRequestQueue = MyApp.getRequestQueue();
    }

    @Override
    public void getVideoInfo(int aid, final VideoCommonListener<VideoInfo> listener) {
        mRequestQueue.add(RequestsHelper.getVideoInfo(aid, new Response.Listener<VideoInfo>() {
            @Override
            public void onResponse(VideoInfo response) {
                if (response != null && response.getCid() != 0)
                    listener.onSucceed(response, 0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(VolleyErrorHelper.getMessage(error));
            }
        }));
    }

    @Override
    public void getVideoSrc(int cid, String type, final VideoCommonListener<VideoSrc> listener) {
        mRequestQueue.add(RequestsHelper.getVideoSrc(cid, type, new Response.Listener<VideoSrc>() {
            @Override
            public void onResponse(VideoSrc response) {
                if (response != null && response.getDurl().size() != 0)
                    listener.onSucceed(response, 1);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(VolleyErrorHelper.getMessage(error));
            }
        }));
    }

    @Override
    public void getVideoRecommend(int aid, final VideoCommonListener<VideoRecommend> listener) {
        mRequestQueue.add(RequestsHelper.getVideoRecommend(aid, new Response.Listener<VideoRecommend>() {
            @Override
            public void onResponse(VideoRecommend response) {
                if (response != null && response.getList().size() != 0)
                    listener.onSucceed(response, 2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(VolleyErrorHelper.getMessage(error));
            }
        }));
    }
}
