package com.safecode.andcloud.worker;

import com.google.gson.Gson;
import com.safecode.andcloud.service.ADBService;
import com.safecode.andcloud.util.SpringContextUtil;
import com.safecode.andcloud.util.screentouch.DefaultInstructionParser;
import com.safecode.andcloud.util.screentouch.EvTouchInstructionParser;
import com.safecode.andcloud.util.screentouch.InstructionParser;
import com.safecode.andcloud.vo.EmulatorParameter;
import com.safecode.andcloud.vo.message.ScreenTouchMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.zeromq.ZMQ;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * 将前台发送的坐标转换到模拟器上
 *
 * @author sumy
 */
public class ScreenTouchWorker extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(ScreenTouchWorker.class);

    private boolean isstop = false;
    private final String identify;
    private final int emulatorId;
    private final EmulatorParameter parameter;

    private Gson gson = new Gson();
    private InstructionParser parser = new DefaultInstructionParser();

    private ADBService adbService;
    private Environment environment;

    public ScreenTouchWorker(String identify, int eid, EmulatorParameter parameter) {
        this.identify = identify;
        this.emulatorId = eid;
        this.parameter = parameter;
        this.parser = new EvTouchInstructionParser(parameter.getScreenwidth(), parameter.getScreenheight(),
                parameter.getWmwidth(), parameter.getWmheight(), parameter.getMaxxpixel(), parameter.getMaxypixel());

        this.adbService = SpringContextUtil.getBean(ADBService.class);
        this.environment = SpringContextUtil.getBean(Environment.class);
    }


    @Override
    public void run() {
        Process process = adbService.getShellRuntime(this.identify);
        if (process == null) {
            logger.error("Can't got shell runtime for emulator-" + this.emulatorId + " with identify " + this.identify + ". Exit.");
            return;
        }
        OutputStream outputStream = process.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        ZMQ.Context ctx = ZMQ.context(1);
        ZMQ.Socket socket = ctx.socket(ZMQ.SUB);
        String endpoint = environment.getRequiredProperty("mq.touch.endpoint");
        socket.connect(endpoint);
        socket.subscribe("".getBytes());

        while (!isstop) {
            String recvdata = null;
            ScreenTouchMessage message = null;
            while (!isstop) {
                byte[] data = socket.recv();
                recvdata = new String(data);
                message = gson.fromJson(recvdata, ScreenTouchMessage.class);
                if (message.getId() == this.emulatorId) {
                    break;
                }
            }
            if (isstop) {
                break;
            }
            List<String> instructions = parser.parse(message.getMsg());
            for (String inst : instructions) {
                String touchcommand = "sendevent " + parameter.getTouchdevice() + " " + inst;
                logger.debug("touch command: " + touchcommand);
                writer.println(touchcommand);
                writer.flush();
            }
        }

        socket.close();
        process.destroy();

    }

    public void stopme() {
        this.isstop = true;
    }
}
