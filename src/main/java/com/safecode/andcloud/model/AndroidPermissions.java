package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * 模型类，记录Manifest中Permission的描述
 */

@Entity
@Table(name = "T_ANDROIDPERMISSIONS")
public class AndroidPermissions {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Lob
    @Column(name = "info")
    private String info;

    @Lob
    @Column(name = "description")
    private String description;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
