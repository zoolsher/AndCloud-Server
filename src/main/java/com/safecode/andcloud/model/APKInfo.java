package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * 模型类，在数据库中存储APK文件的信息
 *
 * @author sumy
 */
@Entity
@Table(name = "T_APKINFO")
public class APKInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "size")
    private long size;

    @Column(name = "md5")
    private String md5;

    @Column(name = "sha1")
    private String sha1;

    @Column(name = "sha256")
    private String sha256;

    @Column(name = "packagename")
    private String packagename;

    @Column(name = "mainactivity")
    private String mainactivity;

    @Column(name = "targetsdk")
    private String targetsdk;

    @Column(name = "minsdk")
    private String minsdk;

    @Column(name = "maxsdk")
    private String maxsdk;

    @Column(name = "versionname")
    private String versionname;

    @Column(name = "versioncode")
    private String versioncode;

    @Lob
    @Column(name = "iconimg")
    private String iconimg;

    @Column(name = "icon")
    private String icon;

    @Column(name = "label")
    private String label;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getMainactivity() {
        return mainactivity;
    }

    public void setMainactivity(String mainactivity) {
        this.mainactivity = mainactivity;
    }

    public String getTargetsdk() {
        return targetsdk;
    }

    public void setTargetsdk(String targetsdk) {
        this.targetsdk = targetsdk;
    }

    public String getMinsdk() {
        return minsdk;
    }

    public void setMinsdk(String minsdk) {
        this.minsdk = minsdk;
    }

    public String getMaxsdk() {
        return maxsdk;
    }

    public void setMaxsdk(String maxsdk) {
        this.maxsdk = maxsdk;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getIconimg() {
        return iconimg;
    }

    public void setIconimg(String iconimg) {
        this.iconimg = iconimg;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
