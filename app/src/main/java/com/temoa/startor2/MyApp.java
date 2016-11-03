package com.temoa.startor2;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor2.utils.TaskDBController;
import com.temoa.startor2.utils.WXHelper;

/**
 * Created by Temoa
 * on 2016/10/12 14:24
 */

public class MyApp extends Application {

    private static RequestQueue sRequestQueue;
    private static TaskDBController.TasksManagerDBOpenHelper sTasksManagerDBOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());

        WXHelper.initApi(this, Config.WX_APP_ID);

        FileDownloader.init(getApplicationContext());
        sTasksManagerDBOpenHelper = new TaskDBController.TasksManagerDBOpenHelper(getApplicationContext());
    }

    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    public static TaskDBController.TasksManagerDBOpenHelper getTasksManagerDBOpenHelper() {
        return sTasksManagerDBOpenHelper;
    }
}
