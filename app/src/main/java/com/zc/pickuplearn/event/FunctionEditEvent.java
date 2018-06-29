package com.zc.pickuplearn.event;

import com.zc.pickuplearn.beans.FunctionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能编辑完成事件
 * Created by chenbin on 2017/8/15.
 */

public class FunctionEditEvent extends BaseEvent{
    public FunctionEditEvent(List<FunctionBean> datas) {
        this.datas = datas;
    }

    public List<FunctionBean> datas = new ArrayList<>();
}
