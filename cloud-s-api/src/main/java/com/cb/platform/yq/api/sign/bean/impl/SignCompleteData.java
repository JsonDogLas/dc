package com.cb.platform.yq.api.sign.bean.impl;

import com.cb.platform.yq.base.customsign.entity.Stamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 签名完成后的数据
 */
@ApiModel(value="签名页面返回数据",description = "返回结果")
public class SignCompleteData {

    /**
     * 下载签名文件的url
     */
    @ApiModelProperty(value = "下载签名文件的url")
    private String downloadFileUrl;

    /**
     * 印章集合
     */
    @ApiModelProperty(value = "印章集合")
    private List<StampDTO> stampList;

    public String getDownloadFileUrl() {
        return downloadFileUrl;
    }

    public void setDownloadFileUrl(String downloadFileUrl) {
        this.downloadFileUrl = downloadFileUrl;
    }

    public List<StampDTO> getStampList() {
        return stampList;
    }

    public void setStampList(List<StampDTO> stampList) {
        this.stampList = stampList;
    }
}
