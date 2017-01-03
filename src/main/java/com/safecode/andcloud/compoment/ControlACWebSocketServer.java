package com.safecode.andcloud.compoment;

import com.safecode.andcloud.model.Token;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.SpringContextUtil;
import org.java_websocket.WebSocket;

import java.net.UnknownHostException;

/**
 * WebSocket服务器，用与传递控制信息
 *
 * @author sumy
 */
public class ControlACWebSocketServer extends ACWebSocketServer {

    private ProjectService projectService;

    public ControlACWebSocketServer(String port) throws UnknownHostException {
        super(port == null ? 8880 : Integer.parseInt(port));
        projectService = SpringContextUtil.getBean(ProjectService.class);
    }

    @Override
    public void onMessage(WebSocket conn, WebSocketSession session) {

    }

    @Override
    protected int onAuth(String token) {
        Token t = projectService.getTokenByName(token);
        Integer deviceId = projectService.getDeviceIdByProjectAndType(t.getProject(), t.getType());
        return null == deviceId ? -1 : deviceId;
    }
}
