package com.cb.platform.yq.base.interfaces.bean.permission;

import com.cb.platform.yq.base.interfaces.bean.RequestParam;
import com.ceba.base.web.response.IResult;
import org.apache.commons.lang.StringUtils;

import java.util.List;


/**
 * 权限
 */
public class Permission extends RequestParam {


    private String id;
    private String pid;//父节点
    private String name;//姓名
    private String code;//编号
    private String permission;//权限
    private String permissionType;//
    private String permissionUrl;
    private String companyId;
    private String applicationId;
    private String description;
    private String createUserId;
    private String createTime;
    private String updateUserId;
    private String updateTime;

    private String status;//状态 0正常 1删除 2锁定
    private String version;//版本

    //-------------以下非数据库字段----------------
    private List<Permission> permissionList;//子节点集合

    //------------根据权限批量删除用户专用-------------
    private List<String> idList;//权限id
    private boolean open=false;//false 关闭 true 打开

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Permission() {
        super();
        this.setResult(true);
    }

    public Permission(IResult iResult){
        this.setResult(iResult.getResult());
        this.setMessage(iResult.getMessage());
        this.setResultCode(iResult.getResultCode());
        this.setData(iResult.getData());
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getNameStr(){
        if(StringUtils.isNotEmpty(this.name) && this.name.length()>6){
            return this.name.substring(0,4)+"...";
        }
        return this.name;
    }

}
