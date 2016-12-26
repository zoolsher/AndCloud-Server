package com.safecode.andcloud.vo;

import com.safecode.andcloud.vo.message.NewWorkMessage;

/**
 * 包含任务信息，分发给Worker
 *
 * @author sumy
 */

public class Work {

    public static final Integer TYPE_AUTOMATIC = 0; // 自动进行任务
    public static final Integer TYPE_HAND = 1; // 手动任务

    private Integer projectid;
    private Integer type;
    private Integer uid;

    public Work(NewWorkMessage message) {
        this.projectid = message.getProjectid();
        this.type = message.getType();
        this.uid = message.getUid();
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public boolean isvalid() {
        return projectid != null && type != null && uid != null;
    }

}
