package com.cb.platform.yq.api.sign.bean.impl;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 签名准备数据
 * @author whh
 */
@ApiModel(value="com.cb.platform.yq.api.sign.bean.impl.SignGetReadyDataVO",description = "返回结果")
public class SignGetReadyDataVO {


    /**
     * 签名文件
     */
    @ApiModelProperty(value = "上传的文件")
    private MultipartFile multiUploadFile;

    /**
     * keyId
     */
    @ApiModelProperty(value = "keyId")
    public String keyId;

    /**
     * key验证信息
     */
    @ApiModelProperty(value = "key身份验证信息")
    public String keyValidInfo;

    /**
     * 权限
     * 多个用 英文逗号 隔开
     */
    @ApiModelProperty(value = "签名页面权限，多个用 英文逗号 隔开 不传默认全部开启")
    public String authority;

    /**
     * 页面标题
     */
    @ApiModelProperty(value = "页面标题 默认为文件名（没有文件后缀）")
    public String pageTitle;

    /**
     * 日期章缩放比率 默认为1
     */
    @ApiModelProperty(value = "日期章缩放比率 默认为1 缩放比率 1=75px")
    public float dateStampZoom;

    /**
     * 二维码缩放比率 默认为1
     */
    @ApiModelProperty(value = "二维码缩放比率 默认为1 缩放比率 1=75px")
    public float qrcodeStampZoom;



    public SignGetReadyDataVO() {
    }

    public SignGetReadyDataVO(MultipartFile multiUploadFile, String keyId, String keyValidInfo, String authority, String pageTitle, Float dateStampZoom, Float qrcodeStampZoom) {
        this.multiUploadFile = multiUploadFile;
        this.keyId = keyId;
        this.keyValidInfo = keyValidInfo;
        this.authority = authority;
        this.pageTitle = pageTitle;
        this.dateStampZoom = dateStampZoom == null ? 0 : dateStampZoom;
        this.qrcodeStampZoom = qrcodeStampZoom == null ? 0 : qrcodeStampZoom;
    }

    public MultipartFile getMultiUploadFile() {
        return multiUploadFile;
    }

    public void setMultiUploadFile(MultipartFile multiUploadFile) {
        this.multiUploadFile = multiUploadFile;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyValidInfo() {
        return keyValidInfo;
    }

    public void setKeyValidInfo(String keyValidInfo) {
        this.keyValidInfo = keyValidInfo;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public float getDateStampZoom() {
        return dateStampZoom;
    }

    public void setDateStampZoom(float dateStampZoom) {
        this.dateStampZoom = dateStampZoom;
    }

    public float getQrcodeStampZoom() {
        return qrcodeStampZoom;
    }

    public void setQrcodeStampZoom(float qrcodeStampZoom) {
        this.qrcodeStampZoom = qrcodeStampZoom;
    }
}
