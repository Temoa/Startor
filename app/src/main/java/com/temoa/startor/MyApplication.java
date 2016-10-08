package com.temoa.startor;

import android.app.Application;
import android.util.SparseArray;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor.beans.VideoTaskBean;

/**
 * Created by Temoa
 * on 2016/8/31 12:01
 */
public class MyApplication extends Application {

    private static RequestQueue queue;
    private static SparseArray<VideoTaskBean> mTask = new SparseArray<>();

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
        FileDownloader.init(getApplicationContext());
    }

    public static RequestQueue getQueue() {
        return queue;
    }

    /**
     * 下载任务队列相关方法
     */
    public static void addTask(int id, VideoTaskBean task) {
        mTask.put(id, task);
    }

    public static SparseArray<VideoTaskBean> getTask() {
        return mTask;
    }

    public static void releaseTask(int id) {
        mTask.remove(id);
    }

    public static void releaseAllTask() {
        mTask.clear();
    }
}
