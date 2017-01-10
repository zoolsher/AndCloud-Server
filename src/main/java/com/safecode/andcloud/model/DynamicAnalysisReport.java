package com.safecode.andcloud.model;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by sharp on 2017/1/10.
 */

@Entity
@Table(name = "REPORT")
public class DynamicAnalysisReport
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    private String title = "Dynamic Analysis";
    private String emulator_id;
    private String pkgName;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_BASE64")
    @OrderColumn(name = "id")
    private ArrayList<String> API_BASE64 = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_FILEIO")
    @OrderColumn(name = "id")
    private ArrayList<String> API_FILEIO = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_RELECT")
    @OrderColumn(name = "id")
    private ArrayList<String> API_RELECT = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_SYSPROP")
    @OrderColumn(name = "id")
    private ArrayList<String> API_SYSPROP = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_CNTRSLVR")
    @OrderColumn(name = "id")
    private ArrayList<String> API_CNTRSLVR = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_CNTVAL")
    @OrderColumn(name = "id")
    private ArrayList<String> API_CNTVAL = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_BINDER")
    @OrderColumn(name = "id")
    private ArrayList<String> API_BINDER = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_CRYPTO")
    @OrderColumn(name = "id")
    private ArrayList<String> API_CRYPTO = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_ACNTMNGER")
    @OrderColumn(name = "id")
    private ArrayList<String> API_ACNTMNGER = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_DEVICEINFO")
    @OrderColumn(name = "id")
    private ArrayList<String> API_DEVICEINFO = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_NET")
    @OrderColumn(name = "id")
    private ArrayList<String> API_NET = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_DEXLOADER")
    @OrderColumn(name = "id")
    private ArrayList<String> API_DEXLOADER = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_CMD")
    @OrderColumn(name = "id")
    private ArrayList<String> API_CMD = new ArrayList<>();

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "REPORT_API_SM")
    @OrderColumn(name = "id")
    private ArrayList<String> API_SMS = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getEmulator_id()
    {
        return emulator_id;
    }

    public void setEmulator_id(String emulator_id)
    {
        this.emulator_id = emulator_id;
    }

    public String getPkgName()
    {
        return pkgName;
    }

    public void setPkgName(String pkgName)
    {
        this.pkgName = pkgName;
    }

    public ArrayList<String> getAPI_BASE64()
    {
        return API_BASE64;
    }

    public void setAPI_BASE64(ArrayList<String> API_BASE64)
    {
        this.API_BASE64 = API_BASE64;
    }

    public ArrayList<String> getAPI_FILEIO()
    {
        return API_FILEIO;
    }

    public void setAPI_FILEIO(ArrayList<String> API_FILEIO)
    {
        this.API_FILEIO = API_FILEIO;
    }

    public ArrayList<String> getAPI_RELECT()
    {
        return API_RELECT;
    }

    public void setAPI_RELECT(ArrayList<String> API_RELECT)
    {
        this.API_RELECT = API_RELECT;
    }

    public ArrayList<String> getAPI_SYSPROP()
    {
        return API_SYSPROP;
    }

    public void setAPI_SYSPROP(ArrayList<String> API_SYSPROP)
    {
        this.API_SYSPROP = API_SYSPROP;
    }

    public ArrayList<String> getAPI_CNTRSLVR()
    {
        return API_CNTRSLVR;
    }

    public void setAPI_CNTRSLVR(ArrayList<String> API_CNTRSLVR)
    {
        this.API_CNTRSLVR = API_CNTRSLVR;
    }

    public ArrayList<String> getAPI_CNTVAL()
    {
        return API_CNTVAL;
    }

    public void setAPI_CNTVAL(ArrayList<String> API_CNTVAL)
    {
        this.API_CNTVAL = API_CNTVAL;
    }

    public ArrayList<String> getAPI_BINDER()
    {
        return API_BINDER;
    }

    public void setAPI_BINDER(ArrayList<String> API_BINDER)
    {
        this.API_BINDER = API_BINDER;
    }

    public ArrayList<String> getAPI_CRYPTO()
    {
        return API_CRYPTO;
    }

    public void setAPI_CRYPTO(ArrayList<String> API_CRYPTO)
    {
        this.API_CRYPTO = API_CRYPTO;
    }

    public ArrayList<String> getAPI_ACNTMNGER()
    {
        return API_ACNTMNGER;
    }

    public void setAPI_ACNTMNGER(ArrayList<String> API_ACNTMNGER)
    {
        this.API_ACNTMNGER = API_ACNTMNGER;
    }

    public ArrayList<String> getAPI_DEVICEINFO()
    {
        return API_DEVICEINFO;
    }

    public void setAPI_DEVICEINFO(ArrayList<String> API_DEVICEINFO)
    {
        this.API_DEVICEINFO = API_DEVICEINFO;
    }

    public ArrayList<String> getAPI_NET()
    {
        return API_NET;
    }

    public void setAPI_NET(ArrayList<String> API_NET)
    {
        this.API_NET = API_NET;
    }

    public ArrayList<String> getAPI_DEXLOADER()
    {
        return API_DEXLOADER;
    }

    public void setAPI_DEXLOADER(ArrayList<String> API_DEXLOADER)
    {
        this.API_DEXLOADER = API_DEXLOADER;
    }

    public ArrayList<String> getAPI_CMD()
    {
        return API_CMD;
    }

    public void setAPI_CMD(ArrayList<String> API_CMD)
    {
        this.API_CMD = API_CMD;
    }

    public ArrayList<String> getAPI_SMS()
    {
        return API_SMS;
    }

    public void setAPI_SMS(ArrayList<String> API_SMS)
    {
        this.API_SMS = API_SMS;
    }
}