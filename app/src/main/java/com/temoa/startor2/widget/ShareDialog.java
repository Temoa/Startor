package com.temoa.startor2.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.temoa.startor2.R;
import com.temoa.startor2.utils.DisplayUtils;
import com.temoa.startor2.utils.ToastUtils;
import com.temoa.startor2.utils.WXHelper;

/**
 * Created by Temoa
 * on 2016/10/28 14:49
 */

public class ShareDialog extends Dialog implements View.OnClickListener {

    private String target = "";
    private String title = "";
    private String desc = "";
    private String thumb = "";
    private Context mContext;

    public ShareDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_share_view);

        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialogAnim);
            window.getDecorView().setPadding(
                    DisplayUtils.dpToPx(16),
                    DisplayUtils.dpToPx(16),
                    DisplayUtils.dpToPx(16),
                    DisplayUtils.dpToPx(16));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        findViewById(R.id.share_wx).setOnClickListener(this);
        findViewById(R.id.share_time_line).setOnClickListener(this);
        findViewById(R.id.share_other).setOnClickListener(this);

    }

    public void setData(String target, String title, String desc, String thumb) {
        this.target = target;
        this.title = title;
        this.desc = desc;
        this.thumb = thumb;
    }


    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(target) || TextUtils.isEmpty(title)
                || TextUtils.isEmpty(desc) || TextUtils.isEmpty(thumb)) {
            ToastUtils.showShort((Activity) mContext, mContext.getString(R.string.share_dialog_no_msg));
            dismiss();
            return;
        }

        int id = v.getId();
        switch (id) {
            case R.id.share_wx:
                if (checkWxApp())
                    return;

                dismiss();
                WXHelper.share2Wx(true, target, title, desc, thumb);
                break;
            case R.id.share_time_line:
                if (checkWxApp())
                    return;

                dismiss();
                WXHelper.share2Wx(true, target, title, desc, thumb);
                break;
            case R.id.share_other:
                dismiss();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");// 纯文本
                intent.putExtra(Intent.EXTRA_TEXT, target);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
        }
    }

    private boolean checkWxApp() {
        if (WXHelper.checkWxApp()) {
            ToastUtils.showShort((Activity) mContext, mContext.getString(R.string.share_dialog_wx_error));
            dismiss();
            return true;
        }
        return false;
    }
}
