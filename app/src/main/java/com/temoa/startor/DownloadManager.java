package com.temoa.startor;

import android.os.Environment;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.temoa.startor.adapter.DownloadVideoAdapter;
import com.temoa.startor.beans.VideoTaskBean;
import com.temoa.startor.utils.LogUtils;

/**
 * Created by Temoa
 * on 2016/9/10 14:33
 */
public class DownloadManager {

    private static final String VideoSavePath
            = Environment.getExternalStoragePublicDirectory("").getAbsolutePath() + "/startor/";
    private DownloadVideoAdapter mAdapter;
    private FileDownloader mLoader;

    public DownloadManager(DownloadVideoAdapter adapter) {
        mAdapter = adapter;
        mLoader = FileDownloader.getImpl();
    }

    public void startTask(VideoTaskBean taskBean, int pos) {
        if (mAdapter == null) {
            return;
        }
        mLoader.create(taskBean.getUrl())
                .setPath(VideoSavePath + taskBean.getTitle() + ".mp4")
                .setListener(getListener(taskBean, pos))
                .start();
    }

    public int pauseTask(int id) {
        return mLoader.pause(id);
    }

    public void pauseAll() {
        mLoader.pauseAll();
    }

    public void clearTask(int pos, int id, String path) {
        if (mAdapter == null) {
            return;
        }
        mLoader.clear(id, path);
        mAdapter.remove(pos);
    }

    private FileDownloadSampleListener getListener(final VideoTaskBean taskBean, final int pos) {
        return new FileDownloadSampleListener() {
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.progress(task, soFarBytes, totalBytes);
                taskBean.setPause(false);
                taskBean.setTotalSize(totalBytes);
                taskBean.setSoFarSize(soFarBytes);
                if (mAdapter != null) {
                    mAdapter.notifyItemChanged(pos, taskBean);
                    LogUtils.d("TASK ID ---> " + task.getId() +
                            ":Loading: " + soFarBytes + "/" + totalBytes);
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                super.paused(task, soFarBytes, totalBytes);
                taskBean.setPause(true);
                taskBean.setTotalSize(totalBytes);
                taskBean.setSoFarSize(soFarBytes);
                if (mAdapter != null) {
                    mAdapter.notifyItemChanged(pos, taskBean);
                    LogUtils.d("TASK ID ---> " + task.getId() + ":Task paused");
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                super.completed(task);
                taskBean.setLoading(false);
                taskBean.setMsg(null);
                taskBean.setFilePath(task.getTargetFilePath());
                if (mAdapter != null) {
                    mAdapter.notifyItemChanged(pos, taskBean);
                    MyApplication.releaseTask(taskBean.getTaskId());
                    LogUtils.d("TASK ID ---> " + task.getId() + ":Task completed");
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                super.warn(task);
                taskBean.setMsg("等待中");
                if (mAdapter != null) {
                    mAdapter.notifyItemChanged(pos, taskBean);
                    LogUtils.d("TASK ID ---> " + task.getId() + ":Task warn");
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                super.error(task, e);
                taskBean.setMsg("下载失败");
                if (mAdapter != null) {
                    mAdapter.notifyItemChanged(pos, taskBean);
                    LogUtils.d("TASK ID ---> " + task.getId() + ":Task error");
                }
            }
        };
    }

    public void closeManger() {
        mAdapter = null;
        mLoader = null;
    }
}

