package com.safecode.andcloud.compoment;

import org.java_websocket.WebSocket;

import java.util.HashMap;

/**
 * Created by zoolsher on 2016/12/26.
 *
 * @author zoolsher
 */
public class WebSocketSession {
    public static final String SESSION_KEY_ROOM = "room";
    public static final String SESSION_KEY_LASTTIME = "last_active_time";
    private HashMap<String, Object> session = new HashMap<>();
    private WebSocket connection = null;

    public WebSocketSession(WebSocket conn) {
        connection = conn;
    }

    public void put(String key, Object value) {
        this.session.put(key, value);
    }

    public Object get(String key) {
        return this.session.get(key);
    }

    public void unset(String key) {
        this.session.remove(key);
    }

    public WebSocket getConnection() {
        return connection;
    }

    public Integer getRoomID() {
        return (Integer) this.session.get(SESSION_KEY_ROOM);
    }
}
