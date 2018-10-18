package com.cb.platform.yq.base.interfaces.bean.role;

import com.cb.platform.yq.base.interfaces.bean.RequestParam;
import com.cb.platform.yq.base.interfaces.bean.permission.Permission;
import com.ceba.base.web.response.IResult;
import java.util.List;

/**
 * 角色
 */
public class Role extends RequestParam {

    private String id;
    private String pid;//父节点
    private String name;//姓名
    private String code;//编码
    private String companyId;//公司ID
    private String applicationId;//
    private String description;//
    private String createUserId;//创建用户
    private String updateUserId;//更新用户
    private String updateTime;//更新时间
    private String createTime;//创建时间
    private String status;//状态 0正常 1删除 2锁定 3未激活

    private List<String> permissionIds;//权限id集合
    private String appId;
    private List<Permission> permissions;//权限

    //------------根据权限ID查询用户专用-------------
    private String permissionId;//权限id
    //------------根据权限批量删除角色专用-------------
    private List<String> idList;//角色id

    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public Role() {
        super();
        this.setResult(true);
    }

    public Role(IResult iResult){
        this.setResult(iResult.getResult());
        this.setMessage(iResult.getMessage());
        this.setResultCode(iResult.getResultCode());
        this.setData(iResult.getData());
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public List<String> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
