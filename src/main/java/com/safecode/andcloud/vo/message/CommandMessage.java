package com.safecode.andcloud.vo.message;

/**
 * Created by sharp on 2017/1/3.
 * @author sharp
 */
public class CommandMessage
{
    public static final String COMMAND_CLOSE = "close";

    private String id;
    private String command;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }
}
