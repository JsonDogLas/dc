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
 * 签名文件表
 */
@Entity
@Table(name = "api_sign_file")
public class ApiSignFileDo implements Serializable {

    /**
     * 签名文件id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 签名文件id
     */
    @Column(name = "sign_file_id")
    private String signFileId;

    /**
     * 状态 1显示 2删除
     */
    @Column(name = "status")
    private String status;

    /**
     * 主题
     */
    @Column(name = "title")
    private String title;


    /**
     * 创建时间
     */
    @Column(name="create_time")
    private String createTime;

    /**
     * 创建人
     */
    @Column(name="create_user")
    private String createUser;

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

    public String getSignFileId() {
        return signFileId;
    }

    public void setSignFileId(String signFileId) {
        this.signFileId = signFileId;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
