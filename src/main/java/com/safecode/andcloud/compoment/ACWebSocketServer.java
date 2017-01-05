package com.safecode.andcloud.compoment;

import com.google.gson.Gson;
import com.safecode.andcloud.vo.message.WsFromClientMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zoolsher on 2016/12/26.
 *
 * @author zoolsher
 */
public abstract class ACWebSocketServer extends WebSocketServer {
    public abstract void onMessage(WebSocketSession session, String message);

    protected abstract int onAuth(String token);

    public static final int ROOM_ALL = -1;

    private final HashMap<Integer, List<WebSocket>> roomMap = new HashMap<>();

    @Override
    public void start() {
        super.start();
        this.runWalkPong();
    }

    private void joinRoom(Integer roomId, WebSocket conn) {
        leftRoom(conn);
        List<WebSocket> conns = roomMap.computeIfAbsent(roomId, k -> new ArrayList<>());
        conns.add(conn);
        conn.send("you join room " + roomId);
        this.setSessionForConn(conn, WebSocketSession.SESSION_KEY_ROOM, roomId);
    }

    private void leftRoom(WebSocket conn) {
        Object roomid_tmp = this.getSessionForConn(conn, WebSocketSession.SESSION_KEY_ROOM);
        if (roomid_tmp == null) {
            return;
        }
        int roomid = (int) roomid_tmp;
        synchronized (this.roomMap) {
            List<WebSocket> wss = this.roomMap.get(roomid);
            wss.remove(conn);
        }
        this.setSessionForConn(conn, WebSocketSession.SESSION_KEY_ROOM, null);
    }

    protected void sendToRoom(int roomid, String msg) {
        synchronized (roomMap) {
            List<WebSocket> wss = roomMap.get(roomid);
            if (wss != null) {
                for (WebSocket ws : wss) {
                    ws.send(msg);
                }
            }
        }
    }

    protected void sendToRoom(int roomid, byte[] data) {
        synchronized (roomMap) {
            List<WebSocket> wss = roomMap.get(roomid);
            if (wss != null) {
                for (WebSocket ws : wss) {
                    ws.send(data);
                }
            }
        }
    }

    private final HashMap<WebSocket, WebSocketSession> sessionMap = new HashMap<>();

    public void setSessionForConn(WebSocket conn, String key, Object value) {
        synchronized (this.sessionMap) {
            WebSocketSession session = this.sessionMap.computeIfAbsent(conn, k -> new WebSocketSession(conn));
            if (value == null) {
                session.unset(key);
            } else {
                session.put(key, value);
            }
        }
    }

    public Object getSessionForConn(WebSocket conn, String key) {
        synchronized (this.sessionMap) {
            WebSocketSession session = this.sessionMap.computeIfAbsent(conn, k -> new WebSocketSession(conn));
            return session.get(key);
        }
    }

    public WebSocketSession getSession(WebSocket conn) {
        synchronized (this.sessionMap) {
            return this.sessionMap.computeIfAbsent(conn, k -> new WebSocketSession(conn));
        }
    }

    public void deleteSession(WebSocket conn) {
        WebSocketSession session = this.sessionMap.get(conn);
        if (session != null) {
            this.sessionMap.remove(conn);
        }
    }

    public ACWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ACWebSocketServer(InetSocketAddress address) {
        super(address);
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        WebSocketSession session = new WebSocketSession(conn);
        synchronized (this.sessionMap) {
            this.sessionMap.put(conn, session);
            this.setSessionForConn(conn, WebSocketSession.SESSION_KEY_LASTTIME, System.currentTimeMillis());
            this.joinRoom(-1, conn);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        this.leftRoom(conn);
        this.deleteSession(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Gson json = new Gson();
        WsFromClientMessage clientMessage = null;
        try {
            clientMessage = json.fromJson(message, WsFromClientMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            onMessage(getSession(conn), message);
            return;
        }
        this.setSessionForConn(conn, WebSocketSession.SESSION_KEY_LASTTIME, System.currentTimeMillis());
        switch (clientMessage.getType()) {
            case WsFromClientMessage.TYPE_AUTH:
                this.onAuth(conn, clientMessage.getDetail());
                break;
            case WsFromClientMessage.TYPE_PING:
                this.onPing(conn);
                break;
            default:
                this.onUnKnowType(conn, clientMessage);
                break;
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    public void onAuth(WebSocket conn, String msg) {
        if (msg == null) {
            this.leftRoom(conn);
            conn.close();
        } else {
            int authResult = this.onAuth(msg);
            if (authResult > 0) {
                this.joinRoom(authResult, conn);
            } else {
                this.leftRoom(conn);
                conn.close();
            }
        }
    }

    public void onPing(WebSocket conn) {

    }

    public void onUnKnowType(WebSocket conn, WsFromClientMessage message) {

    }

    public void runWalkPong() {
        WalkerPong walkerPong = new WalkerPong();
        Thread pongThread = new Thread(walkerPong);
        pongThread.start();
    }

    class WalkerPong implements Runnable {
        public static final int LOOP_TIME = 60000;
        public static final int EXP_TIME = 120000;
        private boolean isrunning = true;

        public void stop() {
            isrunning = false;
        }

        @Override
        public void run() {
            while (isrunning) {
                synchronized (sessionMap) {
                    for (Map.Entry<WebSocket, WebSocketSession> entry : sessionMap.entrySet()) {
                        WebSocketSession session = entry.getValue();
                        if (session != null) {
                            Object time = session.get(WebSocketSession.SESSION_KEY_LASTTIME);
                            WebSocket conn = entry.getKey();
                            if (System.currentTimeMillis() - (long) time > WalkerPong.EXP_TIME) {
                                leftRoom(conn);
                                conn.close();
                            } else {
                                conn.send("{type:\"pong\"}");
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(WalkerPong.LOOP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
