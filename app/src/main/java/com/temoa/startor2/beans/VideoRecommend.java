package com.temoa.startor2.beans;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/10/17 18:15
 */

public class VideoRecommend {

    /**
     * code : 0
     * list : [{"aid":6693248,"title":"起小点TOP10 VOL240 绝世武艺，剑豪施展御风真髓","cover":"http://i0.hdslb.com/bfs/archive/4388655c6c1f8dad7e7b197f08c5c4bf51c66961.jpg","click":165733,"review":5,"favorites":356,"video_review":1260},{"aid":6651929,"title":"青铜修炼手册33：你知道有一招叫隔山打牛的掌法吗？","cover":"http://i2.hdslb.com/bfs/archive/ea53e573fe4bc84ca605d1b975eb217691083c81.jpg","click":318786,"review":4,"favorites":539,"video_review":2083},{"aid":6640504,"title":"起小点TOP10 VOL239 反复横跳！复仇之矛演绎秘技！","cover":"http://i1.hdslb.com/bfs/archive/50425431d0e9d3231ce90b69a287b46db5dd1742.jpg","click":297088,"review":3,"favorites":616,"video_review":1415},{"aid":6629895,"title":"【雷米比你快】被LV100末日人机支配的恐惧","cover":"http://i0.hdslb.com/bfs/archive/5c49de729d0b27e7b2f24c62a288d2b6d283ea11.jpg","click":159512,"review":1,"favorites":523,"video_review":2060},{"aid":6615428,"title":"长歌躺赢班11：无限乱斗之水煮机器人","cover":"http://i1.hdslb.com/bfs/archive/10978c747d28f2c8e07eb7eafcc5b7287f07a3ab.jpg","click":81248,"review":4,"favorites":296,"video_review":790},{"aid":6602793,"title":"起小点TOP10 VOL238 疾风剑豪罡气护体，斩破束缚！","cover":"http://i2.hdslb.com/bfs/archive/67db066c2591120b132ca523d0701c946f966662.jpg","click":332073,"review":5,"favorites":370,"video_review":1825},{"aid":6557730,"title":"青铜修炼手册32：我开车送货走这条路有6年了，很稳！","cover":"http://i0.hdslb.com/bfs/archive/f03811c5b9bc89a3869a832a6fda52d76537d6d8.jpg","click":403251,"review":9,"favorites":869,"video_review":2447},{"aid":6540708,"title":"起小点TOP10 VOL237 绝地反击！伊泽瑞尔灵巧五杀","cover":"http://i2.hdslb.com/bfs/archive/ae9ad901697bbb5e390b8400944ee42828e513cc.jpg","click":389796,"review":5,"favorites":466,"video_review":1829},{"aid":6508064,"title":"长歌躺赢班10：国庆特别篇 丝毫不怠惰的瞎子","cover":"http://i0.hdslb.com/bfs/archive/31be9ed1703a9cc1d28bcda8f77e499d465a69e7.jpg","click":142959,"review":1,"favorites":222,"video_review":1507},{"aid":6491824,"title":"起小点TOP10 VOL236 来自深渊的惊喜，imp大嘴教你输出","cover":"http://i2.hdslb.com/bfs/archive/6a893d972a8e77979377a10ee1f55a4c0d50c442.jpg","click":381872,"review":2,"favorites":566,"video_review":1915},{"aid":6453113,"title":"青铜修炼手册31：召唤师峡谷第一预言家诞生记！","cover":"http://i1.hdslb.com/bfs/archive/11fbd7ce328fbaf791960e2795716ed26cb6b26e.jpg","click":443465,"review":8,"favorites":1270,"video_review":3136},{"aid":6441785,"title":"起小点TOP10 VOL235 狭路相逢，圣枪游侠的背水一战","cover":"http://i2.hdslb.com/bfs/archive/4f07bd9c322bee415e1e4d563c1784e63f7d6f12.jpg","click":369909,"review":8,"favorites":493,"video_review":1423},{"aid":6420484,"title":"长歌躺赢班09：锤石病中惊坐起 笑问神钩怎么输","cover":"http://i2.hdslb.com/bfs/archive/fbff7a47c6a1ff5adf7267dec64345f5860d4799.jpg","click":109177,"review":2,"favorites":367,"video_review":939},{"aid":6405736,"title":"起小点TOP10 VOL234 敏锐疾行！战争女神的极限狩猎","cover":"http://i1.hdslb.com/bfs/archive/1887b93e6b872b59764ab1b109f20ff04edabad3.jpg","click":344368,"review":6,"favorites":789,"video_review":1378},{"aid":6397249,"title":"一分五十五秒必拿一血 新英雄艾翁实战解析","cover":"http://i2.hdslb.com/bfs/archive/b3bda36681a68ff6191548e9bdca55cc7e0d7f72.jpg","click":171960,"review":3,"favorites":536,"video_review":2157},{"aid":6367205,"title":"青铜修炼手册30：机器人瞬间移动骗了你，也骗了我自己","cover":"http://i2.hdslb.com/bfs/archive/6b956bf269b5081272eb0495f2e4ec72000d8980.jpg","click":450949,"review":8,"favorites":1393,"video_review":2403}]
     */

    private int code;
    /**
     * aid : 6693248
     * title : 起小点TOP10 VOL240 绝世武艺，剑豪施展御风真髓
     * cover : http://i0.hdslb.com/bfs/archive/4388655c6c1f8dad7e7b197f08c5c4bf51c66961.jpg
     * click : 165733
     * review : 5
     * favorites : 356
     * video_review : 1260
     */

    private List<video> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<video> getList() {
        return list;
    }

    public void setList(List<video> list) {
        this.list = list;
    }

    public static class video {
        private int aid;
        private String title;
        private String cover;
        private int click;
        private int review;
        private int favorites;
        private int video_review;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public int getReview() {
            return review;
        }

        public void setReview(int review) {
            this.review = review;
        }

        public int getFavorites() {
            return favorites;
        }

        public void setFavorites(int favorites) {
            this.favorites = favorites;
        }

        public int getVideo_review() {
            return video_review;
        }

        public void setVideo_review(int video_review) {
            this.video_review = video_review;
        }
    }
}
