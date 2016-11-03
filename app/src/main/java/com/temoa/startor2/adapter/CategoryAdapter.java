package com.temoa.startor2.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.temoa.startor2.R;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/12 20:52
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    private static final int TYPE_FOOTER = -1;

    // footer 状态
    public static final int STATUS_PREPARE = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_EMPTY = 2;
    public static final int STATUS_DISMISS = 3;
    public static final int STATUS_ERROR = -1;
    public int loadModeStatus = STATUS_PREPARE;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<VideoList> mData;

    // 是否向下滑动
    private boolean isScrollDown;
    // 是否加载更多
    private boolean isLoadMore;
    // 是否正在加载
    private boolean isLoading = false;
    // 是否加载完毕
    private boolean isLoadCompleted = false;

    private ItemClickListener mItemClickListener;
    private LoadMoreListener mLoadMoreListener;

    public CategoryAdapter(Context context, RecyclerView recyclerView, List<VideoList> data) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data == null ? new ArrayList<VideoList>() : data;

        // 添加滑动监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 判断是否是向下滑动
                isScrollDown = dy > 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // Recycler 停止滑动
                    int lastVisibleItemPos;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    // 获取到最后一个可见的Item
                    if (layoutManager instanceof LinearLayoutManager) {
                        lastVisibleItemPos =
                                ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else {
                        throw new RuntimeException("Unsupported LayoutManager used");
                    }
                    // 获取item 的总数
                    // 得到最后一个可见的item 为最后的item, 并且是向下滑动
                    int totalItemCount = layoutManager.getItemCount();
                    if (lastVisibleItemPos >= totalItemCount - 1
                            && isScrollDown && isLoadMore && !isLoading && !isLoadCompleted) {
                        setLoadMoreStatus(STATUS_LOADING);
                        mLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public interface ItemClickListener {
        void onItemClickListener(View view, int pos, VideoList video);
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (isLoadMore && viewType == TYPE_FOOTER)
            itemView = mLayoutInflater.inflate(R.layout.category_recycler_footer, parent, false);
        else
            itemView = mLayoutInflater.inflate(R.layout.common_recycler_item, parent, false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (isLoadMore && getItemViewType(position) == TYPE_FOOTER)
            bindFooterViewHolder(holder);
        else {
            bindCommonViewHolder(holder, position);
        }

    }

    private void bindCommonViewHolder(RecyclerHolder holder, final int pos) {
        final VideoList video = mData.get(pos);
        holder.setImageByUrl(R.id.item_image, mContext, video.getPic());
        holder.setText(R.id.item_title, video.getTitle());
        holder.setText(R.id.item_play, CommonUtils.formatNumber(video.getPlay()));
        holder.setText(R.id.item_likes, CommonUtils.formatNumber(video.getFavorites()));
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClickListener(v, pos, video);
            }
        });
    }

    private void bindFooterViewHolder(RecyclerHolder holder) {
        switch (loadModeStatus) {
            case STATUS_LOADING:
                isLoading = true;
                holder.getItemView().setVisibility(View.VISIBLE);
                holder.setVisibility(R.id.footer_progress, View.VISIBLE);
                holder.setVisibility(R.id.footer_title, View.VISIBLE);
                holder.setText(R.id.footer_title, mContext.getString(R.string.category_adapter_load_more));
                break;
            case STATUS_EMPTY:
                isLoading = false;
                holder.getItemView().setVisibility(View.VISIBLE);
                holder.setVisibility(R.id.footer_progress, View.GONE);
                holder.setVisibility(R.id.footer_title, View.VISIBLE);
                holder.setText(R.id.footer_title, mContext.getString(R.string.category_adapter_end));
                break;
            case STATUS_ERROR:
                isLoading = false;
                holder.getItemView().setVisibility(View.VISIBLE);
                holder.setVisibility(R.id.footer_progress, View.GONE);
                holder.setVisibility(R.id.footer_title, View.VISIBLE);
                holder.setText(R.id.footer_title, mContext.getString(R.string.category_adapter_load_error));
                break;
            case STATUS_PREPARE:
                isLoading = false;
                holder.getItemView().setVisibility(View.INVISIBLE);
                break;
            case STATUS_DISMISS:
                isLoading = false;
                holder.getItemView().setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        // 如果加载更多:
        // 正常的item为0, 则不显示footer, 返回0
        // 正常的item不为0, 则返回mData.size() + 1
        // 如果不加载更多:
        // 返回mData.size()
        return isLoadMore ? (mData.size() == 0 ? 0 : mData.size() + 1) : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 如果加载更多且pos为最后一个, 则返回footer类型
        if (isLoadMore && position == getItemCount() - 1)
            return TYPE_FOOTER;
        else
            return super.getItemViewType(position);
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addLoadMoreListener(LoadMoreListener listener) {
        isLoadMore = true;
        mLoadMoreListener = listener;
    }

    public void setNewData(List<VideoList> data) {
        mData = data;
        notifyItemRangeChanged(0, mData.size());
    }

    public void addData(List<VideoList> data) {
        int originalSize = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(originalSize, data.size());
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public void loadCompleted() {
        isLoadCompleted = true;
        setLoadMoreStatus(STATUS_EMPTY);
    }

    public void setLoadMoreStatus(int status) {
        loadModeStatus = status;
        notifyItemChanged(getItemCount() - 1);
    }
}
