package com.temoa.startor.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.temoa.startor.Constants;
import com.temoa.startor.R;
import com.temoa.startor.activity.ClassificationActivity;
import com.temoa.startor.activity.VideoActivity;
import com.temoa.startor.beans.SectionBean;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;
import com.temoa.startor.utils.CommonUtils;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/9/21 13:46
 */

public class HomeVideoAdapter extends BaseSectionQuickAdapter<SectionBean> implements View.OnClickListener {

    private Context mAppContext;
    private Context mContext;

    public HomeVideoAdapter
            (Context context, int layoutResId, int sectionHeadResId, List<SectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
        mContext = context;
        mAppContext = mContext.getApplicationContext();
    }

    @Override
    protected void convertHead(BaseViewHolder holder, final SectionBean bean) {
        String title = bean.header;
        TextView tv = holder.getView(R.id.section_title);
        tv.setText(title);
        RollPagerView pagerView = holder.getView(R.id.section_roll_view);
        LinearLayout layout = holder.getView(R.id.section_head_btn_layout);
        switch (title) {
            case "热门推荐":
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hot_red_24, 0, 0, 0);
                final List<VlistEntity> showVList = bean.getShowVList();
                pagerView.setVisibility(View.VISIBLE);
                pagerView.setAdapter(new RollPagerAdapter(mAppContext, showVList));
                pagerView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int i) {
                        startActivity(mContext, showVList.get(i).getAid());
                    }
                });
                layout.setVisibility(View.VISIBLE);
                layout.findViewById(R.id.section_head_text_1).setOnClickListener(this);
                layout.findViewById(R.id.section_head_text_2).setOnClickListener(this);
                layout.findViewById(R.id.section_head_text_3).setOnClickListener(this);
                layout.findViewById(R.id.section_head_text_4).setOnClickListener(this);
                layout.findViewById(R.id.section_head_text_5).setOnClickListener(this);
                layout.findViewById(R.id.section_head_text_6).setOnClickListener(this);
                break;
            case "最新视频":
                pagerView.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new_blue_24, 0, 0, 0);
                break;
            default:
                pagerView.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void convert(BaseViewHolder holder, SectionBean bean) {
        VlistEntity entity = bean.t;
        holder.setText(R.id.item_text_title, entity.getTitle());
        holder.setText(R.id.item_text_play, CommonUtils.formatNumber(entity.getPlay()));
        holder.setText(R.id.item_text_likes, CommonUtils.formatNumber(entity.getFavorites()));
        ImageView iv = holder.getView(R.id.item_image_view);
        Glide.with(mAppContext)
                .load(entity.getPic())
                .into(iv);
    }

    @Override
    public void setNewData(List data) {
        super.setNewData(data);
    }

    private void startActivity(Context context, int aid) {
        Intent intent = new Intent(context, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("aid", aid);
        intent.putExtra("videoInfo", bundle);
        context.startActivity(intent);
    }

    private void startActivity(String type) {
        Intent intent = new Intent(mContext, ClassificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        intent.putExtra("bundle", bundle);
        mContext.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.section_head_text_1:
                startActivity(Constants.REAL_PLAY);
                break;
            case R.id.section_head_text_2:
                startActivity(Constants.LOL_TOP_10);
                break;
            case R.id.section_head_text_3:
                startActivity(Constants.PUPIL_PLAY);
                break;
            case R.id.section_head_text_4:
                startActivity(Constants.OW_TOP_10);
                break;
            case R.id.section_head_text_5:
                startActivity(Constants.LYING_WIN);
                break;
            case R.id.section_head_text_6:
                startActivity(Constants.OTHER);
                break;
        }
    }
}
