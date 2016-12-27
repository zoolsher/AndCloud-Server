/**
 * Created by zoolsher on 2016/12/16.
 *
 */

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.*;
import java.io.UnsupportedEncodingException;


public class MessageQueen {

    public static void main (String args[]){
        class client_task implements Runnable {
            @Override
            public void run() {

                Context ctx = ZMQ.context(1);
                Socket puller = ctx.socket(ZMQ.PULL);
                puller.connect("tcp://127.0.0.1:8765");
                String str = puller.recvStr();
                System.out.print(str);
                puller.close();
                ctx.term();
            }
        }
        Context ctx = ZMQ.context(1);
        new Thread(new client_task()).run();
        ctx.term();
    }
}
