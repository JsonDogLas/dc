package com.cb.platform.yq.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 客户端其他信息
 */
@Entity
@Table(name = "api_client_info")
public class ApiClientInfoDo implements Serializable {
    /**
     * 签名文件id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 客户端id
     */
    @Column(name = "app_id")
    private String appId;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 云签API印章来源
     * <p>
     * 1。电子文件平台印章  标识  FILE_PLAT_STAMP
     * 2。第三方提供        标识  CLIENT_PLAT_STAMP
     * 多个用逗号隔开（英文逗号）
     */
    @Column(name = "client_key_type")
    private String clientKeyType;


    /**
     * 客户端 key类型 印章来源的url
     */
    @Column(name = "url")
    private String url;


    /**
     * 接口调用权限 开关
     * 1 开启
     * 2 关闭
    @Column(name = "flag")
    private String flag;*/

    /**
     * 接口状态
     * 1 正常
     * 2 删除
     */
    private String status;

    /**
     * 创建时间
     */
    @Column(name="create_user")
    private String createUser;
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

    /**
     * 备用字段
     */
    @Column(name="spare1")
    private String spare1;


    /**
     * 备用字段
     */
    @Column(name="spare2")
    private String spare2;

    /**
     * 备用字段
     */
    @Column(name="spare3")
    private String spare3;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getClientKeyType() {
        return clientKeyType;
    }

    public void setClientKeyType(String clientKeyType) {
        this.clientKeyType = clientKeyType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSpare1() {
        return spare1;
    }

    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    public String getSpare2() {
        return spare2;
    }

    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }
}
