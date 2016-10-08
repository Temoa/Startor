package com.temoa.startor.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.temoa.startor.R;
import com.temoa.startor.beans.VideoTaskBean;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/9/8 13:27
 */
public class DownloadVideoAdapter extends BaseQuickAdapter<VideoTaskBean> {

    public DownloadVideoAdapter(int layoutResId, List<VideoTaskBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VideoTaskBean taskInfo) {
        holder.setText(R.id.item_text_title, taskInfo.getTitle())
                .addOnClickListener(R.id.item_btn_play)
                .addOnClickListener(R.id.item_btn_delete);
        int totalSize = taskInfo.getTotalSize();
        int soFarSize = taskInfo.getSoFarSize();
        TextView tv = holder.getView(R.id.item_text_status);
        ImageButton playBtn = holder.getView(R.id.item_btn_play);
        ProgressBar bar = holder.getView(R.id.item_progress_bar);
        if (taskInfo.isLoading()) {
            tv.setText(formatStatus(totalSize, soFarSize, taskInfo.getMsg()));
            if (taskInfo.isPause()){
                playBtn.setImageResource(R.drawable.ic_play_black_24);
            }else{
                playBtn.setImageResource(R.drawable.ic_pause_black_24);
            }
            bar.setVisibility(View.VISIBLE);
            bar.setMax(totalSize);
            bar.setProgress(soFarSize);
        } else {
            tv.setText(formatStatus(totalSize, totalSize, null));
            playBtn.setImageResource(R.drawable.ic_play_black_24);
            bar.setVisibility(View.GONE);
        }
    }

    private String formatStatus(int totalSize, int soFarSize, String msg) {
        if (msg != null) {
            return msg;
        } else {
            totalSize = totalSize / 1024 / 1024;
            soFarSize = soFarSize / 1024 / 1024;
            if (totalSize <= soFarSize) {
                return soFarSize + "MB/已完成";
            } else {
                return soFarSize + "MB/" + totalSize + "MB";
            }
        }
    }
}
