package com.temoa.startor.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.temoa.startor.Constants;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.activity.VideoActivity;
import com.temoa.startor.adapter.HomeVideoAdapter;
import com.temoa.startor.beans.SectionBean;
import com.temoa.startor.beans.VideosBean;
import com.temoa.startor.callback.ICallback;
import com.temoa.startor.network.DataFactory;
import com.temoa.startor.network.RequestFactory;
import com.temoa.startor.utils.SnackbarUtils;

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
 * on 2016/9/21 16:02
 */

public class HomeFragment extends Fragment {

    private static final int PAGE = 1;

    private RequestQueue mQueue = MyApplication.getQueue();
    private Activity mActivity;
    private Context mContext;
    private ACache mACache;
    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;
    private HomeVideoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.home_content, container, false);
        mContext = container != null ? container.getContext() : null;
        mACache = ACache.get(mContext);
        initSwipeLayout();
        initRecyclerView();
        getDataFromCache();
        getVideos();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
            if (mAdapter != null) {
                SectionBean section = (SectionBean) mAdapter.getItem(i);
                if (!section.isHeader) {
                    int aid = section.t.getAid();
                    startVideoActivity(view, aid);
                }
            }
        }
    };

    private void initRecyclerView() {
        RecyclerView recycler = (RecyclerView) mRootView.findViewById(R.id.home_recycler_view);
        recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addOnItemTouchListener(mItemClickListener);
        mAdapter = new HomeVideoAdapter(mContext, R.layout.recycler_common_item,
                R.layout.view_section_head, new ArrayList<SectionBean>());
        recycler.setAdapter(mAdapter);
    }

    private void initSwipeLayout() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.home_content_swipe_layout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVideos();
            }
        });
    }

    private void prepareData(final List<VideosBean> list) {
        Observable.just(list)
                .map(new Func1<List<VideosBean>, List<SectionBean>>() {
                    @Override
                    public List<SectionBean> call(List<VideosBean> videoBean) {
                        return DataFactory.getSectionData(videoBean);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<SectionBean>>() {
                    @Override
                    public void call(List<SectionBean> sectionBeen) {
                        mAdapter.setNewData(sectionBeen);
                        if (mRefreshLayout.isRefreshing())
                            mRefreshLayout.setRefreshing(false);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        SnackbarUtils.show(mRootView, "数据出错");
                    }
                });
    }

    private List<VideosBean> baseDataList = new ArrayList<>();

    private void getVideos() {
        baseDataList.clear();
        Observable.just(Constants.MID_起小点, Constants.MID_起小D, Constants.MID_长歌)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer mid) {
                        mQueue.add(RequestFactory.getVideoList(mid, PAGE, new ICallback<VideosBean>() {
                            @Override
                            public void onNetworkSucceed(VideosBean data) {
                                baseDataList.add(data);
                                if (mACache != null) {
                                    int key = data.getData().getVlist().get(0).getMid();
                                    mACache.put(String.valueOf(key), data);
                                }
                                if (baseDataList.size() == 3)
                                    prepareData(baseDataList);
                            }

                            @Override
                            public void onNetworkError(String error) {
                                SnackbarUtils.show(mRootView, error);
                                if (mRefreshLayout.isRefreshing())
                                    mRefreshLayout.setRefreshing(false);
                            }
                        }));
                    }
                });
    }

    private void getDataFromCache() {
        List<VideosBean> cacheDataList = new ArrayList<>();
        if (mACache != null) {
            VideosBean bossBean = (VideosBean) mACache.getAsObject(String.valueOf(Constants.MID_起小点));
            VideosBean boyBean = (VideosBean) mACache.getAsObject(String.valueOf(Constants.MID_起小D));
            VideosBean girlBean = (VideosBean) mACache.getAsObject(String.valueOf(Constants.MID_长歌));
            if (bossBean != null && boyBean != null && girlBean != null) {
                cacheDataList.add(bossBean);
                cacheDataList.add(boyBean);
                cacheDataList.add(girlBean);
                prepareData(cacheDataList);
            }
        }
    }

    private void startVideoActivity(View view, int aid) {
        Intent videoActivityIntent = new Intent(mActivity, VideoActivity.class);
        Bundle videoInfoBundle = new Bundle();
        videoInfoBundle.putInt("aid", aid);
        videoActivityIntent.putExtra("videoInfo", videoInfoBundle);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                        mActivity,
                        view.findViewById(R.id.item_image_view),
                        getString(R.string.trans));
        ActivityCompat.startActivity(mActivity, videoActivityIntent, options.toBundle());
    }
}
