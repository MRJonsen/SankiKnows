package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 * 班组圈信息
 * 作者： Jonsen
 * 时间: 2017/3/13 15:18
 * 联系方式：chenbin252@163.com
 */

public class TeamCircleBean implements Serializable {
    private String TEAMMANIFESTO;//宣言
    private String TEAMCIRCLENAME;//名字
    private String TEAMCIRCLEREMARK;//简介
    /**
     * FILECODE :
     * FILENAME :
     * FILETYPE :
     * FILEURL :
     * ISENABLED :
     * SEQKEY : 6332
     * SYSCOMPANYID :
     * SYSCREATEDATE : 2017-03-13 00:00:00.0
     * SYSCREATORID : 33002305
     * SYSDEPTID : 33002039
     * SYSUPDATEDATE :
     * SYSUPDATORID :
     * TEAMCIRCLECODE : 5724
     * TEAMCIRCLECODENAME : 信息通信工程建设
     */

    private String FILECODE;
    private String FILENAME;
    private String FILETYPE;
    private String FILEURL;
    private String ISENABLED;
    private String SEQKEY;
    private String SYSCOMPANYID;
    private String SYSCREATEDATE;
    private String SYSCREATORID;
    private String SYSDEPTID;
    private String SYSUPDATEDATE;
    private String SYSUPDATORID;
    private String TEAMCIRCLECODE;
    private String TEAMCIRCLECODENAME;
    /**
     * NICKNAME : 班组圈首页的旋转新加字段
     * USERCODE : 33002200
     * USERNAME :
     * USERTYPE : 1
     */

    private String NICKNAME;
    private String USERCODE;
    private String USERNAME;
    private String USERTYPE;
    /**
     * TEAMMEMBERS : 1
     */

    private String TEAMMEMBERS;
    /**
     * TEAMCODE : 6345
     */

    private String TEAMCODE;
    /**
     * STATUS : 0 团队中的状态 0:未加入   1:审核中    2:一加入   3:已创建
     */

    private String STATUS;
    /**
     * JMESSAGEGROUPID : 22592257
     */

    private String JMESSAGEGROUPID;

    public String getTEAMMANIFESTO() {
        return TEAMMANIFESTO;
    }

    public void setTEAMMANIFESTO(String TEAMMANIFESTO) {
        this.TEAMMANIFESTO = TEAMMANIFESTO;
    }

    public String getTEAMCIRCLENAME() {
        return TEAMCIRCLENAME;
    }

    public void setTEAMCIRCLENAME(String TEAMCIRCLENAME) {
        this.TEAMCIRCLENAME = TEAMCIRCLENAME;
    }

    public String getTEAMCIRCLEREMARK() {
        return TEAMCIRCLEREMARK;
    }

    public void setTEAMCIRCLEREMARK(String TEAMCIRCLEREMARK) {
        this.TEAMCIRCLEREMARK = TEAMCIRCLEREMARK;
    }

    public String getFILECODE() {
        return FILECODE;
    }

    public void setFILECODE(String FILECODE) {
        this.FILECODE = FILECODE;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getFILETYPE() {
        return FILETYPE;
    }

    public void setFILETYPE(String FILETYPE) {
        this.FILETYPE = FILETYPE;
    }

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getISENABLED() {
        return ISENABLED;
    }

    public void setISENABLED(String ISENABLED) {
        this.ISENABLED = ISENABLED;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getSYSCOMPANYID() {
        return SYSCOMPANYID;
    }

    public void setSYSCOMPANYID(String SYSCOMPANYID) {
        this.SYSCOMPANYID = SYSCOMPANYID;
    }

    public String getSYSCREATEDATE() {
        return SYSCREATEDATE;
    }

    public void setSYSCREATEDATE(String SYSCREATEDATE) {
        this.SYSCREATEDATE = SYSCREATEDATE;
    }

    public String getSYSCREATORID() {
        return SYSCREATORID;
    }

    public void setSYSCREATORID(String SYSCREATORID) {
        this.SYSCREATORID = SYSCREATORID;
    }

    public String getSYSDEPTID() {
        return SYSDEPTID;
    }

    public void setSYSDEPTID(String SYSDEPTID) {
        this.SYSDEPTID = SYSDEPTID;
    }

    public String getSYSUPDATEDATE() {
        return SYSUPDATEDATE;
    }

    public void setSYSUPDATEDATE(String SYSUPDATEDATE) {
        this.SYSUPDATEDATE = SYSUPDATEDATE;
    }

    public String getSYSUPDATORID() {
        return SYSUPDATORID;
    }

    public void setSYSUPDATORID(String SYSUPDATORID) {
        this.SYSUPDATORID = SYSUPDATORID;
    }

    public String getTEAMCIRCLECODE() {
        return TEAMCIRCLECODE;
    }

    public void setTEAMCIRCLECODE(String TEAMCIRCLECODE) {
        this.TEAMCIRCLECODE = TEAMCIRCLECODE;
    }

    public String getTEAMCIRCLECODENAME() {
        return TEAMCIRCLECODENAME;
    }

    public void setTEAMCIRCLECODENAME(String TEAMCIRCLECODENAME) {
        this.TEAMCIRCLECODENAME = TEAMCIRCLECODENAME;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
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

    public String getUSERTYPE() {
        return USERTYPE;
    }

    public void setUSERTYPE(String USERTYPE) {
        this.USERTYPE = USERTYPE;
    }

    public String getTEAMMEMBERS() {
        return TEAMMEMBERS;
    }

    public void setTEAMMEMBERS(String TEAMMEMBERS) {
        this.TEAMMEMBERS = TEAMMEMBERS;
    }

    public String getTEAMCODE() {
        return TEAMCODE;
    }

    public void setTEAMCODE(String TEAMCODE) {
        this.TEAMCODE = TEAMCODE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getJMESSAGEGROUPID() {
        return JMESSAGEGROUPID;
    }

    public void setJMESSAGEGROUPID(String JMESSAGEGROUPID) {
        this.JMESSAGEGROUPID = JMESSAGEGROUPID;
    }
}
