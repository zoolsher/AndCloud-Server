package com.safecode.andcloud.compoment;

import com.google.gson.Gson;
import com.safecode.andcloud.model.SimulatorDomain;
import com.safecode.andcloud.service.MirrorService;
import com.safecode.andcloud.vo.message.CommandMessage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import java.util.List;

/**
 * Created by sharp on 2017/1/3.
 * @author sharp
 */

@Component
public class EmulatorManageTask
{
    @Autowired
    private MirrorService mirrorService;
    @Autowired
    @Qualifier("controlMQ")
    private ZMQ.Socket commandMQ;

    @Scheduled(fixedRate = 60000)
    public void notifyEmulatorToClose()
    {
        List<SimulatorDomain> unDeleteSimulator = mirrorService.findUndeleteSimulator();
        Gson gson = new Gson();
        for (SimulatorDomain simulator : unDeleteSimulator)
        {
            if (simulator.getDeadlinetime().isAfterNow())
            {
                CommandMessage cmd = new CommandMessage();
                cmd.setId(simulator.getId() + "");
                cmd.setCommand(CommandMessage.COMMAND_CLOSE);
                commandMQ.send(gson.toJson(cmd));
            }
        }
    }
}
