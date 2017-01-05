package com.safecode.andcloud.compoment;

import com.google.gson.Gson;
import com.safecode.andcloud.model.Token;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.message.ScreenTouchMessage;
import org.java_websocket.WebSocket;
import org.zeromq.ZMQ;

import java.net.UnknownHostException;

/**
 * WebSocket服务器，用与传递控制信息
 *
 * @author sumy
 */
public class ControlACWebSocketServer extends ACWebSocketServer {

    private ProjectService projectService;
    private ZMQ.Socket screenControlMQ;
    private Gson gson = new Gson();

    public ControlACWebSocketServer(String port) throws UnknownHostException {
        super(port == null ? 8880 : Integer.parseInt(port));
        this.projectService = SpringContextUtil.getBean(ProjectService.class);
        this.screenControlMQ = SpringContextUtil.getBean(ZMQ.Socket.class, "touchMQ");
    }

    @Override
    public void onMessage(WebSocketSession session, String message) {
        Integer roomid = session.getRoomID();
        if (roomid != null && roomid != ACWebSocketServer.ROOM_ALL) {
            ScreenTouchMessage tmsg = new ScreenTouchMessage();
            tmsg.setId(roomid);
            tmsg.setMsg(message);
            screenControlMQ.send(gson.toJson(tmsg));
        }
    }

    @Override
    protected int onAuth(String token) {
        Token t = projectService.getTokenByName(token);
        Integer deviceId = projectService.getDeviceIdByProjectAndType(t.getProject(), t.getType());
        return null == deviceId ? -1 : deviceId;
    }
}
