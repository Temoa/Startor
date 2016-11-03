package com.temoa.startor2.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.temoa.startor2.Constants;
import com.temoa.startor2.activity.DownloadActivity;
import com.temoa.startor2.adapter.DownloadAdapter.TaskItemHolder;
import com.temoa.startor2.beans.Task;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/24 21:01
 */

public class TaskManager {

    private final static class HolderClass {
        private final static TaskManager INSTANCE = new TaskManager();
    }

    public static TaskManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private SparseArray<BaseDownloadTask> taskArray = new SparseArray<>();
    private TaskDBController mDBController;
    private List<Task> taskList;

    private FileDownloadConnectListener mConnectListener;

    private TaskManager() {
        mDBController = new TaskDBController();
        taskList = mDBController.getAllTasks();
    }

    public void addTaskForViewHolder(BaseDownloadTask task) {
        taskArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(int id) {
        taskArray.remove(id);
    }

    public void updateTaskForViewHolder(int id, TaskItemHolder holder) {
        BaseDownloadTask task = taskArray.get(id);
        if (task == null)
            return;
        task.setTag(holder);
    }

    public void releaseTasksForViewHolder() {
        taskArray.clear();
    }

    private void registerServiceConnectionListener(
            final WeakReference<DownloadActivity> activityWeakReference) {
        if (mConnectListener != null)
            FileDownloader.getImpl().removeServiceConnectListener(mConnectListener);

        mConnectListener = new FileDownloadConnectListener() {
            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null)
                    return;

                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null)
                    return;

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(mConnectListener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(mConnectListener);
        mConnectListener = null;
    }

    public void onCreate(WeakReference<DownloadActivity> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTasksForViewHolder();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public Task get(int position) {
        return taskList.get(position);
    }

    public Task getById(final int id) {
        for (Task task : taskList) {
            if (task.getId() == id)
                return task;
        }
        return null;
    }

    public Task addTask(Context context, String name, String url) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url))
            return null;

        //String path = formatPath(name);
        String path = getDiskCacheVideoDir(context, name);

        int id = FileDownloadUtils.generateId(url, path);
        Task task = getById(id);
        if (task != null)
            return task;

        Task newTask = mDBController.addTask(name, url, path);
        if (newTask != null)
            taskList.add(0, newTask);
        return newTask;
    }

    public void removeTask(Task task) {
        if (task == null)
            return;

        if (taskList != null)
            taskList.remove(task);

        if (mDBController != null) {
            mDBController.removeTask(task.getId());
        }
    }

    public boolean isDownload(int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotalBytes(int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFarBytes(int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCount() {
        return taskList.size();
    }

    public String formatPath(String name) {
        if (TextUtils.isEmpty(name))
            return Constants.DEFAULT_PATH + String.valueOf(System.currentTimeMillis()) + ".mp4";
        return Constants.DEFAULT_PATH + name + ".mp4";
    }

    /**
     * 获取缓存视频文件路径
     * 如果SD卡存在，或者不可移除，调用getExternalCacheDir()方法来获取缓存路径
     * 否侧调用getCacheDir()方法来获取缓存路径
     *
     * @return 文件路径
     */
    public String getDiskCacheVideoDir(Context context, String name) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + "video" + File.separator + name + ".mp4";
    }
}
