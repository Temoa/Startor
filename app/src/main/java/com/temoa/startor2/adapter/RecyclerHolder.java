package com.temoa.startor2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Temoa
 * on 2016/8/1 18:28
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View mItemView;

    public RecyclerHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        views = new SparseArray<>(12);
    }

    public SparseArray<View> getAllViews() {
        return views;
    }

    public View getItemView() {
        return mItemView;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public RecyclerHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
        return this;
    }

    public RecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public RecyclerHolder setImageByUrl(int viewId, Context context, String url) {
        View view = getView(viewId);
        if (view instanceof ImageView)
            Glide.with(context).load(url).into((ImageView) view);
        return this;
    }

    public RecyclerHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public RecyclerHolder setOnItemChildClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecyclerHolder setOnItemClickListener(View.OnClickListener listener) {
        mItemView.setOnClickListener(listener);
        return this;
    }
}
