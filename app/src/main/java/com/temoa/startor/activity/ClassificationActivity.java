package com.temoa.startor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.RequestQueue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.temoa.startor.Constants;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.adapter.CommonVideoAdapter;
import com.temoa.startor.beans.VideosBean;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;
import com.temoa.startor.callback.ICallback;
import com.temoa.startor.network.DataFactory;
import com.temoa.startor.network.GsonRequest;
import com.temoa.startor.network.RequestFactory;
import com.temoa.startor.utils.LogUtils;
import com.temoa.startor.utils.NetUtils;
import com.temoa.startor.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ClassificationActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private CommonVideoAdapter mAdapter;
    private RecyclerView mRecycler;
    private View mRootView;

    private static String type;
    private int pages = 10;
    private int page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        mRootView = this.findViewById(android.R.id.content);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            type = bundle.getString("type");
        } else {
            if (savedInstanceState != null)
                type = savedInstanceState.getString("type");
        }
        prepareDataAtFirst();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.putString("type", type);
    }

    private void prepareDataAtFirst() {
        Observable
                .create(new Observable.OnSubscribe<List<VideosBean>>() {
                    @Override
                    public void call(Subscriber<? super List<VideosBean>> subscriber) {
                        List<VideosBean> baseData = DataFactory.getBaseList();
                        subscriber.onNext(baseData);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideosBean>>() {
                    @Override
                    public void call(List<VideosBean> baseData) {
                        List<VlistEntity> list = DataFactory.getDataByType(baseData, type);
                        initToolbar(type);
                        initView(list);
                        if (list.size() < 12)
                            getMoreVideos();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        SnackbarUtils.show(mRootView, "数据出错");
                    }
                });
    }

    private void initToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView(List<VlistEntity> dataList) {
        mRecycler = (RecyclerView) findViewById(R.id.classification_recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CommonVideoAdapter(
                this, R.layout.recycler_common_item, dataList);
        if (!type.equals(Constants.OTHER)) {
            mAdapter.openLoadMore(10);
            mAdapter.setOnLoadMoreListener(this);
        }
        View loadView = LayoutInflater.from(this).inflate(R.layout.view_load_more, mRecycler, false);
        mAdapter.setLoadingView(loadView);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addOnItemTouchListener(mItemClickListener);
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
            if (mAdapter != null) {
                VlistEntity info = mAdapter.getItem(i);
                int aid = info.getAid();
                startVideoActivity(view, aid);
            }
        }
    };

    @Override
    public void onLoadMoreRequested() {
        LogUtils.i("onLoadMoreRequested");
        if (mRecycler != null) {
            mRecycler.post(new Runnable() {
                @Override
                public void run() {
                    if (page > pages) {
                        mAdapter.loadComplete();
                    } else {
                        getMoreVideos();
                    }
                }
            });
        }
    }

    private void getMoreVideos() {
        if (!NetUtils.isNetConnection(this)) {
            SnackbarUtils.show(mRootView, "网络未连接");
            return;
        }
        RequestQueue queue = MyApplication.getQueue();
        int mid;
        switch (type) {
            case Constants.REAL_PLAY:
                mid = Constants.MID_起小点;
                break;
            case Constants.LOL_TOP_10:
            case Constants.PUPIL_PLAY:
            case Constants.LYING_WIN:
                mid = Constants.MID_长歌;
                break;
            case Constants.OW_TOP_10:
                mid = Constants.MID_起小D;
                break;
            default:
                return;
        }
        LogUtils.i("getMoreVideos: " + mid);
        GsonRequest<VideosBean> request = RequestFactory.getVideoList(mid, page, loadMoreCallback);
        queue.add(request);
        page++;
    }

    private ICallback<VideosBean> loadMoreCallback = new ICallback<VideosBean>() {
        @Override
        public void onNetworkSucceed(final VideosBean data) {
            Observable.just(data)
                    .map(new Func1<VideosBean, List<VlistEntity>>() {
                        @Override
                        public List<VlistEntity> call(VideosBean videosBean) {
                            pages = videosBean.getData().getPages();
                            List<VideosBean> newDataList = new ArrayList<>();
                            newDataList.add(data);
                            return DataFactory.getDataByType(newDataList, type);
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<VlistEntity>>() {
                        @Override
                        public void call(List<VlistEntity> entities) {
                            mAdapter.addData(entities);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            SnackbarUtils.show(mRootView, "数据出错");
                        }
                    });
        }

        @Override
        public void onNetworkError(String error) {
            SnackbarUtils.show(mRootView, error);
        }
    };

    private void startVideoActivity(View view, int aid) {
        Intent videoActivityIntent = new Intent(this, VideoActivity.class);
        Bundle videoInfoBundle = new Bundle();
        videoInfoBundle.putInt("aid", aid);
        videoActivityIntent.putExtra("videoInfo", videoInfoBundle);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(
                        this,
                        view.findViewById(R.id.item_image_view),
                        getString(R.string.trans));
        ActivityCompat.startActivity(this, videoActivityIntent, options.toBundle());
    }
}
