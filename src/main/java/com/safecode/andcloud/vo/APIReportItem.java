package com.safecode.andcloud.vo;

import com.safecode.andcloud.model.ReportItem;

/**
 * 动态报告解析到的项目
 *
 * @author Sumy
 */
public class APIReportItem {

    private String ret;
    private String cls;
    private String mtd;
    private String args;
    private Long timestamp;
    private String ext;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public ReportItem toReportItem(String type) {
        ReportItem reportItem = new ReportItem();
        reportItem.setType(type);
        reportItem.setArgs(args);
        reportItem.setCls(cls);
        reportItem.setRet(ret);
        reportItem.setMtd(mtd);
        reportItem.setExt(ext);
        return reportItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        APIReportItem that = (APIReportItem) o;

        if (ret != null ? !ret.equals(that.ret) : that.ret != null) return false;
        if (cls != null ? !cls.equals(that.cls) : that.cls != null) return false;
        if (mtd != null ? !mtd.equals(that.mtd) : that.mtd != null) return false;
        return args != null ? args.equals(that.args) : that.args == null;
    }

    @Override
    public int hashCode() {
        int result = ret != null ? ret.hashCode() : 0;
        result = 31 * result + (cls != null ? cls.hashCode() : 0);
        result = 31 * result + (mtd != null ? mtd.hashCode() : 0);
        result = 31 * result + (args != null ? args.hashCode() : 0);
        return result;
    }
}
