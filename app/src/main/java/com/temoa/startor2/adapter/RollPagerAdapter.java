package com.temoa.startor2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/11 20:09
 */

public class RollPagerAdapter extends StaticPagerAdapter {

    private Context mContext;
    private List<VideoList> mData;

    public RollPagerAdapter(Context context, List<VideoList> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {
        ImageView view = new ImageView(viewGroup.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(mContext)
                .load(mData.get(i).getPic())
                .dontAnimate()
                .into(view);
        return view;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public VideoList getData(int pos) {
        if (mData != null) {
            return mData.get(pos);
        } else {
            return null;
        }
    }
}

