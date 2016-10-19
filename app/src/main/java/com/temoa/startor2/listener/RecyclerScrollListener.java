package com.temoa.startor2.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Temoa
 * on 2016/10/14 12:16
 */

public abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener {

    // 是否向下滑
    private boolean isScrollDown;

    public abstract void loadMore();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 如果dy>0,则是向下滑
        isScrollDown = dy > 0;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) { // Recycler 停止滑动
            int lastVisibleItemPos;
            // 获取RecyclerView 的LayoutManager
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            // 获取到最后一个可见的Item
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPos = findMax(into);
            } else {
                throw new RuntimeException("Unsupported LayoutManager used");
            }
            // 获取item 的总数
            int totalItemCount = layoutManager.getItemCount();
            // 得到最后一个可见的item 为最后的item,并且是向下滑动
            if (lastVisibleItemPos >= totalItemCount - 1 && isScrollDown) {
                loadMore();
            }
        }
    }

    private int findMax(int[] into) {
        int max = into[0];
        for (int value : into) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
