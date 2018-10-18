package com.cb.platform.yq.api.sign.bean.impl;

import java.io.Serializable;

/**
 * 签章VO
 * @author whh
 */
public class StampVO implements Serializable{
    //id
    private String id;
    //签章图片
    private String stampImageBase64;
    //签章名称
    private String stampName;
    //签章类型
    private String stampType;
    //签章缩放
    private float stampZoom;
    //签章图片格式
    private String stampImageFormat;
    //时间戳
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStampImageBase64() {
        return stampImageBase64;
    }

    public void setStampImageBase64(String stampImageBase64) {
        this.stampImageBase64 = stampImageBase64;
    }

    public String getStampName() {
        return stampName;
    }

    public void setStampName(String stampName) {
        this.stampName = stampName;
    }

    public String getStampType() {
        return stampType;
    }

    public void setStampType(String stampType) {
        this.stampType = stampType;
    }

    public float getStampZoom() {
        return stampZoom;
    }

    public void setStampZoom(float stampZoom) {
        this.stampZoom = stampZoom;
    }

    public String getStampImageFormat() {
        return stampImageFormat;
    }

    public void setStampImageFormat(String stampImageFormat) {
        this.stampImageFormat = stampImageFormat;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
