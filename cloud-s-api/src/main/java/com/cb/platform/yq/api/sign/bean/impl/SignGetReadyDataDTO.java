package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import com.cb.platform.yq.api.sign.bean.SignGetReadyData;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 签名准备数据
 * @author whh
 */

public class SignGetReadyDataDTO implements SignGetReadyData {
    /**
     * keyId
     */
    public String keyId;

    /**
     * key验证信息
     */
    public String keyValidInfo;

    /**
     * 权限
     * 多个用 英文逗号 隔开
     */
    public String authority;

    /**
     * 页面标题
     */
    public String pageTitle;

    /**
     * 日期章缩放比率 默认为1
     */
    public float dateStampZoom;

    /**
     * 二维码缩放比率 默认为1
     */
    public float qrcodeStampZoom;

    /**
     * 上传文件路径
     */
    public String uploadFilePath;

    /**
     * token
     */
    public String token;
    /**
     * 客户端信息
     */
    public ApiClientInfoDo apiClientInfoDo;


    public SignGetReadyDataDTO() {
    }

    public SignGetReadyDataDTO(SignGetReadyDataVO signGetReadyDataVO, String token , ApiClientInfoDo apiClientInfoDo){
        this(signGetReadyDataVO,token,apiClientInfoDo,null);
    }

    public SignGetReadyDataDTO(SignGetReadyDataVO signGetReadyDataVO,String token ,ApiClientInfoDo apiClientInfoDo,String uploadFilePath){
        this.keyId = signGetReadyDataVO.getKeyId();
        this.keyValidInfo = signGetReadyDataVO.getKeyValidInfo();
        this.authority = signGetReadyDataVO.getAuthority();
        this.pageTitle = signGetReadyDataVO.getPageTitle();
        this.dateStampZoom = signGetReadyDataVO.getDateStampZoom();
        this.qrcodeStampZoom = signGetReadyDataVO.getQrcodeStampZoom();
        this.token=token;
        this.apiClientInfoDo=apiClientInfoDo;
        this.uploadFilePath=uploadFilePath;
    }

    /**
     * 签名keyId
     *
     * @return
     */
    @Override
    public String keyId() {
        return getKeyId();
    }

    /**
     * 签名key验证信息
     *
     * @return
     */
    @Override
    public String keyValidInfo() {
        return getKeyValidInfo();
    }

    /**
     * 签名的权限信息
     *
     * @return
     */
    @Override
    public List<String> authority() {
        if(StringUtils.isNotEmpty(authority)){
            String[] authorityArray=StringUtils.split(authority,",");
            List<String> list=new ArrayList<>();
            for(String authority:authorityArray){
                list.add(authority);
            }
            return list;
        }
        return null;
    }

    /**
     * 签名页面的标题
     *
     * @return
     */
    @Override
    public String pageTitle() {
        String title = getPageTitle();
        if(StringUtils.isEmpty(title)){
            String name=new File(uploadFilePath).getName();
            int position=name.lastIndexOf(".");
            if(StringUtils.isNotEmpty(name)){
                return name.substring(0,position);
            }
        }
        return title;
    }

    /**
     * 客户端ID
     *
     * @return
     */
    @Override
    public String clientId() {
        return apiClientInfoDo.getAppId();
    }

    /**
     * 日期章的缩放比例
     *
     * @return
     */
    @Override
    public float dateStampZoom() {
        return getDateStampZoom();
    }

    /**
     * 二维码章的缩放比率
     *
     * @return
     */
    @Override
    public float qrcodeStampZoom() {
        return getQrcodeStampZoom();
    }

    /**
     * token
     *
     * @return
     */
    @Override
    public String token() {
        return getToken();
    }

    /**
     * 客户信息
     *
     * @return
     */
    @Override
    public ApiClientInfoDo apiClientInfoDo() {
        return getApiClientInfoDo();
    }

    /**
     * 上传文件路径
     */
    @Override
    public String uploadFilePath() {
        return this.uploadFilePath;
    }


    public void setQrcodeStampZoom(float qrcodeStampZoom) {
        this.qrcodeStampZoom = qrcodeStampZoom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApiClientInfoDo getApiClientInfoDo() {
        return apiClientInfoDo;
    }

    public void setApiClientInfoDo(ApiClientInfoDo apiClientInfoDo) {
        this.apiClientInfoDo = apiClientInfoDo;
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

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }
}
