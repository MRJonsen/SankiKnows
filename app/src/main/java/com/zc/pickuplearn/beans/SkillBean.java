package com.zc.pickuplearn.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 能力项
 * Created by chenbin on 2017/12/13.
 */

public class SkillBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * ABILITYCODE : X05U02
         * ABILITYNAME : 低压光伏电源并网验收
         * ABILITYTYPE : X05U02
         * ABILITYTYPECODE : E
         * ABILITY_LEVEL : III
         * ABILITY_LEVEL_DESC : 等级3
         * PRACTICALWEIGHT : 21.6
         */

        private String ABILITYCODE;
        private String ABILITYNAME;
        private String ABILITYTYPE;
        private String ABILITYTYPECODE;
        private String ABILITY_LEVEL;
        private String ABILITY_LEVEL_DESC;
        private String PRACTICALWEIGHT;

        public String getABILITYCODE() {
            return ABILITYCODE;
        }

        public void setABILITYCODE(String ABILITYCODE) {
            this.ABILITYCODE = ABILITYCODE;
        }

        public String getABILITYNAME() {
            return ABILITYNAME;
        }

        public void setABILITYNAME(String ABILITYNAME) {
            this.ABILITYNAME = ABILITYNAME;
        }

        public String getABILITYTYPE() {
            return ABILITYTYPE;
        }

        public void setABILITYTYPE(String ABILITYTYPE) {
            this.ABILITYTYPE = ABILITYTYPE;
        }

        public String getABILITYTYPECODE() {
            return ABILITYTYPECODE;
        }

        public void setABILITYTYPECODE(String ABILITYTYPECODE) {
            this.ABILITYTYPECODE = ABILITYTYPECODE;
        }

        public String getABILITY_LEVEL() {
            return ABILITY_LEVEL;
        }

        public void setABILITY_LEVEL(String ABILITY_LEVEL) {
            this.ABILITY_LEVEL = ABILITY_LEVEL;
        }

        public String getABILITY_LEVEL_DESC() {
            return ABILITY_LEVEL_DESC;
        }

        public void setABILITY_LEVEL_DESC(String ABILITY_LEVEL_DESC) {
            this.ABILITY_LEVEL_DESC = ABILITY_LEVEL_DESC;
        }

        public String getPRACTICALWEIGHT() {
            return PRACTICALWEIGHT;
        }

        public void setPRACTICALWEIGHT(String PRACTICALWEIGHT) {
            this.PRACTICALWEIGHT = PRACTICALWEIGHT;
        }
    }
}
