package com.temoa.startor.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.temoa.startor.R;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;
import com.temoa.startor.utils.CommonUtils;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/8/30 18:15
 */
public class CommonVideoAdapter extends BaseQuickAdapter<VlistEntity> {

    private Context mAppContext;

    public CommonVideoAdapter(Context context, int layoutResId, List<VlistEntity> data) {
        super(layoutResId, data);
        mAppContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VlistEntity vlistEntity) {
        baseViewHolder.setText(R.id.item_text_title, vlistEntity.getTitle());
        baseViewHolder.setText(R.id.item_text_play, CommonUtils.formatNumber(vlistEntity.getPlay()));
        baseViewHolder.setText(R.id.item_text_likes, CommonUtils.formatNumber(vlistEntity.getFavorites()));
        ImageView iv = baseViewHolder.getView(R.id.item_image_view);
        Glide.with(mAppContext)
                .load(vlistEntity.getPic())
                .into(iv);
    }
}
