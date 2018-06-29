package com.zc.pickuplearn.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by chenbin on 2017/12/12.
 */

public class CourseWareBean {


    private List<DatasBean> datas;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * totalCount : 2
     * datas : [{"SYSCREATORID":"66669999","FILEURL":",2017-12-19\\315c59e8-73f1-4982-8ddf-2e1aeef48aa6.docx","TYPENAME":"电工基础","ISCOLLECT":"","FILECODE":"祝福词.docx@D7BB2DD2ADDAB8CB604608D60A29B9FB","POSTYPE":"D-06-05","SEQKEY":"42","ABILITYCODE":"X01A01","FILENAME":"祝福词.docx","SERVICEAREA":"33000001","COURSENAME":"基础电工（二）","FILETYPE":".docx","COMMENTCOUNT":0,"ISTHEORY":"1","CONTENTLEN":"共3页","ABILITYTYPE":"A","POSCODE":"D-06-05-001","PHOTOURL":",2017-12-19\\c85dbdc7-0215-4a2a-a278-dd6eed85c5a2.jpg","PHOTOURLNAME":"","CLICKCOUNT":35,"COURSEXPLAIN":"","SYSCREATEDATE":"2017-12-19 14:50:28.0"},{"SYSCREATORID":"66669999","FILEURL":",2017-12-19\\315c59e8-73f1-4982-8ddf-2e1aeef48aa6.docx","TYPENAME":"电工基础","ISCOLLECT":"","FILECODE":"祝福词.docx@D7BB2DD2ADDAB8CB604608D60A29B9FB","POSTYPE":"D-06-05","SEQKEY":"41","ABILITYCODE":"X01A01","FILENAME":"祝福词.docx","SERVICEAREA":"33000001","COURSENAME":"基础电工（一）","FILETYPE":".docx","COMMENTCOUNT":0,"ISTHEORY":"1","CONTENTLEN":"共2页","ABILITYTYPE":"A","POSCODE":"D-06-05-001","PHOTOURL":",2017-12-19\\c85dbdc7-0215-4a2a-a278-dd6eed85c5a2.jpg","PHOTOURLNAME":"","CLICKCOUNT":9,"COURSEXPLAIN":"","SYSCREATEDATE":"2017-12-19 14:49:53.0"}]
     */

    private int totalCount;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Serializable {
        /**
         * SYSCREATORID : 1
         * FILEURL : ,2017-12-08\\f9058738-3ce7-4063-9061-f5d36eca150c.docx
         * TYPENAME : 电工基础
         * ISCOLLECT :
         * FILECODE : 电网运行规则（试行）.docx@E0688731BBE538D761645F6773AF81CA
         * POSTYPE : D-06-05
         * SEQKEY : 1
         * ABILITYCODE : X01A01
         * FILENAME : 电网运行规则（试行）.docx
         * SERVICEAREA : 33000001
         * COURSENAME : 电网运行规则
         * FILETYPE : .docx
         * COMMENTCOUNT :
         * ISTHEORY : 1 //1 理论 2 实操
         * CONTENTLEN : 4页
         * ABILITYTYPE : B
         * POSCODE : 22
         * PHOTOURL : , 2017-12-08\\05810a10-7a49-4850-bafa-ade483d2e7c2.jpg
         * PHOTOURLNAME :
         * CLICKCOUNT : 0
         * COURSEXPLAIN : 11月18日，国务院公布《划转部分国有资本充实社保基金实施方案》，中央和地方及国有控股大中型企业、金融机构都纳入股权转让范围，划转比例统一为企业国有股权的10％。
         * SYSCREATEDATE : 2017-12-0809: 43: 25.0
         */

        private String SYSCREATORID;
        private String FILEURL;
        private String TYPENAME;
        private String ISCOLLECT;
        private String FILECODE;
        private String POSTYPE;
        private String SEQKEY;
        private String ABILITYCODE;
        private String FILENAME;
        private String SERVICEAREA;
        private String COURSENAME;
        private String FILETYPE;
        private String COMMENTCOUNT;
        private String ISTHEORY;
        private String CONTENTLEN;
        private String ABILITYTYPE;
        private String POSCODE;
        private String PHOTOURL;
        private String PHOTOURLNAME;
        private int CLICKCOUNT;
        private String COURSEXPLAIN;
        private String SYSCREATEDATE;
        private String PLAYTIME;

        public String getPLAYTIME() {
            return PLAYTIME;
        }

        public void setPLAYTIME(String PLAYTIME) {
            this.PLAYTIME = PLAYTIME;
        }

        /**
         * H5URL : www.baidu.com
         */

        private String H5URL;

        public String getSYSCREATORID() {
            return SYSCREATORID;
        }

        public void setSYSCREATORID(String SYSCREATORID) {
            this.SYSCREATORID = SYSCREATORID;
        }

        public String getFILEURL() {
            return FILEURL;
        }

        public void setFILEURL(String FILEURL) {
            this.FILEURL = FILEURL;
        }

        public String getTYPENAME() {
            return TYPENAME;
        }

        public void setTYPENAME(String TYPENAME) {
            this.TYPENAME = TYPENAME;
        }

        public String getISCOLLECT() {
            return ISCOLLECT;
        }

        public void setISCOLLECT(String ISCOLLECT) {
            this.ISCOLLECT = ISCOLLECT;
        }

        public String getFILECODE() {
            return FILECODE;
        }

        public void setFILECODE(String FILECODE) {
            this.FILECODE = FILECODE;
        }

        public String getPOSTYPE() {
            return POSTYPE;
        }

        public void setPOSTYPE(String POSTYPE) {
            this.POSTYPE = POSTYPE;
        }

        public String getSEQKEY() {
            return SEQKEY;
        }

        public void setSEQKEY(String SEQKEY) {
            this.SEQKEY = SEQKEY;
        }

        public String getABILITYCODE() {
            return ABILITYCODE;
        }

        public void setABILITYCODE(String ABILITYCODE) {
            this.ABILITYCODE = ABILITYCODE;
        }

        public String getFILENAME() {
            return FILENAME;
        }

        public void setFILENAME(String FILENAME) {
            this.FILENAME = FILENAME;
        }

        public String getSERVICEAREA() {
            return SERVICEAREA;
        }

        public void setSERVICEAREA(String SERVICEAREA) {
            this.SERVICEAREA = SERVICEAREA;
        }

        public String getCOURSENAME() {
            return COURSENAME;
        }

        public void setCOURSENAME(String COURSENAME) {
            this.COURSENAME = COURSENAME;
        }

        public String getFILETYPE() {
            return FILETYPE;
        }

        public void setFILETYPE(String FILETYPE) {
            this.FILETYPE = FILETYPE;
        }

        public String getCOMMENTCOUNT() {
            return COMMENTCOUNT;
        }

        public void setCOMMENTCOUNT(String COMMENTCOUNT) {
            this.COMMENTCOUNT = COMMENTCOUNT;
        }

        public String getISTHEORY() {
            return ISTHEORY;
        }

        public void setISTHEORY(String ISTHEORY) {
            this.ISTHEORY = ISTHEORY;
        }

        public String getCONTENTLEN() {
            return CONTENTLEN;
        }

        public void setCONTENTLEN(String CONTENTLEN) {
            this.CONTENTLEN = CONTENTLEN;
        }

        public String getABILITYTYPE() {
            return ABILITYTYPE;
        }

        public void setABILITYTYPE(String ABILITYTYPE) {
            this.ABILITYTYPE = ABILITYTYPE;
        }

        public String getPOSCODE() {
            return POSCODE;
        }

        public void setPOSCODE(String POSCODE) {
            this.POSCODE = POSCODE;
        }

        public String getPHOTOURL() {
            return PHOTOURL;
        }

        public void setPHOTOURL(String PHOTOURL) {
            this.PHOTOURL = PHOTOURL;
        }

        public String getPHOTOURLNAME() {
            return PHOTOURLNAME;
        }

        public void setPHOTOURLNAME(String PHOTOURLNAME) {
            this.PHOTOURLNAME = PHOTOURLNAME;
        }

        public int getCLICKCOUNT() {
            return CLICKCOUNT;
        }

        public void setCLICKCOUNT(int CLICKCOUNT) {
            this.CLICKCOUNT = CLICKCOUNT;
        }

        public String getCOURSEXPLAIN() {
            return COURSEXPLAIN;
        }

        public void setCOURSEXPLAIN(String COURSEXPLAIN) {
            this.COURSEXPLAIN = COURSEXPLAIN;
        }

        public String getSYSCREATEDATE() {
            return SYSCREATEDATE;
        }

        public void setSYSCREATEDATE(String SYSCREATEDATE) {
            this.SYSCREATEDATE = SYSCREATEDATE;
        }

        public String getH5URL() {
            return H5URL;
        }

        public void setH5URL(String H5URL) {
            this.H5URL = H5URL;
        }
    }
}
