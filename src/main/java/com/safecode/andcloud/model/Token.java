package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * Created by zoolsher on 2016/12/28.
 *
 * @auther zoolsher
 */
@Entity
@Table(name = "T_TOKEN")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

    @OneToOne
    @JoinColumn(name = "projectid")
    private Project project;

    @OneToOne
    @JoinColumn(name = "imageid")
    private MirrorImage mirrorImage;

    @Column(name = "type")
    private int type;

    @Column(name = "token")
    private String token;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MirrorImage getMirrorImage() {
        return mirrorImage;
    }

    public void setMirrorImage(MirrorImage mirrorImage) {
        this.mirrorImage = mirrorImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
