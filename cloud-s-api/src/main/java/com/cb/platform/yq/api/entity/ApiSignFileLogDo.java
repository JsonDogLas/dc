package com.cb.platform.yq.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 签名日志
 */
@Entity
@Table(name = "api_sign_file_log")
public class ApiSignFileLogDo implements Serializable {

    /**
     * 文件id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 签名文件Id api_sign_file
     */
    @Column(name = "sign_file_id")
    private String signFileId;

    /**
     * 原始文件 api_g_file
     */
    @Column(name = "original_file_id")
    private String originalFileId;

    /**
     * 签名后的文件ID api_g_file
     */
    @Column(name = "after_sign_file_id")
    private String afterSignFileId;

    /**
     * 签名类型
     * 1修订
     * 2验证
     * 3普通签
     */
    @Column(name = "sign_type")
    private String signType;


    /**
     * keyId
     */
    @Column(name="key_id")
    private String keyId;

    /**
     * 签章文件ID api_g_file
     */
    private String stampFileIds;


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

    public String getOriginalFileId() {
        return originalFileId;
    }

    public void setOriginalFileId(String originalFileId) {
        this.originalFileId = originalFileId;
    }

    public String getAfterSignFileId() {
        return afterSignFileId;
    }

    public void setAfterSignFileId(String afterSignFileId) {
        this.afterSignFileId = afterSignFileId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getStampFileIds() {
        return stampFileIds;
    }

    public void setStampFileIds(String stampFileIds) {
        this.stampFileIds = stampFileIds;
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
