package com.temoa.startor2.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/11 22:58
 */

public class UpVideos implements Serializable {

    private static final long serialVersionUID = 185262901854679660L;
    private boolean status;
    private Data data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data implements Serializable {

        private static final long serialVersionUID = 7174121816840319937L;
        private int count;
        private int pages;
        private List<VideoList> vlist;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<VideoList> getVlist() {
            return vlist;
        }

        public void setVlist(List<VideoList> vlist) {
            this.vlist = vlist;
        }

        public static class VideoList implements Serializable {

            private static final long serialVersionUID = -839486226703000307L;
            private int aid;
            private String copyright;
            private int typeid;
            private String title;
            private String subtitle;
            private int play;
            private int review;
            private int video_review;
            private int favorites;
            private int mid;
            private String author;
            private String description;
            private String created;
            private String pic;
            private int comment;
            private String length;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getCopyright() {
                return copyright;
            }

            public void setCopyright(String copyright) {
                this.copyright = copyright;
            }

            public int getTypeid() {
                return typeid;
            }

            public void setTypeid(int typeid) {
                this.typeid = typeid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public int getPlay() {
                return play;
            }

            public void setPlay(int play) {
                this.play = play;
            }

            public int getReview() {
                return review;
            }

            public void setReview(int review) {
                this.review = review;
            }

            public int getVideo_review() {
                return video_review;
            }

            public void setVideo_review(int video_review) {
                this.video_review = video_review;
            }

            public int getFavorites() {
                return favorites;
            }

            public void setFavorites(int favorites) {
                this.favorites = favorites;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getComment() {
                return comment;
            }

            public void setComment(int comment) {
                this.comment = comment;
            }

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }
        }
    }
}
