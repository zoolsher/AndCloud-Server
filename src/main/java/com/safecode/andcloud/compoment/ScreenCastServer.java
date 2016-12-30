package com.safecode.andcloud.compoment;

import com.safecode.andcloud.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 一个Socket服务器，用于接收模拟器发送的手机截屏 <br>
 * https://github.com/ajinabraham/Mobile-Security-Framework-MobSF/blob/master/DynamicAnalyzer/views/android.py#L1088
 *
 * @author sumy
 */
public class ScreenCastServer {
    private final static Logger logger = LoggerFactory.getLogger(ScreenCastServer.class);

    public ControlACWebSocketServer webSocketServer;

    public SelectorLoop connectionBell;
    //public SelectorLoop readBell;

    public final String serverip;
    public final int serverport;
    
    public ScreenCastServer(String ip, String port) {
        this.serverip = ip;
        this.serverport = Integer.parseInt(port);
        this.webSocketServer = SpringContextUtil.getBean(ControlACWebSocketServer.class);
    }

    public void start() throws IOException {
        connectionBell = new SelectorLoop();
        //readBell = new SelectorLoop();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress(serverip, serverport));
        ssc.register(connectionBell.getSelector(), SelectionKey.OP_ACCEPT);
        new Thread(connectionBell).start(); // 处理连接
        logger.info("Start ScreenCast server on " + this.serverip + ":" + this.serverport);
    }


    class SelectorLoop implements Runnable {
        private Selector selector;
        private ByteBuffer temp = ByteBuffer.allocate(1024);

        public SelectorLoop() throws IOException {
            this.selector = Selector.open();
        }

        public Selector getSelector() {
            return this.selector;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        this.dispatch(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void dispatch(SelectionKey key) throws IOException {
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                // register 会阻塞 等待 selector() 返回
                sc.register(connectionBell.getSelector(), SelectionKey.OP_READ);
                logger.debug("accept connect " + sc.socket().getInetAddress().toString());
            } else if (key.isReadable()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                SocketChannel sc = (SocketChannel) key.channel();
                temp.clear();

                while (true) {
                    int count = sc.read(temp);
                    if (count > 0) {
                        temp.flip();
                        byte[] b = new byte[count];
                        temp.get(b);
                        baos.write(b);
                        temp.clear();
                    } else if (count == 0) {
                        // pass
                    } else {
                        handle(baos.toByteArray());
                        logger.debug("read screen data from " + sc.socket().getInetAddress().toString() + ": " + baos.toByteArray().length);
                        key.cancel();
                        sc.close();
                        return;
                    }
                }
            }
        }
    }

    public void handle(byte[] data) {
        this.webSocketServer.sendToRoom(ACWebSocketServer.ROOM_ALL, data);
    }
}
