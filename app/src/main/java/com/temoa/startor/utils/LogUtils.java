package com.temoa.startor.utils;

import android.util.Log;

import com.temoa.startor.BuildConfig;

/**
 * Created by Temoa
 * on 2016/9/6 14:07
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    private static boolean isDebug = BuildConfig.DEBUG;

    private LogUtils() {
        
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
