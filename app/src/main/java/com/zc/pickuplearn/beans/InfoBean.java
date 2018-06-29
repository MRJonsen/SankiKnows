package com.zc.pickuplearn.beans;

/**
 * 首页消息 bean
 * Created by chenbin on 2017/8/14.
 */

public class InfoBean {

    /**
     * SYSCREATORID : 1234567788
     * MES_STATE :
     * SEQKEY : 1
     * MSG_CONTENT : 七言绝句特辣恶魔
     * REMARK :
     * SERVICEAREA : 33000001
     * SYSCOMPANYID : 50152150
     * SYSUPDATORID :
     * USERCODE :
     * SYSUPDATEDATE :
     * MSG_TITLE : 陈冰123创建了新团队【测试首页消息】
     * EXTRASPARAM :
     * FORKEY : 6577
     * USERNAME :
     * SYSDEPTID : 50152150
     * MES_TYPE : 2
     * SYSCREATEDATE : 2017-08-11 11:36:01.0
     */

    private String SEQKEY;
    private String MSG_CONTENT;
    private String MSG_TITLE;
    private String EXTRASPARAM;
    private String FORKEY;
    private String MES_TYPE;

    public String getMES_NAME() {
        return MES_NAME;
    }

    public void setMES_NAME(String MES_NAME) {
        this.MES_NAME = MES_NAME;
    }

    private String MES_NAME;
    private String SYSCREATEDATE;

    private String FILEURL;
    /**
     * SYSCREATORID : 66010001
     * MES_STATE : 1
     * REMARK :
     * SERVICEAREA : 66660000
     * SYSCOMPANYID : 66660000
     * SYSUPDATORID :
     * SYSUPDATEDATE :
     * USERCODE : pengj
     * USERNAME :
     * SYSDEPTID : 66660001
     * URLPATH : /ZUI/pages/grow/onlineExam.html
     */

    private String URLPATH;

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getMSG_CONTENT() {
        return MSG_CONTENT;
    }

    public void setMSG_CONTENT(String MSG_CONTENT) {
        this.MSG_CONTENT = MSG_CONTENT;
    }

    public String getMSG_TITLE() {
        return MSG_TITLE;
    }

    public void setMSG_TITLE(String MSG_TITLE) {
        this.MSG_TITLE = MSG_TITLE;
    }

    public String getEXTRASPARAM() {
        return EXTRASPARAM;
    }

    public void setEXTRASPARAM(String EXTRASPARAM) {
        this.EXTRASPARAM = EXTRASPARAM;
    }

    public String getFORKEY() {
        return FORKEY;
    }

    public void setFORKEY(String FORKEY) {
        this.FORKEY = FORKEY;
    }

    public String getMES_TYPE() {
        return MES_TYPE;
    }

    public void setMES_TYPE(String MES_TYPE) {
        this.MES_TYPE = MES_TYPE;
    }

    public String getSYSCREATEDATE() {
        return SYSCREATEDATE;
    }

    public void setSYSCREATEDATE(String SYSCREATEDATE) {
        this.SYSCREATEDATE = SYSCREATEDATE;
    }

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getURLPATH() {
        return URLPATH;
    }

    public void setURLPATH(String URLPATH) {
        this.URLPATH = URLPATH;
    }
}
