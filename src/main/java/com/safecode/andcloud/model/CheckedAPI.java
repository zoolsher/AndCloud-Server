package com.safecode.andcloud.model;

import com.safecode.andcloud.vo.CheckedAPIvo;

import javax.persistence.*;

/**
 * Created by sharp on 2017/1/13.
 */
@Entity
@Table(name = "T_API_CHECKED")
public class CheckedAPI extends CheckedAPIvo
{
    private int id;
    private String emulatorID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Column(name = "id_emulator", unique = true, nullable = false)
    public String getEmulatorID()
    {
        return emulatorID;
    }

    public void setEmulatorID(String emulatorID)
    {
        this.emulatorID = emulatorID;
    }

    @Override
    @Column(name = "type")
    public String getType()
    {
        return super.getType();
    }

    @Override
    public void setType(String type)
    {
        super.setType(type);
    }

    @Override
    @Column(name = "cls")
    public String getCls()
    {
        return super.getCls();
    }

    @Override
    public void setCls(String cls)
    {
        super.setCls(cls);
    }

    @Override
    @Column(name = "mtd")
    public String getMtd()
    {
        return super.getMtd();
    }

    @Override
    public void setMtd(String mtd)
    {
        super.setMtd(mtd);
    }

    @Override
    @Column(name = "args")
    public String getArgs()
    {
        return super.getArgs();
    }

    @Override
    public void setArgs(String args)
    {
        super.setArgs(args);
    }

    @Override
    @Column(name = "ret")
    public String getRet()
    {
        return super.getRet();
    }

    @Override
    public void setRet(String ret)
    {
        super.setRet(ret);
    }

    @Override
    @Column(name = "timestamp")
    public String getTimeStamp()
    {
        return super.getTimeStamp();
    }

    @Override
    public void setTimeStamp(String timeStamp)
    {
        super.setTimeStamp(timeStamp);
    }
}
