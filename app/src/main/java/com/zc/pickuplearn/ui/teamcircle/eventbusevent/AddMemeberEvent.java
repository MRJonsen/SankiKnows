package com.zc.pickuplearn.ui.teamcircle.eventbusevent;

import com.zc.pickuplearn.beans.UserBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/3/14 16:15
 * 联系方式：chenbin252@163.com
 */

public class AddMemeberEvent {
    private List<UserBean> userBeen;

    public List<UserBean> getUserBeen() {
        return userBeen;
    }

    public void setUserBeen(List<UserBean> userBeen) {
        this.userBeen = userBeen;
    }
}
