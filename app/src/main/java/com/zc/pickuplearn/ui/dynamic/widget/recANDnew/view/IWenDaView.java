package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view;

import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.beans.QuestionBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/10 16:17
 * 联系方式：chenbin252@163.com
 */

public interface IWenDaView {
    /**
     * 获取搜索关键字
     * @return
     */
    String getSearchString();
    void  clearSearchString();

    /**
     * 获取索引
     * @return
     */
    int getIndex();
    void addData(List<QuestionBean> list);
    void disShowRefreshView();
    void loadMoreStatus(LoadingFooter.State state);
    void showEmptyView(boolean isshow);
}
