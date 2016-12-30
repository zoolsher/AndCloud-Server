package com.safecode.andcloud.vo.message;

/**
 * Created by zoolsher on 2016/12/27.
 * @author zoolsher
 */
public class WsFromClientMessage {
    public static final String TYPE_AUTH = "auth";
    public static final String TYPE_PING = "ping";
    public static final String TYPE_CLOSE = "close";

    private String type;
    private String detail;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
