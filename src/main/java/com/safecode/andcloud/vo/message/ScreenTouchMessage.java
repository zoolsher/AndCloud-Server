package com.safecode.andcloud.vo.message;

/**
 * 传送从前端发送的界面控制信息
 *
 * @author sumy
 */
public class ScreenTouchMessage {
    private int id;
    private String msg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
