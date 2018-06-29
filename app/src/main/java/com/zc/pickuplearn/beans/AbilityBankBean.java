package com.zc.pickuplearn.beans;

import java.util.List;

/**
 *
 * Created by chenbin on 2017/8/17.
 */

public class AbilityBankBean {

    /**
     * done_num : 0
     * module_list : {"DATAS":[{"MODULE_ID":"8f83726f-1a5a-4aa3-b385-5dda21ef20cf","MODULE_NAME":"2017年继电保护普考题库"}],"ROWS":1}
     * right_rate : 0
     * time_pass : 0
     */

    private int done_num;
    private ModuleListBean module_list;
    private int right_rate;
    private int time_pass;

    public int getDone_num() {
        return done_num;
    }

    public void setDone_num(int done_num) {
        this.done_num = done_num;
    }

    public ModuleListBean getModule_list() {
        return module_list;
    }

    public void setModule_list(ModuleListBean module_list) {
        this.module_list = module_list;
    }

    public int getRight_rate() {
        return right_rate;
    }

    public void setRight_rate(int right_rate) {
        this.right_rate = right_rate;
    }

    public int getTime_pass() {
        return time_pass;
    }

    public void setTime_pass(int time_pass) {
        this.time_pass = time_pass;
    }

    public static class ModuleListBean {
        /**
         * DATAS : [{"MODULE_ID":"8f83726f-1a5a-4aa3-b385-5dda21ef20cf","MODULE_NAME":"2017年继电保护普考题库"}]
         * ROWS : 1
         */

        private int ROWS;
        private List<DATASBean> DATAS;

        public int getROWS() {
            return ROWS;
        }

        public void setROWS(int ROWS) {
            this.ROWS = ROWS;
        }

        public List<DATASBean> getDATAS() {
            return DATAS;
        }

        public void setDATAS(List<DATASBean> DATAS) {
            this.DATAS = DATAS;
        }

        public static class DATASBean {
            /**
             * MODULE_ID : 8f83726f-1a5a-4aa3-b385-5dda21ef20cf
             * MODULE_NAME : 2017年继电保护普考题库
             */

            private String MODULE_ID;
            private String MODULE_NAME;

            public String getMODULE_ID() {
                return MODULE_ID;
            }

            public void setMODULE_ID(String MODULE_ID) {
                this.MODULE_ID = MODULE_ID;
            }

            public String getMODULE_NAME() {
                return MODULE_NAME;
            }

            public void setMODULE_NAME(String MODULE_NAME) {
                this.MODULE_NAME = MODULE_NAME;
            }
        }
    }
}
