package com.temoa.startor.beans;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.temoa.startor.beans.VideosBean.DataEntity.VlistEntity;

import java.util.List;

/**
 * Created by Temoa
 * on 2016/9/21 12:53
 */

public class SectionBean extends SectionEntity<VlistEntity> {

    private List<VlistEntity> showVList;

    public SectionBean(VlistEntity vlistEntity) {
        super(vlistEntity);
    }

    public SectionBean(boolean isHeader, String header, List<VlistEntity> urls) {
        super(isHeader, header);
        this.showVList = urls;
    }

    public List<VlistEntity> getShowVList() {
        return showVList;
    }

    public void setShowVList(List<VlistEntity> showVList) {
        this.showVList = showVList;
    }
}
