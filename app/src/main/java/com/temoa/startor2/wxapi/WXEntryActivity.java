package com.temoa.startor2.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.temoa.startor2.R;
import com.temoa.startor2.utils.ToastUtils;
import com.temoa.startor2.utils.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by Temoa
 * on 2016/10/27 20:07
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXHelper.wxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXHelper.wxApi.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调该方法
    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调该方法
    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            ToastUtils.showShort(this, getString(R.string.share_succeed));
        } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            ToastUtils.showShort(this, getString(R.string.share_cancel));
        } else {
            ToastUtils.showShort(this, getString(R.string.share_failure));
        }
        finish();
    }
}
