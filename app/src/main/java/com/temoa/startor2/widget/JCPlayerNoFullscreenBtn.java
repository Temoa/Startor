package com.temoa.startor2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.temoa.startor2.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Temoa
 * on 2016/10/31 14:50
 */

public class JCPlayerNoFullscreenBtn extends JCVideoPlayerStandard {

    public ImageView fullScreenBtn;

    public JCPlayerNoFullscreenBtn(Context context) {
        super(context);
    }

    public JCPlayerNoFullscreenBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        fullScreenBtn = (ImageView) findViewById(R.id.fullscreen);
        fullScreenBtn.setVisibility(GONE);
    }
}
