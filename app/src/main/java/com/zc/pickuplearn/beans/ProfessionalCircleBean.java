package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 *
 * 作者： Jonsen
 * 时间: 2017/1/4 11:57
 * 联系方式：chenbin252@163.com
 */

public class ProfessionalCircleBean implements Serializable {

    /**
     * ACTIVELEVEL : 0
     * ANSWERSUM : 0
     * ANSWERTAKESUM : 0
     * PROCIRCLECODE : 2382
     * PROCIRCLENAME : 输电运检
     * PROCIRCLEPOINTS : 0
     * QUESTIONSUM : 0
     * SEQKEY : 601
     * SYSCOMPANYID :
     * SYSCREATEDATE :
     * SYSCREATORID :
     * SYSDEPTID :
     * SYSUPDATEDATE :
     * SYSUPDATORID :
     */
    private boolean ISJOIN;//是否加入
    private String POSITION;//圈子排名
    /**
     * ICOPATH : /file/icon/009.png 圈子的icon路径
     */

    private String ICOPATH;

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    public boolean getISJOIN() {
        return ISJOIN;
    }

    public void setISJOIN(boolean ISJOIN) {
        this.ISJOIN = ISJOIN;
    }

    private String ACTIVELEVEL;
    private String ANSWERSUM;
    private String ANSWERTAKESUM;
    private String PROCIRCLECODE;
    private String PROCIRCLENAME;
    private int PROCIRCLEPOINTS;
    private String QUESTIONSUM;
    private String SEQKEY;
    private String SYSCOMPANYID;
    private String SYSCREATEDATE;
    private String SYSCREATORID;
    private String SYSDEPTID;
    private String SYSUPDATEDATE;
    private String SYSUPDATORID;

    public String getACTIVELEVEL() {
        return ACTIVELEVEL;
    }

    public void setACTIVELEVEL(String ACTIVELEVEL) {
        this.ACTIVELEVEL = ACTIVELEVEL;
    }

    public String getANSWERSUM() {
        return ANSWERSUM;
    }

    public void setANSWERSUM(String ANSWERSUM) {
        this.ANSWERSUM = ANSWERSUM;
    }

    public String getANSWERTAKESUM() {
        return ANSWERTAKESUM;
    }

    public void setANSWERTAKESUM(String ANSWERTAKESUM) {
        this.ANSWERTAKESUM = ANSWERTAKESUM;
    }

    public String getPROCIRCLECODE() {
        return PROCIRCLECODE;
    }

    public void setPROCIRCLECODE(String PROCIRCLECODE) {
        this.PROCIRCLECODE = PROCIRCLECODE;
    }

    public String getPROCIRCLENAME() {
        return PROCIRCLENAME;
    }

    public void setPROCIRCLENAME(String PROCIRCLENAME) {
        this.PROCIRCLENAME = PROCIRCLENAME;
    }

    public int getPROCIRCLEPOINTS() {
        return PROCIRCLEPOINTS;
    }

    public void setPROCIRCLEPOINTS(int PROCIRCLEPOINTS) {
        this.PROCIRCLEPOINTS = PROCIRCLEPOINTS;
    }

    public String getQUESTIONSUM() {
        return QUESTIONSUM;
    }

    public void setQUESTIONSUM(String QUESTIONSUM) {
        this.QUESTIONSUM = QUESTIONSUM;
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

    public String getICOPATH() {
        return ICOPATH;
    }

    public void setICOPATH(String ICOPATH) {
        this.ICOPATH = ICOPATH;
    }
}
