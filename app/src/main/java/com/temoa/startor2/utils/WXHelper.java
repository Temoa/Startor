package com.temoa.startor2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Temoa
 * on 2016/10/27 20:29
 */

public class WXHelper {

    public static IWXAPI wxApi;

    public static void initApi(Context context, String wxAppId) {
        wxApi = WXAPIFactory.createWXAPI(context, wxAppId, false);
        wxApi.registerApp(wxAppId);
    }

    /**
     * @param isShareFriend true 分享给好友 false 分享到朋友圈
     */
    public static void share2Wx(final boolean isShareFriend,
                                final String target, final String title,
                                final String desc, final String thumb) {
        if (TextUtils.isEmpty(target)
                || TextUtils.isEmpty(title)
                || TextUtils.isEmpty(desc)
                || TextUtils.isEmpty(thumb))
            return;

        Observable<SendMessageToWX.Req> observable = Observable.create(new Observable.OnSubscribe<SendMessageToWX.Req>() {
            @Override
            public void call(Subscriber<? super SendMessageToWX.Req> subscriber) {
                if (subscriber.isUnsubscribed())
                    return;

                try {
                    WXWebpageObject webPage = new WXWebpageObject();
                    webPage.webpageUrl = target;

                    WXMediaMessage msg = new WXMediaMessage(webPage);
                    msg.title = title;
                    msg.description = desc;
                    Bitmap bitmap = loadBitmap(thumb);
                    msg.thumbData = getBitmapBytes(bitmap);
                    bitmap.recycle();

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = isShareFriend
                            ? SendMessageToWX.Req.WXSceneSession
                            : SendMessageToWX.Req.WXSceneTimeline;

                    subscriber.onNext(req);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SendMessageToWX.Req>() {
                    @Override
                    public void call(SendMessageToWX.Req req) {
                        wxApi.sendReq(req);
                    }
                });

    }

    // 检查是否安装了微信, 微信是否支持当前版本的API
    public static boolean checkWxApp() {
        if (!wxApi.isWXAppInstalled())
            return true;

        if (!wxApi.isWXAppSupportAPI())
            return true;

        return false;
    }

    // 加载网络图片
    private static Bitmap loadBitmap(String url) {
        URL fileUrl;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Bitmap -> byte[]
    private static byte[] getBitmapBytes(Bitmap sourceBitmap) {
        Bitmap bitmap = Bitmap.createScaledBitmap(sourceBitmap, DisplayUtils.dpToPx(80), DisplayUtils.dpToPx(60), true);
        byte[] bytes = compressBitmap(bitmap, 30);
        sourceBitmap.recycle();
        bitmap.recycle();
        return bytes;
    }

    // 压缩图片到指定大小
    private static byte[] compressBitmap(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于maxSize kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        return baos.toByteArray();
    }

    // 创建唯一标识
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
