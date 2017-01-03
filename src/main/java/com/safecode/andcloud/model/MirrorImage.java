package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * 记录根镜像配置，需手动添加。
 *
 * @author Sumy
 */
@Entity
@Table(name = "T_MIRRORIMAGE")
public class MirrorImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "note")
    private String note;

    @Column(name = "wmheight", nullable = false)
    private int wmheight; // 显示高度

    @Column(name = "wmwidth", nullable = false)
    private int wmwidth; // 显示宽度

    @Column(name = "height", nullable = false)
    private int height; // 屏幕高度

    @Column(name = "width", nullable = false)
    private int width; // 屏幕宽度

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getWmheight() {
        return wmheight;
    }

    public void setWmheight(int wmheight) {
        this.wmheight = wmheight;
    }

    public int getWmwidth() {
        return wmwidth;
    }

    public void setWmwidth(int wmwidth) {
        this.wmwidth = wmwidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
