package com.dhcc.yangmingci.entity;

/**
 * 图片信息
 * Created by pengbangqin on 16-10-12.
 */
public class PicInfo {
    /**
     * 图片id
     */
    String fId;
    /**
     * 图片路径
     */
    String fPath;
    /**
     * 图片类型
     */
    String fType;
    int width;
    int height;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getfPath() {
        return fPath;
    }

    public void setfPath(String fPath) {
        this.fPath = fPath;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
