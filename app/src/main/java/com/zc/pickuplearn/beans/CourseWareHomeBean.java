package com.zc.pickuplearn.beans;

/**
 * Created by chenbin on 2017/12/12.
 */

public class CourseWareHomeBean {

    /**
     * TYPENAME : 安全生产
     * ABILITYTYPE : B
     * POSTYPE : D-06-05
     * DATALIST : {"datas":[{"SYSCREATORID":"1","FILEURL":",2017-12-08\\f9058738-3ce7-4063-9061-f5d36eca150c.docx","TYPENAME":"电工基础","ISCOLLECT":"","FILECODE":"电网运行规则（试行）.docx@E0688731BBE538D761645F6773AF81CA","POSTYPE":"D-06-05","SEQKEY":"1","ABILITYCODE":"X01A01","FILENAME":"电网运行规则（试行）.docx","SERVICEAREA":"33000001","COURSENAME":"电网运行规则","FILETYPE":".docx","COMMENTCOUNT":"","ISTHEORY":"1","CONTENTLEN":"4页","ABILITYTYPE":"B","POSCODE":"22","PHOTOURL":",2017-12-08\\05810a10-7a49-4850-bafa-ade483d2e7c2.jpg","PHOTOURLNAME":"","CLICKCOUNT":0,"COURSEXPLAIN":"11月18日，国务院公布《划转部分国有资本充实社保基金实施方案》，中央和地方及国有控股大中型企业、金融机构都纳入股权转让范围，划转比例统一为企业国有股权的10％。","SYSCREATEDATE":"2017-12-08 09:43:25.0"}]}
     * POSCODE : 22
     *
     */

    private String TYPENAME;
    private String ABILITYTYPE;
    private String POSTYPE;
    private String DATALIST;
    private String POSCODE;

    public String getTYPENAME() {
        return TYPENAME;
    }

    public void setTYPENAME(String TYPENAME) {
        this.TYPENAME = TYPENAME;
    }

    public String getABILITYTYPE() {
        return ABILITYTYPE;
    }

    public void setABILITYTYPE(String ABILITYTYPE) {
        this.ABILITYTYPE = ABILITYTYPE;
    }

    public String getPOSTYPE() {
        return POSTYPE;
    }

    public void setPOSTYPE(String POSTYPE) {
        this.POSTYPE = POSTYPE;
    }

    public String getDATALIST() {
        return DATALIST;
    }

    public void setDATALIST(String DATALIST) {
        this.DATALIST = DATALIST;
    }

    public String getPOSCODE() {
        return POSCODE;
    }

    public void setPOSCODE(String POSCODE) {
        this.POSCODE = POSCODE;
    }
}
