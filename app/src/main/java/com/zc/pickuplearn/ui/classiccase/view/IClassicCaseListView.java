package com.zc.pickuplearn.ui.classiccase.view;

import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.beans.ClassicCaseBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 15:36
 * 联系方式：chenbin252@163.com
 */

public interface IClassicCaseListView {
    /**
     *  设置刷新控件的状态
     */
    void setRefreshProgressStatus();

    /**
     *  设置加载更多控件控制状态 Loading  TheEnd Normal
     */
    void setLoadMoreProgressStatus(LoadingFooter.State state);



    /**
     * 类型选择布局
     * @param b 是否显示
     */
    void showTypeView(boolean b);

    /**
     *  获取类型
     * @return 类型
     */
    String getTypeNow();

    /**
     * 获取数据索引条数
     * @return
     */
    int getIndex();

    /**
     * 添加数据
     * @param list
     */
    void addCaseListData(List<ClassicCaseBean> list);

    /**
     * 通知数据变更更新页面
     */
    void notifyDataChange();

}
