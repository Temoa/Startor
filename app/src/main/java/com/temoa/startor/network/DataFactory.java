package com.temoa.startor.network;

import com.temoa.startor.Constants;
import com.temoa.startor.beans.SectionBean;
import com.temoa.startor.beans.VideosBean;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Temoa
 * on 2016/9/21 12:57
 */

public class DataFactory {

    private static List<VideosBean> baseList;

    /**
     * 获取分栏视频集合
     */
    public static List<SectionBean> getSectionData(List<VideosBean> originList) {
        setBaseList(originList);
        List<SectionBean> list = new ArrayList<>();

        List<VlistEntity> showVList = getOtherData(originList, 3);


        list.add(new SectionBean(true, "热门推荐", showVList));
        for (int i = 0; i < 4; i++) {
            list.add(new SectionBean(getHotData(originList).get(i)));
        }

        list.add(new SectionBean(true, "最新视频", null));
        for (int i = 0; i < 4; i++) {
            list.add(new SectionBean(getNewestData(originList).get(i)));
        }

        return list;
    }

    /**
     * 获取某一作者的视频集合
     * @param originList 原始集合
     * @param author 作者名字
     */
    public static List<VlistEntity> getDataByAuthor(List<VideosBean> originList, String author) {
        List<VlistEntity> list = new ArrayList<>();
        for (VideosBean bean : originList) {
            for (VlistEntity entity : bean.getData().getVlist()) {
                if (entity.getAuthor().equals(author)) {
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 获取某一类别视频集合,如"主播真会玩","青铜修炼手册"等
     * @param originList 原始集合
     * @param type 类别
     */
    public static List<VlistEntity> getDataByType(List<VideosBean> originList, String type) {
        List<VlistEntity> targetList = new ArrayList<>();
        switch (type) {
            case Constants.REAL_PLAY:
                List<VlistEntity> list1 = getDataByAuthor(originList, Constants.boss);
                for (VlistEntity entity : list1) {
                    if (entity.getTitle().contains(type))
                        targetList.add(entity);
                }
                return targetList;
            case Constants.LOL_TOP_10:
            case Constants.PUPIL_PLAY:
            case Constants.LYING_WIN:
                List<VlistEntity> list2 = getDataByAuthor(originList, Constants.girl);
                for (VlistEntity entity : list2) {
                    if (entity.getTitle().contains(type))
                        targetList.add(entity);
                }
                return targetList;
            case Constants.OW_TOP_10:
                List<VlistEntity> list3 = getDataByAuthor(originList, Constants.boy);
                for (VlistEntity entity : list3) {
                    if (entity.getTitle().contains(type))
                        targetList.add(entity);
                }
                return targetList;
            default:
                for (VideosBean bean : originList) {
                    for (VlistEntity entity : bean.getData().getVlist()) {
                        String title = entity.getTitle();
                        boolean other = !(title.contains(Constants.REAL_PLAY) ||
                                title.contains(Constants.LOL_TOP_10) ||
                                title.contains(Constants.PUPIL_PLAY) ||
                                title.contains(Constants.LYING_WIN) ||
                                title.contains(Constants.OW_TOP_10));
                        if (other)
                            targetList.add(entity);
                    }
                }
                return targetList;
        }
    }

    /**
     * 获取热门视频集合
     * @param originList 原始集合
     */
    public static List<VlistEntity> getHotData(List<VideosBean> originList) {
        List<VlistEntity> list = new ArrayList<>();
        list.add(getDataByType(originList, Constants.REAL_PLAY).get(0));
        list.add(getDataByType(originList, Constants.LOL_TOP_10).get(0));
        list.add(getDataByType(originList, Constants.OW_TOP_10).get(0));
        list.add(getDataByType(originList, Constants.PUPIL_PLAY).get(0));
        return list;
    }

    /**
     * 获取最新视频集合(每个UP主的新视频)
     * @param originList 原始集合
     */
    public static List<VlistEntity> getNewestData(List<VideosBean> originList) {
        List<VlistEntity> list = new ArrayList<>();
        List<VlistEntity> bossList = getDataByAuthor(originList, Constants.boss);
        list.add(bossList.get(0));
        list.add(bossList.get(1));
        list.add(bossList.get(2));
        list.add(bossList.get(3));
        list.add(getDataByAuthor(originList, Constants.boy).get(0));
        list.add(getDataByAuthor(originList, Constants.girl).get(0));
        return list;
    }

    /**
     * 获取一定量的其他视频集合(没有定义分类的其他视频)
     * @param originList 原始集合
     * @param size 个数
     */
    public static List<VlistEntity> getOtherData(List<VideosBean> originList, int size) {
        List<VlistEntity> list = new ArrayList<>();
        List<VlistEntity> otherList = getDataByType(originList, Constants.OTHER);
        for (int i = 0; i < size; i++) {
            list.add(otherList.get(i));
        }
        return list;
    }

    public static List<VideosBean> getBaseList() {
        return baseList;
    }

    public static void setBaseList(List<VideosBean> baseList) {
        DataFactory.baseList = baseList;
    }
}
