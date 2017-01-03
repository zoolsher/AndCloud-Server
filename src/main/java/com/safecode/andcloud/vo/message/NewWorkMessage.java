package com.safecode.andcloud.vo.message;

/**
 * 消息模型类 前台通过MessageQueen通知后端新任务
 */
public class NewWorkMessage {
    public static final Integer TYPE_AUTOMATIC = 0; // 自动进行任务
    public static final Integer TYPE_HAND = 1; // 手动任务

    public static final Integer TIME_INFINITY = 0;
    public static final Integer TIME_FIVE_MINS = 1;
    public static final Integer TIME_TEN_MINS = 2;

    private Integer projectid;
    private Integer type;
    private Integer uid;
    private int time;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
