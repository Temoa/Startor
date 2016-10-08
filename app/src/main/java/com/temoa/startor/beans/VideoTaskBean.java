package com.temoa.startor.beans;

/**
 * Created by Temoa
 * on 2016/9/8 14:36
 */
public class VideoTaskBean {

    private int taskId = -1;
    private String title;
    private String url = null;
    private String msg = null;
    private String filePath;
    private int totalSize = 0;
    private int soFarSize = 0;
    private boolean isPause = false;
    private boolean isLoading = false;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getSoFarSize() {
        return soFarSize;
    }

    public void setSoFarSize(int soFarSize) {
        this.soFarSize = soFarSize;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
