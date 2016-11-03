package com.temoa.startor2.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Temoa
 * on 2016/10/11 23:10
 */

public class ToastUtils {

    public static void showShort(final Activity context, final String string) {
        //判断是否为主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } else {//如果不是，就用该方法使其在ui线程中运行
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showLong(final Activity context, final String string) {
        //判断是否为主线程
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        } else {//如果不是，就用该方法使其在ui线程中运行
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, string, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
