package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.customsign.entity.SignRequest;

/**
 * 签章请求
 */
public class SignRequestDTO extends SignRequest {
    private String signFileId;//文件ID-转图片需要的文件ID
    private String keyId;//key id
    private String userId;
    private String keyInfo;//key信息 判断是不是测吧的key
    private String virtualSignFileId;//虚拟签名文件ID

    public String getVirtualSignFileId() {
        return virtualSignFileId;
    }

    public void setVirtualSignFileId(String virtualSignFileId) {
        this.virtualSignFileId = virtualSignFileId;
    }

    public String getKeyInfo() {
        return keyInfo;
    }

    public void setKeyInfo(String keyInfo) {
        this.keyInfo = keyInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSignFileId() {
        return signFileId;
    }

    public void setSignFileId(String signFileId) {
        this.signFileId = signFileId;
    }
}
