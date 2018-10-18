package com.cb.platform.yq.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * DO 表
 * DTO 数据传输对象
 * VO  页面表单对象
 * BO  业务对象
 * 云签接口用户 根据客户端 id + keyId  生成的用户
 */
@Entity
@Table(name = "api_user")
public class ApiUserDo  implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * keyid
     */
    @Column(name = "key_id")
    private String keyId;


    /**
     * 客户端id
     */
    @Column(name="app_id")
    private String appId;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 更新用户
     */
    @Column(name="update_user")
    private String updateUser;

    public ApiUserDo() {
    }

    public ApiUserDo(String id, String keyId, String appId, String createTime, String updateTime, String updateUser) {
        this.id = id;
        this.keyId = keyId;
        this.appId = appId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }


}
