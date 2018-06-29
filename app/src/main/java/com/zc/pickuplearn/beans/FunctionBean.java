package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 * 拾学首页功能模型
 * Created by chenbin on 2017/8/8.
 */

public class FunctionBean implements Serializable{
    public int icon;
    /**
     * SYSCREATORID : 66669999
     * FUNCTIONCODE : -1
     * CLICKRATE :
     * SEQKEY : 14
     * IS_SPREAD :
     * REMARK :
     * SYSUPDATORID :
     * SYSCOMPANYID : 50152150
     * SERVICEAREA : 33000001
     * USERCODE : 1234567788
     * SYSUPDATEDATE :
     * SORT :
     * USERNAME :
     * SYSDEPTID : 50152150
     * SYSCREATEDATE : 2017-08-09 00:00:00.0
     * FUNCTIONNAME : 功能清单
     */

    private String FUNCTIONCODE;
    private String SEQKEY;
    private String USERCODE;
    private String SORT;
    private String USERNAME;
    private String FUNCTIONNAME;
    /**
     * FUNCTIONURL : ,2017-08-08\df550875-017d-4d0e-a4ae-ba6f168ee308.png
     * CLICKRATE :
     * USERCLICKRATE :
     * IS_SPREAD : 0
     */

    private String FUNCTIONURL;
    /**
     * SORT : 5
     * CLICKRATE :
     * USERCLICKRATE :
     * IS_SPREAD : 0
     */

    private String IS_SPREAD;

    public FunctionBean(int icon, String functionname) {
        this.icon = icon;
        this.FUNCTIONNAME = functionname;
    }

    public FunctionBean() {
    }

    public String getFUNCTIONCODE() {
        return FUNCTIONCODE;
    }

    public void setFUNCTIONCODE(String FUNCTIONCODE) {
        this.FUNCTIONCODE = FUNCTIONCODE;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE;
    }

    public String getSORT() {
        return SORT;
    }

    public void setSORT(String SORT) {
        this.SORT = SORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getFUNCTIONNAME() {
        return FUNCTIONNAME;
    }

    public void setFUNCTIONNAME(String FUNCTIONNAME) {
        this.FUNCTIONNAME = FUNCTIONNAME;
    }

    public String getFUNCTIONURL() {
        return FUNCTIONURL;
    }

    public void setFUNCTIONURL(String FUNCTIONURL) {
        this.FUNCTIONURL = FUNCTIONURL;
    }

    public String getIS_SPREAD() {
        return IS_SPREAD;
    }

    public void setIS_SPREAD(String IS_SPREAD) {
        this.IS_SPREAD = IS_SPREAD;
    }
}
