/**
 * Created by zoolsher on 2016/12/16.
 *
 */

import org.zeromq.ZMQ;
import java.io.UnsupportedEncodingException;


public class MessageQueen {
    public static void main (String args[]){

        final ZMQ.Context context = ZMQ.context(1);//number of ioThreads
        final ZMQ.Socket puller = context.socket(ZMQ.PULL);
        puller.setIdentity("and-cloud-puller".getBytes());
//        puller.bind("tcp://127.0.0.1:8765");
        puller.connect("tcp://127.0.0.1:8765");
        while(true) {
            System.out.println("once");
            byte[] b = puller.recv();
            try {
                String s = new String(b,"UTF-8");
                System.out.println(s);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
