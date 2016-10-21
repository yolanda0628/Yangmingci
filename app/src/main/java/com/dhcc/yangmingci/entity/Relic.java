package com.dhcc.yangmingci.entity;




import java.util.List;

/**
 * Created by pengbangqin on 16-10-9.
 */
public class Relic {
    /**
     * 文物的id
     */
    String rId;
    /**
     * 文物的名称
     */
    String rName;
    /**
     * 文物的描述
     */
    String rDesc;
    /**
     * 文物的级别
     */
    String rLevel;
    /**
     * 隐患数
     */
    String cCount;

    List<PicInfo> files;

    public Relic() {
    }

    public String getcCount() {
        return cCount;
    }

    public void setcCount(String cCount) {
        this.cCount = cCount;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public List<PicInfo> getFiles() {
        return files;
    }

    public void setFiles(List<PicInfo> files) {
        this.files = files;
    }

    public String getrLevel() {
        return rLevel;
    }

    public void setrLevel(String rLevel) {
        this.rLevel = rLevel;
    }

    public String getrDesc() {
        return rDesc;
    }

    public void setrDesc(String rDesc) {
        this.rDesc = rDesc;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }
}
