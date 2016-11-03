package com.temoa.startor2.mvpCategory;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.temoa.startor2.Constants;
import com.temoa.startor2.MyApp;
import com.temoa.startor2.beans.UpVideos;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.listener.UpVideosWithPagesListener;
import com.temoa.startor2.network.DataHelper;
import com.temoa.startor2.network.RequestsHelper;
import com.temoa.startor2.network.VolleyErrorHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Temoa
 * on 2016/10/12 20:11
 */

class ModelImpl implements IModel {

    static final int NEW_DATA = 0;
    static final int ADD_DATA = 1;

    private RequestQueue mRequestQueue;

    ModelImpl() {
        mRequestQueue = MyApp.getRequestQueue();
    }

    @Override
    public void getData(final String videoCategory, final UpVideosWithPagesListener listener) {
        Observable
                .create(new Observable.OnSubscribe<List<UpVideos>>() {
                    @Override
                    public void call(Subscriber<? super List<UpVideos>> subscriber) {
                        List<UpVideos> baseList = DataHelper.getBaseList();
                        subscriber.onNext(baseList);
                        subscriber.onCompleted();
                    }
                })
                .map(new Func1<List<UpVideos>, List<VideoList>>() {
                    @Override
                    public List<VideoList> call(List<UpVideos> videos) {
                        return DataHelper.getDataByCategory(videos, videoCategory);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoList>>() {
                    @Override
                    public void call(List<VideoList> video) {
                        listener.onSucceed(video, NEW_DATA);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError("出错了");
                    }
                });

    }

    @Override
    public void getMoreData(final String videoCategory, final int page, final UpVideosWithPagesListener listener) {
        int mid;
        switch (videoCategory) {
            case Constants.REAL_PLAY:
                mid = Constants.MID_BOSS;
                break;
            case Constants.LOL_TOP_10:
            case Constants.PUPIL_PLAY:
            case Constants.LYING_WIN:
                mid = Constants.MID_GIRL;
                break;
            case Constants.OW_TOP_10:
                mid = Constants.MID_BOY;
                break;
            default:
                mid = 0;
        }

        if (mid == 0)
            return;

        mRequestQueue.add(RequestsHelper.getUpVideos(mid, page, new Response.Listener<UpVideos>() {
            @Override
            public void onResponse(UpVideos response) {
                if (response == null || !response.isStatus()) {
                    listener.onError(null);
                    return;
                }
                Observable.just(response)
                        .map(new Func1<UpVideos, List<VideoList>>() {
                            @Override
                            public List<VideoList> call(UpVideos videos) {
                                listener.getPages(videos.getData().getPages());
                                List<UpVideos> videosList = new ArrayList<>();
                                videosList.add(videos);
                                return DataHelper.getDataByCategory(videosList, videoCategory);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<VideoList>>() {
                            @Override
                            public void call(List<VideoList> video) {
                                listener.onSucceed(video, ADD_DATA);

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                listener.onError("出错了");
                            }
                        });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(VolleyErrorHelper.getMessage(error));
            }
        }));
    }
}
