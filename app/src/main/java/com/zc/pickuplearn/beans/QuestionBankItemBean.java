package com.zc.pickuplearn.beans;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by chenbin on 2017/12/14.
 */

public class QuestionBankItemBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * done_num : 0
         * module_code : D-06-05-002
         * module_id : 47599262-3e6a-45e9-83f2-1e8d53f8e583
         * module_name : 晋升型
         * right_rate : 0
         * time_pass : 0
         */

        private int done_num;
        private String module_code;
        private String module_id;
        private String module_name;
        private int right_rate;
        private int time_pass;

        public int getDone_num() {
            return done_num;
        }

        public void setDone_num(int done_num) {
            this.done_num = done_num;
        }

        public String getModule_code() {
            return module_code;
        }

        public void setModule_code(String module_code) {
            this.module_code = module_code;
        }

        public String getModule_id() {
            return module_id;
        }

        public void setModule_id(String module_id) {
            this.module_id = module_id;
        }

        public String getModule_name() {
            return module_name;
        }

        public void setModule_name(String module_name) {
            this.module_name = module_name;
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
    }
}
