package com.cb.platform.yq.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 页面交互对象
 * @author whh
 */
@ApiModel(value="com.cb.platform.yq.api.vo.ApiUserVo",description = "云签系统用户对象")
public class ApiUserVo implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id" ,required=true)
    private String id;

    /**
     * keyid
     */
    @ApiModelProperty(value="keyId" ,required=true)
    private String keyId;


    /**
     * 客户端id
     */
    @ApiModelProperty(value="客户端id" ,required=true)
    private String appId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
