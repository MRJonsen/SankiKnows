package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import com.zc.pickuplearn.beans.CircleRankingBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 16:30
 * 联系方式：chenbin252@163.com
 */

public interface IProCircleDetailView {
    /**
     * 圈内详细排名数据
     * @param list
     */
    void addData(List<CircleRankingBean> list);
    /**
     * 设置加入圈子按钮状态（）
     */
    void setJoinButton(boolean isJoin);
}
