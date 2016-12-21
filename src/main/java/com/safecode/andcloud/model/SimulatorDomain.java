package com.safecode.andcloud.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 实体类 创建的虚拟机
 *
 * @author sumy
 */
@Entity
@Table(name = "T_SIMULATORDOMAIN")
public class SimulatorDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "createtime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createtime; // 模拟器创建时间

    @Column(name = "uuid", nullable = false)
    private String uuid; // 模拟器内部uuid编号

    @Column(name = "mac", nullable = false)
    private String mac; // 模拟器mac地址

    @Column(name = "imagepath", nullable = false)
    private String imagepath; // 模拟器镜像绝对地址

    @Column(name = "isdelete", nullable = false)
    private boolean isdelete; // 是否已删除

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(DateTime createtime) {
        this.createtime = createtime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public boolean isIsdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }
}
