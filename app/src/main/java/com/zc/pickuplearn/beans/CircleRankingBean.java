package com.zc.pickuplearn.beans;

/**
 * 圈内排名详情bean对象
 * 作者： Jonsen
 * 时间: 2017/1/5 16:43
 * 联系方式：chenbin252@163.com
 */

public class CircleRankingBean {

    /**
     * ANSWERSUM : 0
     * ANSWERTAKESUM : 0
     * NICKNAME : 江静
     * PROCIRCLECODE : 2361
     * QUESTIONSUM : 0
     * SUMPOINTS : 0
     */

    private String ANSWERSUM;
    private String ANSWERTAKESUM;
    private String NICKNAME;
    private String PROCIRCLECODE;
    private String QUESTIONSUM;
    private String SUMPOINTS;

    public String getPOSITION() {
        return POSITION;
    }

    public void setPOSITION(String POSITION) {
        this.POSITION = POSITION;
    }

    private String POSITION;//排名
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

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getPROCIRCLECODE() {
        return PROCIRCLECODE;
    }

    public void setPROCIRCLECODE(String PROCIRCLECODE) {
        this.PROCIRCLECODE = PROCIRCLECODE;
    }

    public String getQUESTIONSUM() {
        return QUESTIONSUM;
    }

    public void setQUESTIONSUM(String QUESTIONSUM) {
        this.QUESTIONSUM = QUESTIONSUM;
    }

    public String getSUMPOINTS() {
        return SUMPOINTS;
    }

    public void setSUMPOINTS(String SUMPOINTS) {
        this.SUMPOINTS = SUMPOINTS;
    }
}
