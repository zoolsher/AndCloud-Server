package com.safecode.andcloud;

import com.google.gson.Gson;
import com.safecode.andcloud.vo.message.NewWorkMessage;
import org.zeromq.ZMQ;

/**
 * 一个简单的客户端用于模拟前端向后台发送请求，先启动服务器再启动此客户端
 *
 * @author sumy
 */

public class WebPageClient {

    public static void main(String[] args) {
        final ZMQ.Context context = ZMQ.context(1);//number of ioThreads
        final ZMQ.Socket req = context.socket(ZMQ.REQ);
        req.connect("tcp://127.0.0.1:12001");
        Gson gson = new Gson();
        NewWorkMessage message = new NewWorkMessage();
        message.setProjectid(0);
        message.setType(NewWorkMessage.TYPE_AUTOMATIC);
        message.setUid(0);
        String jsonmsg = gson.toJson(message);
        req.send(jsonmsg.getBytes());
        byte[] request = req.recv();
        System.out.println(new String(request));


        NewWorkMessage message1 = new NewWorkMessage();
        message1.setProjectid(2);
        message1.setType(NewWorkMessage.TYPE_AUTOMATIC);
        message1.setUid(1);
        jsonmsg = gson.toJson(message1);
        req.send(jsonmsg.getBytes());
        request = req.recv();
        System.out.println(new String(request));
    }

}
