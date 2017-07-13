package com.temoa.startor2.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.temoa.startor2.Constants;
import com.temoa.startor2.R;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;
import com.temoa.startor2.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/11 20:30
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerHolder> implements View.OnClickListener {

    private final int TYPE_COMMON_VIEW = 0;
    private final int TYPE_CHOICE_VIEW = 1;
    private final int TYPE_SECTION_VIEW = 2;

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<VideoList> mData;
    private RecyclerView mRecyclerView;

    private ItemClickListener mItemClickListener;
    private ItemChildClickListener mItemChildClickListener;


    public interface ItemClickListener {
        void onItemClickListener(View view, int pos, VideoList video);
    }

    public interface ItemChildClickListener {
        void onItemChildClickListener(View view, String type);
    }

    public HomeAdapter(Context context, RecyclerView recyclerView, List<VideoList> data) {
        mContext = context;
        mRecyclerView = recyclerView;
        mLayoutInflater = LayoutInflater.from(context);
        mData = data == null ? new ArrayList<VideoList>() : data;
    }


    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_CHOICE_VIEW:
                itemView = mLayoutInflater.inflate(R.layout.main_choice, parent, false);
                break;
            case TYPE_SECTION_VIEW:
                itemView = mLayoutInflater.inflate(R.layout.main_section, parent, false);
                break;
            default:
                itemView = mLayoutInflater.inflate(R.layout.main_item, parent, false);
        }
        return new RecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        final GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isSectionPosition(position)) ? layoutManager.getSpanCount() : 1;
            }
        });
        switch (position) {
            case 0:
                // 上部选择区
                holder.setOnItemChildClickListener(R.id.choice_1, this);
                holder.setOnItemChildClickListener(R.id.choice_2, this);
                holder.setOnItemChildClickListener(R.id.choice_3, this);
                holder.setOnItemChildClickListener(R.id.choice_4, this);
                holder.setOnItemChildClickListener(R.id.choice_5, this);
                holder.setOnItemChildClickListener(R.id.choice_6, this);
                break;
            case 1:
                // 分栏
                TextView sectionTitle1 = holder.getView(R.id.section_title);
                sectionTitle1.setText(R.string.main_adapter_hot_section);
                sectionTitle1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_item_hot_24dp, 0, 0, 0);
                break;
            case 6:
                // 分栏
                TextView sectionTitle2 = holder.getView(R.id.section_title);
                sectionTitle2.setText(R.string.main_adapter_new_section);
                sectionTitle2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_item_new_24dp, 0, 0, 0);
                break;
            default:
                // 视频item
                if (position < 6) {
                    int i = position - 2;
                    bindVideoHolder(holder, i);
                } else {
                    int i = position - 3;
                    bindVideoHolder(holder, i);
                }
                break;
        }
    }

    private void bindVideoHolder(final RecyclerHolder holder, final int i) {
        if (mData.size() == 0)
            return;
        final VideoList video = mData.get(i);
        String picUrl = "https:" + video.getPic();
        holder.setImageByUrl(R.id.main_img, mContext, picUrl);
        holder.setText(R.id.mian_title, video.getTitle());
        holder.setText(R.id.main_play, CommonUtils.formatNumber(video.getPlay()));
        holder.setText(R.id.main_like, CommonUtils.formatNumber(video.getFavorites()));
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClickListener(v, i, video);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_CHOICE_VIEW;
            case 1:
            case 6:
                return TYPE_SECTION_VIEW;
            default:
                return TYPE_COMMON_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        // 还包含另外三个不同item
        return 8 + 3;
    }

    public void setNewData(List<VideoList> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addOnItemChildClickListener(ItemChildClickListener listener) {
        mItemChildClickListener = listener;
    }

    private boolean isSectionPosition(int position) {
        switch (position) {
            case 0:
            case 1:
            case 6:
                return true;
            default:
                return false;
        }
    }

    /**
     * 上部选择View点击事件
     */
    @Override
    public void onClick(View v) {
        if (mItemChildClickListener == null) {
            return;
        }

        int id = v.getId();
        switch (id) {
            case R.id.choice_1:
                mItemChildClickListener.onItemChildClickListener(v, Constants.REAL_PLAY);
                break;
            case R.id.choice_2:
                mItemChildClickListener.onItemChildClickListener(v, Constants.LOL_TOP_10);
                break;
            case R.id.choice_3:
                mItemChildClickListener.onItemChildClickListener(v, Constants.PUPIL_PLAY);
                break;
            case R.id.choice_4:
                mItemChildClickListener.onItemChildClickListener(v, Constants.OW_TOP_10);
                break;
            case R.id.choice_5:
                mItemChildClickListener.onItemChildClickListener(v, Constants.LYING_WIN);
                break;
            case R.id.choice_6:
                mItemChildClickListener.onItemChildClickListener(v, Constants.OTHER_VIDEO);
                break;
            default:
                throw new RuntimeException("Unsupported thi view used");
        }
    }
}
