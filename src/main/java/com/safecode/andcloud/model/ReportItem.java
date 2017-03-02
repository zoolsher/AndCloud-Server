package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * Created by sharp on 2017/1/10.
 */

@Entity
@Table(name = "T_REPORT")
public class ReportItem {
    public static final String TYPE_API_BASE64 = "API_BASE64";
    public static final String TYPE_API_FILEIO = "API_FILEIO";
    public static final String TYPE_API_RELECT = "API_RELECT";
    public static final String TYPE_API_SYSPROP = "API_SYSPROP";
    public static final String TYPE_API_CNTRSLVR = "API_CNTRSLVR";
    public static final String TYPE_API_CNTVAL = "API_CNTVAL";
    public static final String TYPE_API_BINDER = "API_BINDER";
    public static final String TYPE_API_CRYPTO = "API_CRYPTO";
    public static final String TYPE_API_ACNTMNGER = "API_ACNTMNGER";
    public static final String TYPE_API_DEVICEINFO = "API_DEVICEINFO";
    public static final String TYPE_API_NET = "API_NET";
    public static final String TYPE_API_DEXLOADER = "API_DEXLOADER";
    public static final String TYPE_API_CMD = "API_CMD";
    public static final String TYPE_API_SMS = "API_SMS";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Lob
    @Column(name = "ret")
    private String ret;

    @Lob
    @Column(name = "cls")
    private String cls;

    @Lob
    @Column(name = "mtd")
    private String mtd;

    @Lob
    @Column(name = "args")
    private String args;

    @Lob
    @Column(name = "ext")
    private String ext;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "type")
    private String type;

    @OneToOne
    @JoinColumn(name = "simulatorid")
    private SimulatorDomain simulatorDomain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getMtd() {
        return mtd;
    }

    public void setMtd(String mtd) {
        this.mtd = mtd;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public SimulatorDomain getSimulatorDomain() {
        return simulatorDomain;
    }

    public void setSimulatorDomain(SimulatorDomain simulatorDomain) {
        this.simulatorDomain = simulatorDomain;
    }
}