package com.temoa.startor.beans;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/9/6 12:37
 */
public class VideoUrlBean {

    /**
     * from : local
     * result : suee
     * format : hdmp4
     * timelength : 608823
     * accept_format : mp4,hdmp4
     * accept_quality : [2,1]
     * seek_param : start
     * seek_type : second
     * durl : [{"order":1,"length":608823,"size":95548569,"url":"http://cn-gdsz-cmcc-v-02.acgvideo.com/vg7/4/d3/9948237-1-hd.mp4?expires=1473165300&ssig=JmQjx_5tCvuL59P40ZDaMA&oi=3746201865&rate=0","backup_url":["http://cn-sccd2-cmcc.acgvideo.com/vg7/4/24/9948237-1-hd.mp4?expires=1473165300&ssig=-lx5ccUV8G9WCQVdhdxjYg&oi=3746201865&rate=0","http://cn-jsnj3-cmcc.acgvideo.com/vg4/f/86/9948237-1-hd.mp4?expires=1473165300&ssig=3yEFo6Wd_SteCYeD-hZChw&oi=3746201865&rate=0"]}]
     */

    private String from;
    private String result;
    private String format;
    private int timelength;
    private String accept_format;
    private String seek_param;
    private String seek_type;
    private List<Integer> accept_quality;
    /**
     * order : 1
     * length : 608823
     * size : 95548569
     * url : http://cn-gdsz-cmcc-v-02.acgvideo.com/vg7/4/d3/9948237-1-hd.mp4?expires=1473165300&ssig=JmQjx_5tCvuL59P40ZDaMA&oi=3746201865&rate=0
     * backup_url : ["http://cn-sccd2-cmcc.acgvideo.com/vg7/4/24/9948237-1-hd.mp4?expires=1473165300&ssig=-lx5ccUV8G9WCQVdhdxjYg&oi=3746201865&rate=0","http://cn-jsnj3-cmcc.acgvideo.com/vg4/f/86/9948237-1-hd.mp4?expires=1473165300&ssig=3yEFo6Wd_SteCYeD-hZChw&oi=3746201865&rate=0"]
     */

    private List<DurlEntity> durl;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getTimelength() {
        return timelength;
    }

    public void setTimelength(int timelength) {
        this.timelength = timelength;
    }

    public String getAccept_format() {
        return accept_format;
    }

    public void setAccept_format(String accept_format) {
        this.accept_format = accept_format;
    }

    public String getSeek_param() {
        return seek_param;
    }

    public void setSeek_param(String seek_param) {
        this.seek_param = seek_param;
    }

    public String getSeek_type() {
        return seek_type;
    }

    public void setSeek_type(String seek_type) {
        this.seek_type = seek_type;
    }

    public List<Integer> getAccept_quality() {
        return accept_quality;
    }

    public void setAccept_quality(List<Integer> accept_quality) {
        this.accept_quality = accept_quality;
    }

    public List<DurlEntity> getDurl() {
        return durl;
    }

    public void setDurl(List<DurlEntity> durl) {
        this.durl = durl;
    }

    public static class DurlEntity {
        private int order;
        private int length;
        private int size;
        private String url;
        private List<String> backup_url;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getBackup_url() {
            return backup_url;
        }

        public void setBackup_url(List<String> backup_url) {
            this.backup_url = backup_url;
        }
    }
}
