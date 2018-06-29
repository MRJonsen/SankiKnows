package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 * 专家类
 * 作者： Jonsen
 * 时间: 2017/4/5 11:42
 * 联系方式：chenbin252@163.com
 */

public class Professor  implements Serializable{
    /**
     * DESCRIBE : 史蒂夫·乔布斯[1]  （Steve Jobs，1955年2月24日—2011年10月5日[2]  ），出生于美国加利福尼亚州旧金山，美国发明家、企业家、美国苹果公司联合创办人。[3] 1976年4月1日，乔布斯签署了一份合同，决定成立一家电脑公司。[1]  1977年4月，乔布斯在美国第一次计算机展览会展示了苹果Ⅱ号样机。1997年苹果推出iMac，创新的外壳
     * EXPERTDATE :
     * EXPERTFRADE :
     * EXPERTID : 29
     * EXPERTTYPE :
     * FILEURL :
     * GOODFIELD :
     * PROCIRCLECODE : 2382
     * PROFESSIONAL :
     * SEQKEY : 14
     * STATUS :
     * USERCODE : 33002200
     * USERNAME : 曹炯
     */

    private String DESCRIBE;
    private String EXPERTDATE;
    private String EXPERTFRADE;
    private String EXPERTID;
    private String EXPERTTYPE;
    private String FILEURL;
    private String GOODFIELD;
    private String PROCIRCLECODE;
    private String PROFESSIONAL;
    private String SEQKEY;
    private String STATUS;
    private String USERCODE;
    private String USERNAME;
    /**
     * SYSCREATORID : wanghm
     * SCORE :
     * PROCIRCLENAME : 人力资源
     * SERVICEAREA : 66660000
     * SYSUPDATORID :
     * SYSCOMPANYID : 66660000
     * SYSUPDATEDATE :
     * ISONLINE : 0
     * CLICKCOUNT : 6
     * SYSDEPTID : 66660001
     * SYSCREATEDATE : 2017-11-07 00:00:00.0
     */

    private String ISONLINE;
    private int CLICKCOUNT;
    /**
     * SYSCREATORID : 66010001
     * SCORE :
     * PROCIRCLENAME : 前端开发|人力资源|项目管理|后台开发|项目运营|是短发
     * SERVICEAREA : 66660000
     * SYSUPDATORID : wanghm
     * SYSCOMPANYID : 66660000
     * SYSUPDATEDATE : 2017-11-21 00:00:00.0
     * SYSDEPTID : 66660001
     * SYSCREATEDATE : 2017-10-25 00:00:00.0
     */

    private String SCORE;
    /**
     * SYSCREATORID : 66010001
     * GRADE : 1
     * PROCIRCLENAME : 前端开发|人力资源|项目管理|后台开发|项目运营|测试专区
     * SERVICEAREA : 66660000
     * SYSUPDATORID : 66010001
     * SYSCOMPANYID : 66660000
     * SYSUPDATEDATE : 2017-11-23 00:00:00.0
     * SYSDEPTID : 66660001
     * SYSCREATEDATE : 2017-10-25 00:00:00.0
     */

    private String GRADE;
    /**
     * SYSCREATORID : 66010001
     * PROCIRCLENAME : 前端开发|人力资源|项目管理|后台开发|项目运营|测试专区
     * GRADENAME : 国家级专家顾问
     * SYSDEPTID : 66660001
     * SYSCOMPANYID : 66660000
     * SYSUPDATORID : 66010001
     * SERVICEAREA : 66660000
     * SYSUPDATEDATE : 2017-11-23 00:00:00.0
     * SYSCREATEDATE : 2017-10-25 00:00:00.0
     */

    private String GRADENAME;

    public String getDESCRIBE() {
        return DESCRIBE;
    }

    public void setDESCRIBE(String DESCRIBE) {
        this.DESCRIBE = DESCRIBE;
    }

    public String getEXPERTDATE() {
        return EXPERTDATE;
    }

    public void setEXPERTDATE(String EXPERTDATE) {
        this.EXPERTDATE = EXPERTDATE;
    }

    public String getEXPERTFRADE() {
        return EXPERTFRADE;
    }

    public void setEXPERTFRADE(String EXPERTFRADE) {
        this.EXPERTFRADE = EXPERTFRADE;
    }

    public String getEXPERTID() {
        return EXPERTID;
    }

    public void setEXPERTID(String EXPERTID) {
        this.EXPERTID = EXPERTID;
    }

    public String getEXPERTTYPE() {
        return EXPERTTYPE;
    }

    public void setEXPERTTYPE(String EXPERTTYPE) {
        this.EXPERTTYPE = EXPERTTYPE;
    }

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getGOODFIELD() {
        return GOODFIELD;
    }

    public void setGOODFIELD(String GOODFIELD) {
        this.GOODFIELD = GOODFIELD;
    }

    public String getPROCIRCLECODE() {
        return PROCIRCLECODE;
    }

    public void setPROCIRCLECODE(String PROCIRCLECODE) {
        this.PROCIRCLECODE = PROCIRCLECODE;
    }

    public String getPROFESSIONAL() {
        return PROFESSIONAL;
    }

    public void setPROFESSIONAL(String PROFESSIONAL) {
        this.PROFESSIONAL = PROFESSIONAL;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getISONLINE() {
        return ISONLINE;
    }

    public void setISONLINE(String ISONLINE) {
        this.ISONLINE = ISONLINE;
    }

    public int getCLICKCOUNT() {
        return CLICKCOUNT;
    }

    public void setCLICKCOUNT(int CLICKCOUNT) {
        this.CLICKCOUNT = CLICKCOUNT;
    }

    public String getSCORE() {
        return SCORE;
    }

    public void setSCORE(String SCORE) {
        this.SCORE = SCORE;
    }

    public String getGRADE() {
        return GRADE;
    }

    public void setGRADE(String GRADE) {
        this.GRADE = GRADE;
    }

    public String getGRADENAME() {
        return GRADENAME;
    }

    public void setGRADENAME(String GRADENAME) {
        this.GRADENAME = GRADENAME;
    }
}
