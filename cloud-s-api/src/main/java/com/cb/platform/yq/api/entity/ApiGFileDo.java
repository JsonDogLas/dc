package com.cb.platform.yq.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 文件
 * @author
 */
@Entity
@Table(name = "api_g_file")
public class ApiGFileDo implements Serializable {
    /**
     * 签名文件id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件后缀
     */
    @Column(name = "ext_name")
    private String extName;


    /**
     * 文件虚拟目录
     */
    @Column(name = "virtual_path")
    private String virtualPath;

    /**
     * 文件大小
     */
    @Column(name = "size")
    private String size;

    /**
     * 文件备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 文件类型
     *
     * 9 API系统缓存文件
     * 8 API系统ftp文件
     * 7 API签章文件 FileTypeEnum
     */
    @Column(name = "type")
    private String type;


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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
