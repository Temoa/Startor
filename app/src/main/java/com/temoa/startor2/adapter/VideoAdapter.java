package com.temoa.startor2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.temoa.startor2.R;
import com.temoa.startor2.beans.VideoInfo;
import com.temoa.startor2.beans.VideoRecommend.video;
import com.temoa.startor2.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/17 19:32
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerHolder>
        implements View.OnClickListener {

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_COMMON = 0;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private VideoInfo mHeaderData;
    private List<video> mVideos;

    private ItemClickListener mItemClickListener;
    private ItemChildClickListener mItemChildClickListener;

    public VideoAdapter(Context context, VideoInfo headerData, List<video> listData) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mHeaderData = headerData == null ? new VideoInfo() : headerData;
        mVideos = listData == null ? new ArrayList<video>() : listData;
    }

    public interface ItemClickListener {
        void onItemClickListener(View v, int pos, video data);
    }

    public interface ItemChildClickListener {
        void onItemChildClickListener(View v);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_HEADER)
            itemView = mLayoutInflater.inflate(R.layout.video_recycler_info, parent, false);
        else
            itemView = mLayoutInflater.inflate(R.layout.video_recycler_item, parent, false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        if (position == 0) {
            holder.setText(R.id.video_title, mHeaderData.getTitle());
            holder.setText(R.id.video_play, CommonUtils.formatNumber(mHeaderData.getPlay()));
            String date = mHeaderData.getCreated_at();
            String formatDate = date != null ? date.substring(0, 11) : null;
            holder.setText(R.id.video_date, formatDate);
            holder.setText(R.id.video_desc, mHeaderData.getDescription());
            holder.setOnItemChildClickListener(R.id.video_share, this);
            holder.setOnItemChildClickListener(R.id.video_coin, this);
            holder.setOnItemChildClickListener(R.id.video_download, this);
        } else {
            final int pos = position - 1;
            final video video = mVideos.get(pos);
            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClickListener(v, pos, video);
                }
            });
            holder.setImageByUrl(R.id.item_image, mContext, video.getCover());
            holder.setText(R.id.item_title, video.getTitle());
            holder.setText(R.id.item_play, CommonUtils.formatNumber(video.getClick()));
            holder.setText(R.id.item_likes, CommonUtils.formatNumber(video.getFavorites()));
        }
    }

    @Override
    public int getItemCount() {
        return mVideos.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_COMMON;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemChildClickListener(ItemChildClickListener listener) {
        mItemChildClickListener = listener;
    }

    public void setNewHeaderData(VideoInfo data) {
        mHeaderData = data;
        notifyItemChanged(0);
    }

    public void setNewData(List<video> data) {
        mVideos = data;
        notifyItemRangeChanged(0, data.size());
    }

    @Override
    public void onClick(View v) {
        mItemChildClickListener.onItemChildClickListener(v);
    }
}
