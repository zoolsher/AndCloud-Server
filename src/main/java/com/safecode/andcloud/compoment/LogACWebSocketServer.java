package com.safecode.andcloud.compoment;

import com.google.gson.Gson;
import com.safecode.andcloud.model.DeviceMap;
import com.safecode.andcloud.model.Project;
import com.safecode.andcloud.model.Token;
import com.safecode.andcloud.service.ProjectService;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.vo.message.LogMessage;
import org.java_websocket.WebSocket;
import org.zeromq.ZMQ;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Created by zoolsher on 2016/12/27.
 *
 * @author zoolsher
 */
public class LogACWebSocketServer extends ACWebSocketServer {
    private ProjectService projectService;
    private ZMQ.Socket logMQSubSocket;

    public LogACWebSocketServer(int port) throws UnknownHostException {
        super(port);
        this.logMQSubSocket = SpringContextUtil.getBean("logMQSub");
        this.projectService = SpringContextUtil.getBean(ProjectService.class);
    }

    @Override
    public void onMessage(WebSocket conn, WebSocketSession session) {

    }

    @Override
    protected int onAuth(String token) {
        Token t = projectService.getTokenByName(token);
        Project p = t.getProject();
        int type = t.getType();
        Integer deviceId = projectService.getDeviceIdByProjectAndType(p, type);
        return null == deviceId ? -1 : deviceId;
    }

    @Override
    public void start() {
        super.start();
        Thread thread = new Thread(new ReceiveMQ());
        thread.start();
    }

    class ReceiveMQ implements Runnable {

        private boolean isRunning = true;

        public void stop() {
            this.isRunning = false;
        }

        @Override
        public void run() {
            Gson gson = new Gson();
            while (isRunning) {
                byte[] log = logMQSubSocket.recv();
                String logStr = new String(log);
                LogMessage logMessage = gson.fromJson(logStr, LogMessage.class);
                Integer roomid = Integer.parseInt(logMessage.getId());
                sendToRoom(roomid, logMessage.getLog());
            }
        }
    }

    public static void main(String[] args) {
        try {
            LogACWebSocketServer acWebSocketServer = new LogACWebSocketServer(6565);
            acWebSocketServer.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
