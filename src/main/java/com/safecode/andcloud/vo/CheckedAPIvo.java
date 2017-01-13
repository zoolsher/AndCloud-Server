package com.safecode.andcloud.vo;

/**
 * Created by sharp on 2017/1/12.
 */
public class CheckedAPIvo
{
    private String type;
    private String cls;
    private String mtd;
    private String args;
    private String ret;
    private String timeStamp;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getCls()
    {
        return cls;
    }

    public void setCls(String cls)
    {
        this.cls = cls;
    }

    public String getMtd()
    {
        return mtd;
    }

    public void setMtd(String mtd)
    {
        this.mtd = mtd;
    }

    public String getArgs()
    {
        return args;
    }

    public void setArgs(String args)
    {
        this.args = args;
    }

    public String getRet()
    {
        return ret;
    }

    public void setRet(String ret)
    {
        this.ret = ret;
    }

    public String getTimeStamp()
    {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
