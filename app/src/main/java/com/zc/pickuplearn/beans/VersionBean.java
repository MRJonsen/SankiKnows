package com.zc.pickuplearn.beans;

/**
 * 作者： Jonsen
 * 时间: 2017/1/13 12:29
 * 联系方式：chenbin252@163.com
 */

public class VersionBean {

    /**
     * FILEURL : https://www.pgyer.com/jH0E
     * VERSIONNAME : 1.2
     * MOBILETYPE : 1
     * FILEURL2 : http://120.27.152.63:6980/nbsjzd.apk
     * VERSIONCODE : 3
     * TYPE : 2
     */

    private String FILEURL;
    private String VERSIONNAME;
    private String MOBILETYPE;
    private String FILEURL2;
    private int VERSIONCODE;
    private String TYPE;

    public String getFILEURL() {
        return FILEURL;
    }

    public void setFILEURL(String FILEURL) {
        this.FILEURL = FILEURL;
    }

    public String getVERSIONNAME() {
        return VERSIONNAME;
    }

    public void setVERSIONNAME(String VERSIONNAME) {
        this.VERSIONNAME = VERSIONNAME;
    }

    public String getMOBILETYPE() {
        return MOBILETYPE;
    }

    public void setMOBILETYPE(String MOBILETYPE) {
        this.MOBILETYPE = MOBILETYPE;
    }

    public String getFILEURL2() {
        return FILEURL2;
    }

    public void setFILEURL2(String FILEURL2) {
        this.FILEURL2 = FILEURL2;
    }

    public int getVERSIONCODE() {
        return VERSIONCODE;
    }

    public void setVERSIONCODE(int VERSIONCODE) {
        this.VERSIONCODE = VERSIONCODE;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}
