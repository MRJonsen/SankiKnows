package com.zc.pickuplearn.ui.classiccase.view.resource.view;

/**
 * 作者： Jonsen
 * 时间: 2016/12/30 15:22
 * 联系方式：chenbin252@163.com
 */

public interface IResurceView {
    void setImage(String url);

    /**
     * 设置评星级别
     */
    void setRatingLevel(String level);

    /**
     * 设置分数
     * @param score
     */
    void setScore(String score);

    /**
     * 设置标题
     * @param title
     */
    void setTitle(String title);
    void setAuthor(String author);
    void setColloctionIcon();
    void setScoreIcon();
    void changefragment(int index);

}
