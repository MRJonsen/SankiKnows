package com.zc.pickuplearn.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 问题 bean
 * 作者： Jonsen
 * 时间: 2016/12/29 9:38
 * 联系方式：chenbin252@163.com
 */

public class QuestionBean implements Serializable{

    /**
     * EXPERTUSERCODE :
     * QUESTIONEXPLAINEMOJI : 5rWL6K+V5LiA5LiL5YWs5Y+45omA5bGe5pyN5Yqh5Yy677yM5YaN5rWL6K+V5LiA5LiL5L+u5pS55Yqf6IO9
     * ANSWERSUM : 1
     * SERVICEAREA : 66660000
     * GOODNUMS : 0
     * ISGOOD :
     */

    private String SERVICEAREA;
    private int GOODNUMS;
    private String ISGOOD;
    public String getPARENTID() {
        return PARENTID;
    }

    public void setPARENTID(String PARENTID) {
        this.PARENTID = PARENTID;
    }

    /**
     * SYSCREATORID : 33000535
     * SEQKEY : Q261
     * FILETYPE :
     * HEADIMAGE : ,2016-12-16\6973b4e7-c36a-4c4b-a179-ddc68af47a00.png
     * NICKNAME : 致成科技。
     * ISLOSE : 0
     * QUESTIONIMAGE :
     * SYSDEPTID : 33002008
     * FILEURL :
     * FILECODE :
     * QUESTIONUSERNAME : 张金儿
     * QUESTIONUSERCODE : 33000535
     * ANSWERSUM :
     * QUESTIONEXPLAIN : 输电线
     * FILENAME :
     * QUESTIONLEVEL : 1
     * SYSUPDATORID :
     * SYSCOMPANYID : 33000001
     * SYSUPDATEDATE :
     * QUESTIONTYPECODE : 5662
     * ISEXPERTANSWER : 0
     * ISSOLVE : 0
     * STATISTICALSRC :
     * SYSCREATEDATE : 2016-11-22 13:10:06.0
     * IMPORTLEVEL : 1
     */
    private String PARENTID;
    private String SYSCREATORID;
    private String SEQKEY;
    private String FILETYPE;
    private String HEADIMAGE;
    private String NICKNAME;
    private String ISLOSE;
    private String QUESTIONIMAGE;
    private String SYSDEPTID;
    private String FILEURL;
    private String FILECODE;
    private String QUESTIONUSERNAME;
    private String QUESTIONUSERCODE;
    private String ANSWERSUM;
    private String QUESTIONEXPLAIN;
    private String FILENAME;
    private String QUESTIONLEVEL;
    private String SYSUPDATORID;
    private String SYSCOMPANYID;
    private String SYSUPDATEDATE;
    private String QUESTIONTYPECODE;
    private String ISEXPERTANSWER;
    private String ISSOLVE;
    private String STATISTICALSRC;
    private String SYSCREATEDATE;
    private String IMPORTLEVEL;
    private String QUESTIONTYPENAME;
    /**
     * BONUSPOINTS : 5
     * ISANONYMITY : 1
     */

    private String BONUSPOINTS;//奖励分数
    private String ISANONYMITY;//是否匿名
    /**
     * ISANSWER : 是不是回答者
     */

    private String ISANSWER;

    private List<QuestionTypeBean> tip;//标签
    /**
     * TARGETUSERCODE :
     * TEAMCIRCLECODE : 6343
     */

    private String TARGETUSERCODE;
    private String TEAMCIRCLECODE;
    /**
     * INFOCOUNT : 0
     */

    private String INFOCOUNT;
    /**
     * USERCODE : 33000535
     */

    private String USERCODE;
    /**
     * TARGETUSERNAME :
     */

    private String TARGETUSERNAME;

    public List<QuestionTypeBean> getTip() {
        return tip;
    }

    public void setTip(List<QuestionTypeBean> tip) {
        this.tip = tip;
    }


    public String getQUESTIONTYPENAME() {
        return QUESTIONTYPENAME;
    }

    public void setQUESTIONTYPENAME(String QUESTIONTYPENAME) {
        this.QUESTIONTYPENAME = QUESTIONTYPENAME;
    }






    public String getSYSCREATORID() {
        return SYSCREATORID;
    }

    public void setSYSCREATORID(String SYSCREATORID) {
        this.SYSCREATORID = SYSCREATORID;
    }

    public String getSEQKEY() {
        return SEQKEY;
    }

    public void setSEQKEY(String SEQKEY) {
        this.SEQKEY = SEQKEY;
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

    public String getISLOSE() {
        return ISLOSE;
    }

    public void setISLOSE(String ISLOSE) {
        this.ISLOSE = ISLOSE;
    }

    public String getQUESTIONIMAGE() {
        return QUESTIONIMAGE;
    }

    public void setQUESTIONIMAGE(String QUESTIONIMAGE) {
        this.QUESTIONIMAGE = QUESTIONIMAGE;
    }

    public String getSYSDEPTID() {
        return SYSDEPTID;
    }

    public void setSYSDEPTID(String SYSDEPTID) {
        this.SYSDEPTID = SYSDEPTID;
    }

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getFILECODE() {
        return FILECODE;
    }

    public void setFILECODE(String FILECODE) {
        this.FILECODE = FILECODE;
    }

    public String getQUESTIONUSERNAME() {
        return QUESTIONUSERNAME;
    }

    public void setQUESTIONUSERNAME(String QUESTIONUSERNAME) {
        this.QUESTIONUSERNAME = QUESTIONUSERNAME;
    }

    public String getQUESTIONUSERCODE() {
        return QUESTIONUSERCODE;
    }

    public void setQUESTIONUSERCODE(String QUESTIONUSERCODE) {
        this.QUESTIONUSERCODE = QUESTIONUSERCODE;
    }

    public String getANSWERSUM() {
        return ANSWERSUM;
    }

    public void setANSWERSUM(String ANSWERSUM) {
        this.ANSWERSUM = ANSWERSUM;
    }

    public String getQUESTIONEXPLAIN() {
        return QUESTIONEXPLAIN;
    }

    public void setQUESTIONEXPLAIN(String QUESTIONEXPLAIN) {
        this.QUESTIONEXPLAIN = QUESTIONEXPLAIN;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getQUESTIONLEVEL() {
        return QUESTIONLEVEL;
    }

    public void setQUESTIONLEVEL(String QUESTIONLEVEL) {
        this.QUESTIONLEVEL = QUESTIONLEVEL;
    }

    public String getSYSUPDATORID() {
        return SYSUPDATORID;
    }

    public void setSYSUPDATORID(String SYSUPDATORID) {
        this.SYSUPDATORID = SYSUPDATORID;
    }

    public String getSYSCOMPANYID() {
        return SYSCOMPANYID;
    }

    public void setSYSCOMPANYID(String SYSCOMPANYID) {
        this.SYSCOMPANYID = SYSCOMPANYID;
    }

    public String getSYSUPDATEDATE() {
        return SYSUPDATEDATE;
    }

    public void setSYSUPDATEDATE(String SYSUPDATEDATE) {
        this.SYSUPDATEDATE = SYSUPDATEDATE;
    }

    public String getQUESTIONTYPECODE() {
        return QUESTIONTYPECODE;
    }

    public void setQUESTIONTYPECODE(String QUESTIONTYPECODE) {
        this.QUESTIONTYPECODE = QUESTIONTYPECODE;
    }

    public String getISEXPERTANSWER() {
        return ISEXPERTANSWER;
    }

    public void setISEXPERTANSWER(String ISEXPERTANSWER) {
        this.ISEXPERTANSWER = ISEXPERTANSWER;
    }

    public String getISSOLVE() {
        return ISSOLVE;
    }

    public void setISSOLVE(String ISSOLVE) {
        this.ISSOLVE = ISSOLVE;
    }

    public String getSTATISTICALSRC() {
        return STATISTICALSRC;
    }

    public void setSTATISTICALSRC(String STATISTICALSRC) {
        this.STATISTICALSRC = STATISTICALSRC;
    }

    public String getSYSCREATEDATE() {
        return SYSCREATEDATE;
    }

    public void setSYSCREATEDATE(String SYSCREATEDATE) {
        this.SYSCREATEDATE = SYSCREATEDATE;
    }

    public String getIMPORTLEVEL() {
        return IMPORTLEVEL;
    }

    public void setIMPORTLEVEL(String IMPORTLEVEL) {
        this.IMPORTLEVEL = IMPORTLEVEL;
    }

    public String getBONUSPOINTS() {
        return BONUSPOINTS;
    }

    public void setBONUSPOINTS(String BONUSPOINTS) {
        this.BONUSPOINTS = BONUSPOINTS;
    }

    public String getISANONYMITY() {
        return ISANONYMITY;
    }

    public void setISANONYMITY(String ISANONYMITY) {
        this.ISANONYMITY = ISANONYMITY;
    }

    public String getISANSWER() {
        return ISANSWER;
    }

    public void setISANSWER(String ISANSWER) {
        this.ISANSWER = ISANSWER;
    }

    public String getTARGETUSERCODE() {
        return TARGETUSERCODE;
    }

    public void setTARGETUSERCODE(String TARGETUSERCODE) {
        this.TARGETUSERCODE = TARGETUSERCODE;
    }

    public String getTEAMCIRCLECODE() {
        return TEAMCIRCLECODE;
    }

    public void setTEAMCIRCLECODE(String TEAMCIRCLECODE) {
        this.TEAMCIRCLECODE = TEAMCIRCLECODE;
    }

    public String getINFOCOUNT() {
        return INFOCOUNT;
    }

    public void setINFOCOUNT(String INFOCOUNT) {
        this.INFOCOUNT = INFOCOUNT;
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE;
    }

    public String getTARGETUSERNAME() {
        return TARGETUSERNAME;
    }

    public void setTARGETUSERNAME(String TARGETUSERNAME) {
        this.TARGETUSERNAME = TARGETUSERNAME;
    }

    public String getSERVICEAREA() {
        return SERVICEAREA;
    }

    public void setSERVICEAREA(String SERVICEAREA) {
        this.SERVICEAREA = SERVICEAREA;
    }

    public int getGOODNUMS() {
        return GOODNUMS;
    }

    public void setGOODNUMS(int GOODNUMS) {
        this.GOODNUMS = GOODNUMS;
    }

    public String getISGOOD() {
        return ISGOOD;
    }

    public void setISGOOD(String ISGOOD) {
        this.ISGOOD = ISGOOD;
    }
}
