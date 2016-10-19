package com.temoa.startor2.network;

import com.temoa.startor2.Constants;
import com.temoa.startor2.beans.UpVideos;
import com.temoa.startor2.beans.UpVideos.Data.VideoList;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Temoa
 * on 2016/9/21 12:57
 */

public class DataHelper {

    private static List<UpVideos> baseList;

    /**
     * 获取分栏视频
     */
    public static List<VideoList> getSectionData(List<UpVideos> originList) {
        List<VideoList> list = new ArrayList<>();
        List<VideoList> hotList = getHotData(originList);
        List<VideoList> newList = getNewestData(originList);
        if (hotList.size() < 4 || newList.size() < 4)
            return null;
        for (int i = 0; i < 8; i++) {
            if (i < 4)
                list.add(hotList.get(i));
            else
                list.add(newList.get(i - 4));
        }
        return list;
    }

    /**
     * 获取首页展示的3个视频
     */
    public static List<VideoList> getRollPagerData(List<UpVideos> originList) {
        List<VideoList> list = getOtherData(originList, 3);
        return list;
    }

    /**
     * 获取某一作者的视频
     *
     * @param originList 原始集合
     * @param author     作者名字
     */
    public static List<VideoList> getDataByAuthor(List<UpVideos> originList, String author) {
        List<VideoList> list = new ArrayList<>();
        for (UpVideos bean : originList) {
            for (VideoList entity : bean.getData().getVlist()) {
                if (entity.getAuthor().equals(author)) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 获取某一类别视频,如"主播真会玩","青铜修炼手册"等
     *
     * @param originList 原始集合
     * @param category   类别
     */
    public static List<VideoList> getDataByCategory(List<UpVideos> originList, String category) {
        List<VideoList> targetList = new ArrayList<>();
        switch (category) {
            case Constants.REAL_PLAY:
                List<VideoList> list1 = getDataByAuthor(originList, Constants.boss);
                for (VideoList entity : list1) {
                    if (entity.getTitle().contains(category))
                        targetList.add(entity);
                }
                return targetList;
            case Constants.LOL_TOP_10:
            case Constants.PUPIL_PLAY:
            case Constants.LYING_WIN:
                List<VideoList> list2 = getDataByAuthor(originList, Constants.girl);
                for (VideoList entity : list2) {
                    if (entity.getTitle().contains(category))
                        targetList.add(entity);
                }
                return targetList;
            case Constants.OW_TOP_10:
                List<VideoList> list3 = getDataByAuthor(originList, Constants.boy);
                for (VideoList entity : list3) {
                    // 我也是醉了,小D标题改来改去的
                    String title = entity.getTitle();
                    if (title.contains(category) || title.contains(Constants.OW_TOP_10_2))
                        targetList.add(entity);
                }
                return targetList;
            default:
                for (UpVideos bean : originList) {
                    for (VideoList entity : bean.getData().getVlist()) {
                        String title = entity.getTitle();
                        boolean other = !(title.contains(Constants.REAL_PLAY) ||
                                title.contains(Constants.LOL_TOP_10) ||
                                title.contains(Constants.PUPIL_PLAY) ||
                                title.contains(Constants.LYING_WIN) ||
                                title.contains(Constants.OW_TOP_10) ||
                                title.contains(Constants.OW_TOP_10_2));
                        if (other)
                            targetList.add(entity);
                    }
                }
                return targetList;
        }
    }

    /**
     * 获取热门视频
     *
     * @param originList 原始集合
     */
    public static List<VideoList> getHotData(List<UpVideos> originList) {
        List<VideoList> list = new ArrayList<>();
        try {
            list.add(getDataByCategory(originList, Constants.REAL_PLAY).get(0));
            list.add(getDataByCategory(originList, Constants.LOL_TOP_10).get(0));
            list.add(getDataByCategory(originList, Constants.OW_TOP_10).get(0));
            list.add(getDataByCategory(originList, Constants.PUPIL_PLAY).get(0));
        } catch (Exception e) {
            int i = 1;
            while (list.size() < 4) {
                list.add(getDataByAuthor(originList, Constants.boss).get(i));
                i++;
            }

        }
        return list;
    }

    /**
     * 获取最新视频(每个UP主的一个新视频)
     *
     * @param originList 原始集合
     */
    public static List<VideoList> getNewestData(List<UpVideos> originList) {
        List<VideoList> list = new ArrayList<>();
        List<VideoList> bossList = getDataByAuthor(originList, Constants.boss);
        list.add(bossList.get(0));
        list.add(bossList.get(1));
        list.add(getDataByAuthor(originList, Constants.boy).get(0));
        list.add(getDataByAuthor(originList, Constants.girl).get(0));
        return list;
    }

    /**
     * 获取一定量的其他视频(没有定义分类的其他视频)
     *
     * @param originList 原始集合
     * @param size       个数
     */
    public static List<VideoList> getOtherData(List<UpVideos> originList, int size) {
        List<VideoList> list = new ArrayList<>();
        List<VideoList> otherList = getDataByCategory(originList, Constants.OTHER_VIDEO);
        for (int i = 0; i < size; i++) {
            list.add(otherList.get(i));
        }
        return list;
    }

    public static List<UpVideos> getBaseList() {
        return baseList;
    }

    public static void setBaseList(List<UpVideos> baseList) {
        DataHelper.baseList = baseList;
    }
}
