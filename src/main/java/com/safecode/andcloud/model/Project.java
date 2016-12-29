package com.safecode.andcloud.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * 实体类 工程条目
 *
 * @author sumy
 */
@Entity
@Table(name = "T_PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "uploadtime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime uploadtime; // 上传时间

    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "logo")
    private String logo;

    @Column(name = "package")
    private String packageName;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DateTime getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(DateTime uploadtime) {
        this.uploadtime = uploadtime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
