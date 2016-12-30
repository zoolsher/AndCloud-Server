package com.safecode.andcloud.compoment;

import org.java_websocket.WebSocket;

import java.net.UnknownHostException;

/**
 * WebSocket服务器，用与传递控制信息
 *
 * @author sumy
 */
public class ControlACWebSocketServer extends ACWebSocketServer {

    public ControlACWebSocketServer(String port) throws UnknownHostException {
        super(port == null ? 8880 : Integer.parseInt(port));
    }

    @Override
    public void onMessage(WebSocket conn, WebSocketSession session) {

    }

    @Override
    protected int onAuth(String token) {
        return 0;
    }
}
