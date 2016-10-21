package com.dhcc.yangmingci.entity;

import java.util.List;

/**
 * 检查记录实体类
 * Created by pengbangqin on 16-10-9.
 */
public class CheckInfo {
    /**
     * 用户id
     */
    String uId;
    /**
     * 检查时间
     */
    String cDatetime;
    /**
     * 检查图片信息
     */
    List<PicInfo> files;
    /**
     * 检查内容
     */
    String cDesc;
    /**
     * 检查类型
     */
    String cType;
    /**
     * 检查记录id
     */
    String cId;
    /**
     * 文物id
     */
    String rId;
    /**
     * 文物name
     */
    String rName;
    /**
     * 整改状态
     */
    String cStatus;

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getcDatetime() {
        return cDatetime;
    }

    public void setcDatetime(String cDatetime) {
        this.cDatetime = cDatetime;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public List<PicInfo> getFiles() {
        return files;
    }

    public void setFiles(List<PicInfo> files) {
        this.files = files;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }
}
