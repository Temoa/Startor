package com.temoa.startor.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.temoa.startor.R;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/9/21 17:47
 */

class RollPagerAdapter extends StaticPagerAdapter {

    private Context mAppContext;
    private List<VlistEntity> info;

    RollPagerAdapter(Context context, List<VlistEntity> info) {
        mAppContext = context;
        this.info = info;
    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {
        ImageView view = new ImageView(viewGroup.getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(mAppContext.getResources().getString(R.string.trans));
        }
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(mAppContext)
                .load(info.get(i).getPic())
                .into(view);
        return view;
    }

    @Override
    public int getCount() {
        return info.size();
    }
}
