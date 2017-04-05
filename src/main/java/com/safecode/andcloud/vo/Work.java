package com.safecode.andcloud.vo;

import com.safecode.andcloud.vo.message.NewWorkMessage;

/**
 * 包含任务信息，分发给Worker
 *
 * @author sumy
 */

public class Work {

    public static final Integer TYPE_AUTOMATIC = NewWorkMessage.TYPE_AUTOMATIC; // 自动进行任务
    public static final Integer TYPE_HAND = NewWorkMessage.TYPE_HAND; // 手动任务

    private Integer projectid;
    private Integer type;
    private Integer uid;
    private Integer time;
    private Integer imageid;

    public Work(NewWorkMessage message) {
        this.projectid = message.getProjectid();
        this.type = message.getType();
        this.uid = message.getUid();
        this.imageid = message.getImageid();
        this.time = message.getTime();
        if (this.time < 5) {
            this.time = 5;
        }
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

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getImageid() {
        return imageid;
    }

    public void setImageid(Integer imageid) {
        this.imageid = imageid;
    }
}
