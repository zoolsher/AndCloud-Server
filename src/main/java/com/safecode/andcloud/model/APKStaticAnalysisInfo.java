package com.safecode.andcloud.model;

import javax.persistence.*;

/**
 * 模型类，存储静态分析的结果
 *
 * @author Sumy
 */
@Entity
@Table(name = "T_APKSTATICANALYSISINFO")
public class APKStaticAnalysisInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "activitiesnum")
    private Integer activitiesNum;

    @Column(name = "exportedactivitiesnum")
    private Integer exportedActivitiesNum;

    @Lob
    @Column(name = "activities")
    private String activities;

    @Column(name = "servicesnum")
    private Integer servicesNum;

    @Column(name = "exportedservicesnum")
    private Integer exportedServicesNum;

    @Lob
    @Column(name = "services")
    private String services;

    @Column(name = "receiversnum")
    private Integer receiversNum;

    @Column(name = "exportedreceiversnum")
    private Integer exportedReceiversNum;

    @Lob
    @Column(name = "receivers")
    private String receivers;

    @Column(name = "providersnum")
    private Integer providersNum;

    @Column(name = "exportedprovidersnum")
    private Integer exportedProvidersNum;

    @Lob
    @Column(name = "providers")
    private String providers;

    @Lob
    @Column(name = "signercertificate")
    private String signerCertificate;

    @Column(name = "certissued")
    private String certIssued;

    @Lob
    @Column(name = "permissions")
    private String permissions;

    @Lob
    @Column(name = "files")
    private String files;

    @Lob
    @Column(name = "manifest")
    private String manifest;

    @OneToOne
    @JoinColumn(name = "apkinfoid")
    private APKInfo apkInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getActivitiesNum() {
        return activitiesNum;
    }

    public void setActivitiesNum(Integer activitiesNum) {
        this.activitiesNum = activitiesNum;
    }

    public Integer getExportedActivitiesNum() {
        return exportedActivitiesNum;
    }

    public void setExportedActivitiesNum(Integer exportedActivitiesNum) {
        this.exportedActivitiesNum = exportedActivitiesNum;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public Integer getServicesNum() {
        return servicesNum;
    }

    public void setServicesNum(Integer servicesNum) {
        this.servicesNum = servicesNum;
    }

    public Integer getExportedServicesNum() {
        return exportedServicesNum;
    }

    public void setExportedServicesNum(Integer exportedServicesNum) {
        this.exportedServicesNum = exportedServicesNum;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Integer getReceiversNum() {
        return receiversNum;
    }

    public void setReceiversNum(Integer receiversNum) {
        this.receiversNum = receiversNum;
    }

    public Integer getExportedReceiversNum() {
        return exportedReceiversNum;
    }

    public void setExportedReceiversNum(Integer exportedReceiversNum) {
        this.exportedReceiversNum = exportedReceiversNum;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public Integer getProvidersNum() {
        return providersNum;
    }

    public void setProvidersNum(Integer providersNum) {
        this.providersNum = providersNum;
    }

    public Integer getExportedProvidersNum() {
        return exportedProvidersNum;
    }

    public void setExportedProvidersNum(Integer exportedProvidersNum) {
        this.exportedProvidersNum = exportedProvidersNum;
    }

    public String getProviders() {
        return providers;
    }

    public void setProviders(String providers) {
        this.providers = providers;
    }

    public String getSignerCertificate() {
        return signerCertificate;
    }

    public void setSignerCertificate(String signerCertificate) {
        this.signerCertificate = signerCertificate;
    }

    public String getCertIssued() {
        return certIssued;
    }

    public void setCertIssued(String certIssued) {
        this.certIssued = certIssued;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public APKInfo getApkInfo() {
        return apkInfo;
    }

    public void setApkInfo(APKInfo apkInfo) {
        this.apkInfo = apkInfo;
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }
}
