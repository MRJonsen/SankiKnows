package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 *
 * Created by chenbin on 2017/12/14.
 */

public class SkillMoudleItemBean  implements Serializable{


    /**
     * AutEvalname : 自主测评
     * auteval : 1
     * module_id : 7771794b-509b-4278-8c83-af1c4533ec7f
     * score : 80
     */

    private String autevalname;
    private String module_id;
    private String score;

    public String getAutEvalname() {
        return autevalname;
    }

    public void setAutEvalname(String autevalname ) {
        this.autevalname = autevalname;
    }


    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
