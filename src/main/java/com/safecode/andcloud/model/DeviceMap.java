package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * 实体类 DEVICEMAP
 *
 * @author zoolsher
 */
@Entity
@Table(name = "T_DEVICEMAP")
public class DeviceMap {
    public static final Integer TYPE_AUTO_CHECK = 0;
    public static final Integer TYPE_HAND_CHECK = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "projectid")
    private Project project;

    @OneToOne
    @JoinColumn(name = "deviceid")
    private SimulatorDomain simulatorDomain;

    @Column(name = "type")
    private Integer type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public SimulatorDomain getSimulatorDomain() {
        return simulatorDomain;
    }

    public void setSimulatorDomain(SimulatorDomain simulatorDomain) {
        this.simulatorDomain = simulatorDomain;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
