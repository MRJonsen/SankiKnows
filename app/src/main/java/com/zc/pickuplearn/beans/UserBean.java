package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 * 作者： Jonsen
 * 时间: 2016/12/7 15:42
 * 联系方式：chenbin252@163.com
 */

public class UserBean implements Serializable {
    private String USERACCOUNT;
    private String USERCODE;
    private String USERNAME;
    private String SEQKEY;
    private String LEVELNAME;
    private String FILEURL;
    private String NICKNAME;
    /**
     * EXPERTFRADE : 1
     * EXPERTSTATUS : 1
     * EXPERTTYPE : 2
     * PASSWORD : CFCD208495D565EF66E7DFF9F98764DA
     * SUMPOINTS : 6830
     */

    private String SUMPOINTS;
    /**
     * USERTYPE : 2
     * TEAMCIRCLECODE : 6345
     */

    private String USERTYPE;
    private String TEAMCIRCLECODE;

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

    public String getUSERACCOUNT() {
        return USERACCOUNT;
    }

    public void setUSERACCOUNT(String USERACCOUNT) {
        this.USERACCOUNT = USERACCOUNT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getLEVELNAME() {
        return LEVELNAME;
    }

    public void setLEVELNAME(String LEVELNAME) {
        this.LEVELNAME = LEVELNAME;
    }

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }


    public String getSUMPOINTS() {
        return SUMPOINTS;
    }

    public void setSUMPOINTS(String SUMPOINTS) {
        this.SUMPOINTS = SUMPOINTS;
    }

    public String getUSERTYPE() {
        return USERTYPE;
    }

    public void setUSERTYPE(String USERTYPE) {
        this.USERTYPE = USERTYPE;
    }

    public String getTEAMCIRCLECODE() {
        return TEAMCIRCLECODE;
    }

    public void setTEAMCIRCLECODE(String TEAMCIRCLECODE) {
        this.TEAMCIRCLECODE = TEAMCIRCLECODE;
    }
}
