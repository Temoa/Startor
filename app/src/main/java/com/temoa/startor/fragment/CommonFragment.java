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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.temoa.startor.MyApplication;
import com.temoa.startor.R;
import com.temoa.startor.activity.VideoActivity;
import com.temoa.startor.adapter.CommonVideoAdapter;
import com.temoa.startor.beans.VideosBean;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;
import com.temoa.startor.callback.ICallback;
import com.temoa.startor.network.GsonRequest;
import com.temoa.startor.network.RequestFactory;
import com.temoa.startor.utils.NetUtils;
import com.temoa.startor.utils.SnackbarUtils;

import java.util.ArrayList;

/**
 * Created by Temoa
 * on 2016/9/21 14:29
 */

public class CommonFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private RequestQueue mQueue = MyApplication.getQueue();
    private Context mContext;
    private Activity mActivity;
    private View mRootView;
    private SwipeRefreshLayout mRefreshLayout;
    private CommonVideoAdapter mAdapter;
    private RecyclerView mRecycler;
    private int pages = 10;
    private int page = 2;
    private int type;

    public CommonFragment getInstance(int type) {
        this.type = type;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.home_content, container, false);
        mContext = container != null ? container.getContext() : null;
        initSwipeLayout();
        initRecyclerView();
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
                VlistEntity info = mAdapter.getItem(i);
                int aid = info.getAid();
                startVideoActivity(view, aid);
            }
        }
    };

    private void initRecyclerView() {
        mRecycler = (RecyclerView) mRootView.findViewById(R.id.home_recycler_view);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CommonVideoAdapter(
                mContext.getApplicationContext(),
                R.layout.recycler_common_item,
                new ArrayList<VlistEntity>());
        mAdapter.openLoadMore(pages);
        mAdapter.setOnLoadMoreListener(this);
        View loadingView = LayoutInflater.from(mContext).
                inflate(R.layout.view_load_more, mRecycler, false);
        mAdapter.setLoadingView(loadingView);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addOnItemTouchListener(mItemClickListener);
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

    private void getVideos() {
        GsonRequest<VideosBean> getVideosList = RequestFactory.getVideoList(type, 1, updateCallback);
        mQueue.add(getVideosList);
        page = 2;
    }

    private void getMoreVideos() {
        if (!NetUtils.isNetConnection(mContext)) {
            SnackbarUtils.show(mRootView, "网络未连接");
            return;
        }
        GsonRequest<VideosBean> request = RequestFactory.getVideoList(type, page, loadMoreCallback);
        mQueue.add(request);
        page++;
    }

    @Override
    public void onLoadMoreRequested() {
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

    private ICallback<VideosBean> updateCallback = new ICallback<VideosBean>() {
        @Override
        public void onNetworkSucceed(VideosBean data) {
            mAdapter.setNewData(data.getData().getVlist());
            mRecycler.scrollToPosition(0);
            pages = data.getData().getPages();
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onNetworkError(String error) {
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            SnackbarUtils.show(mRootView, error);
        }
    };

    private ICallback<VideosBean> loadMoreCallback = new ICallback<VideosBean>() {
        @Override
        public void onNetworkSucceed(VideosBean data) {
            mAdapter.addData(data.getData().getVlist());
        }

        @Override
        public void onNetworkError(String error) {
            SnackbarUtils.show(mRootView, error);
        }
    };

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
