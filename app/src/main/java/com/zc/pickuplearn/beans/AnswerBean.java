package com.zc.pickuplearn.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 14:44
 * 联系方式：chenbin252@163.com
 */

public class AnswerBean implements Serializable{

    /**
     * FILEURL : ,2016-12-14\dc6fcd82-231f-402c-85a6-78cb96152850.jpg,2016-12-14\8c005bf7-e1b4-4497-9d99-a8a696759cc5.jpg
     * SYSCREATORID : 33000535
     * FILECODE : image0.jpg@612590D57018188CAC9D2F0FDE977D76,image1.jpg@07EA4B83A87850F9F764E698DDB8B32F
     * QUESTIONID : Q267
     * ANSWERLEVEL : 1
     * SEQKEY : 312
     * FILENAME : image0.jpg,image1.jpg
     * ANSWEREXPLAIN : 我知道的。。。
     * SYSCOMPANYID : 33000001
     * SYSUPDATORID : 33002200
     * QACOUNT : 34
     * SYSUPDATEDATE : 2016-12-14 00:00:00.0
     * FILETYPE : jpg,jpg
     * HEADIMAGE : ,2016-12-16\6973b4e7-c36a-4c4b-a179-ddc68af47a00.png
     * NICKNAME : 船用
     * GOODNUMS : 1
     * ANSWERUSERCODE : 33000535
     * ISPASS : 1
     * ISGOOD : 0
     * ANSWERIMAGE :
     * ANSWERUSERNAME : 致成科技。
     * SYSDEPTID : 33002008
     * SYSCREATEDATE : 2016-12-14 13:54:19.0
     * ISENABLED : 0
     */

    private String FILEURL;
    private String SYSCREATORID;
    private String FILECODE;
    private String QUESTIONID;
    private String ANSWERLEVEL;
    private String SEQKEY;
    private String FILENAME;
    private String ANSWEREXPLAIN;
    private String SYSCOMPANYID;
    private String SYSUPDATORID;
    private String QACOUNT;
    private String SYSUPDATEDATE;
    private String FILETYPE;
    private String HEADIMAGE;
    private String NICKNAME;
    private String GOODNUMS;
    private String ANSWERUSERCODE;
    private String ISPASS;
    private String ISGOOD;
    private String ANSWERIMAGE;
    private String ANSWERUSERNAME;
    private String SYSDEPTID;
    private String SYSCREATEDATE;
    private String ISENABLED;
    /**
     * OPERATETYPE : 0
     */

    private String OPERATETYPE;
    /**
     * ANSWERCOUNT : 1
     * INFOCOUNT :
     * QUESTIONCOUNT :
     */

    private String ANSWERCOUNT;
    private String INFOCOUNT;
    private String QUESTIONCOUNT;
    /**
     * QACOUNT : 5
     * SERVICEAREA : 66660000
     * ANSWEREXPLAINEMOJI : Zmdoampraua7muWbnuWutui/mOe7meS9oOWkjeWPpOe6og==
     * GOODNUMS : 0
     * DATALIST : {"datas":[{"SYSCREATORID":"wanghm","FILEURL":"","ANSWERCODE":"1761","FILECODE":"","SEQKEY":"1079","OPERATETYPE":"1","FILENAME":"","SERVICEAREA":"66660000","SYSUPDATORID":"","SYSCOMPANYID":"66660000","EXPLAINEMOJI":"5rOV5Zu95LiA5p2h6KGX","SYSUPDATEDATE":"","FILETYPE":"","SYSDEPTID":"66660001","SYSCREATEDATE":"2017-10-2719: 30: 06.0","EXPLAIN":"法国一条街"}]}
     */

    private String DATALIST;

    /**
     * QACOUNT : 0
     * SERVICEAREA : 66660000
     * ANSWEREXPLAINEMOJI : 5Zue5aSN77yM5raI5oGv5piv5ZCm5q2j5bi4
     * GOODNUMS : 1
     * DATALIST :
     */


    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getSYSCREATORID() {
        return SYSCREATORID;
    }

    public void setSYSCREATORID(String SYSCREATORID) {
        this.SYSCREATORID = SYSCREATORID;
    }

    public String getFILECODE() {
        return FILECODE;
    }

    public void setFILECODE(String FILECODE) {
        this.FILECODE = FILECODE;
    }

    public String getQUESTIONID() {
        return QUESTIONID;
    }

    public void setQUESTIONID(String QUESTIONID) {
        this.QUESTIONID = QUESTIONID;
    }

    public String getANSWERLEVEL() {
        return ANSWERLEVEL;
    }

    public void setANSWERLEVEL(String ANSWERLEVEL) {
        this.ANSWERLEVEL = ANSWERLEVEL;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getANSWEREXPLAIN() {
        return ANSWEREXPLAIN;
    }

    public void setANSWEREXPLAIN(String ANSWEREXPLAIN) {
        this.ANSWEREXPLAIN = ANSWEREXPLAIN;
    }

    public String getSYSCOMPANYID() {
        return SYSCOMPANYID;
    }

    public void setSYSCOMPANYID(String SYSCOMPANYID) {
        this.SYSCOMPANYID = SYSCOMPANYID;
    }

    public String getSYSUPDATORID() {
        return SYSUPDATORID;
    }

    public void setSYSUPDATORID(String SYSUPDATORID) {
        this.SYSUPDATORID = SYSUPDATORID;
    }

    public String getQACOUNT() {
        return QACOUNT;
    }

    public void setQACOUNT(String QACOUNT) {
        this.QACOUNT = QACOUNT;
    }

    public String getSYSUPDATEDATE() {
        return SYSUPDATEDATE;
    }

    public void setSYSUPDATEDATE(String SYSUPDATEDATE) {
        this.SYSUPDATEDATE = SYSUPDATEDATE;
    }

    public String getFILETYPE() {
        return FILETYPE;
    }

    public void setFILETYPE(String FILETYPE) {
        this.FILETYPE = FILETYPE;
    }

    public String getHEADIMAGE() {
        return HEADIMAGE;
    }

    public void setHEADIMAGE(String HEADIMAGE) {
        this.HEADIMAGE = HEADIMAGE;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getGOODNUMS() {
        return GOODNUMS;
    }

    public void setGOODNUMS(String GOODNUMS) {
        this.GOODNUMS = GOODNUMS;
    }

    public String getANSWERUSERCODE() {
        return ANSWERUSERCODE;
    }

    public void setANSWERUSERCODE(String ANSWERUSERCODE) {
        this.ANSWERUSERCODE = ANSWERUSERCODE;
    }

    public String getISPASS() {
        return ISPASS;
    }

    public void setISPASS(String ISPASS) {
        this.ISPASS = ISPASS;
    }

    public String getISGOOD() {
        return ISGOOD;
    }

    public void setISGOOD(String ISGOOD) {
        this.ISGOOD = ISGOOD;
    }

    public String getANSWERIMAGE() {
        return ANSWERIMAGE;
    }

    public void setANSWERIMAGE(String ANSWERIMAGE) {
        this.ANSWERIMAGE = ANSWERIMAGE;
    }

    public String getANSWERUSERNAME() {
        return ANSWERUSERNAME;
    }

    public void setANSWERUSERNAME(String ANSWERUSERNAME) {
        this.ANSWERUSERNAME = ANSWERUSERNAME;
    }

    public String getSYSDEPTID() {
        return SYSDEPTID;
    }

    public void setSYSDEPTID(String SYSDEPTID) {
        this.SYSDEPTID = SYSDEPTID;
    }

    public String getSYSCREATEDATE() {
        return SYSCREATEDATE;
    }

    public void setSYSCREATEDATE(String SYSCREATEDATE) {
        this.SYSCREATEDATE = SYSCREATEDATE;
    }

    public String getISENABLED() {
        return ISENABLED;
    }

    public void setISENABLED(String ISENABLED) {
        this.ISENABLED = ISENABLED;
    }

    public String getOPERATETYPE() {
        return OPERATETYPE;
    }

    public void setOPERATETYPE(String OPERATETYPE) {
        this.OPERATETYPE = OPERATETYPE;
    }

    public String getANSWERCOUNT() {
        return ANSWERCOUNT;
    }

    public void setANSWERCOUNT(String ANSWERCOUNT) {
        this.ANSWERCOUNT = ANSWERCOUNT;
    }

    public String getINFOCOUNT() {
        return INFOCOUNT;
    }

    public void setINFOCOUNT(String INFOCOUNT) {
        this.INFOCOUNT = INFOCOUNT;
    }

    public String getQUESTIONCOUNT() {
        return QUESTIONCOUNT;
    }

    public void setQUESTIONCOUNT(String QUESTIONCOUNT) {
        this.QUESTIONCOUNT = QUESTIONCOUNT;
    }

    public String getDATALIST() {
        return DATALIST;
    }

    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }

}
