package com.cb.platform.yq.api.sign.bean;

import com.cb.platform.yq.api.entity.ApiClientInfoDo;
import org.apache.commons.fileupload.FileItem;

import java.util.List;

/**
 * 签名准备数据
 * @author whh
 */
public interface SignGetReadyData {
    /**
     * 签名keyId
     * @return
     */
    String keyId();

    /**
     * 签名key验证信息
     * @return
     */
    String keyValidInfo();


    /**
     * 签名的权限信息
     * @return
     */
    List<String> authority();


    /**
     * 签名页面的标题
     * @return
     */
    String pageTitle();

    /**
     * 客户端ID
     * @return
     */
    String clientId();


    /**
     * 日期章的缩放比例
     * @return
     */
    float dateStampZoom();


    /**
     * 二维码章的缩放比率
     * @return
     */
    float qrcodeStampZoom();

    /**
     * token
     * @return
     */
    String token();


    /**
     * 客户信息
     * @return
     */
    ApiClientInfoDo apiClientInfoDo();

    /**
     * 上传文件路径
     */
    String uploadFilePath();


}
