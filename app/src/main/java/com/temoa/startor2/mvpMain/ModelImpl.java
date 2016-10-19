package com.temoa.startor2.mvpMain;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.temoa.startor2.Constants;
import com.temoa.startor2.MyApp;
import com.temoa.startor2.beans.UpVideos;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.listener.UpVideosListener;
import com.temoa.startor2.network.DataHelper;
import com.temoa.startor2.network.RequestsHelper;
import com.temoa.startor2.network.VolleyErrorHelper;

import org.afinal.simplecache.ACache;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Temoa
 * on 2016/10/12 14:56
 */

public class ModelImpl implements IModel {

    public static final int FLAG_CACHE = 0;
    public static final int FLAG_NEW_DATA = 1;

    public static final int ROLL_PAGER_DATA = 2;
    public static final int SECTION_DATA = 3;

    private RequestQueue mRequestQueue;
    private ACache mCache;

    public ModelImpl(Context context) {
        mRequestQueue = MyApp.getRequestQueue();
        mCache = ACache.get(context);
    }

    @Override
    public void getAllData(int flag, final UpVideosListener listener) {
        // 从缓存获取数据
        if (flag == FLAG_CACHE) {
            List<UpVideos> cacheData = getDataFromCache();
            if (cacheData != null) {
                DataHelper.setBaseList(cacheData);
                getPageData(listener, cacheData);
                getSectionData(listener, cacheData);
                return;
            }
        }
        // 从网络获取数据
        final List<UpVideos> baseList = new ArrayList<>();
        Observable.just(Constants.MID_boss, Constants.MID_girl, Constants.MID_boy)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer mid) {
                        mRequestQueue.add(RequestsHelper.getUpVideos(mid, 1, new Response.Listener<UpVideos>() {
                            @Override
                            public void onResponse(UpVideos response) {
                                if (response == null || !response.isStatus()) {
                                    listener.onError("返回数据出错,稍后重试");
                                    return;
                                }
                                baseList.add(response);
                                // 放进缓存
                                if (mCache != null) {
                                    int key = response.getData().getVlist().get(0).getMid();
                                    mCache.put(String.valueOf(key), response);
                                }
                                if (baseList.size() >= 3) {
                                    DataHelper.setBaseList(baseList);
                                    getPageData(listener, baseList);
                                    getSectionData(listener, baseList);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.onError(VolleyErrorHelper.getMessage(error));
                            }
                        }));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError("返回数据出错,稍后重试");
                    }
                });

    }

    private List<UpVideos> getDataFromCache() {
        List<UpVideos> cacheData = new ArrayList<>();
        if (mCache != null) {
            UpVideos boss = (UpVideos) mCache.getAsObject(String.valueOf(Constants.MID_boss));
            UpVideos boy = (UpVideos) mCache.getAsObject(String.valueOf(Constants.MID_boy));
            UpVideos girl = (UpVideos) mCache.getAsObject(String.valueOf(Constants.MID_girl));
            if (boss != null && boy != null && girl != null) {
                cacheData.add(boss);
                cacheData.add(boy);
                cacheData.add(girl);
            }
            return cacheData;
        }
        return null;
    }

    @Override
    public void getPageData(final UpVideosListener listener, List<UpVideos> origin) {
        Observable.just(origin)
                .map(new Func1<List<UpVideos>, List<VideoList>>() {
                    @Override
                    public List<VideoList> call(List<UpVideos> videos) {
                        return DataHelper.getRollPagerData(videos);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoList>>() {
                    @Override
                    public void call(List<VideoList> video) {
                        listener.onSucceed(video, ROLL_PAGER_DATA);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError("返回数据出错,稍后重试");
                    }
                });
    }

    @Override
    public void getSectionData(final UpVideosListener listener, List<UpVideos> origin) {
        Observable.just(origin)
                .map(new Func1<List<UpVideos>, List<VideoList>>() {
                    @Override
                    public List<VideoList> call(List<UpVideos> videos) {
                        return DataHelper.getSectionData(videos);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoList>>() {
                    @Override
                    public void call(List<VideoList> video) {
                        listener.onSucceed(video, SECTION_DATA);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.onError("返回数据出错,稍后重试");
                    }
                });
    }
}
