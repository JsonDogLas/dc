package com.cb.platform.yq.base.permission.test;

import com.cb.platform.yq.base.interfaces.bean.permission.Permission;
import com.cb.platform.yq.base.interfaces.bean.role.Role;
import com.cb.platform.yq.base.interfaces.constant.InterfaceConstant;
import com.ceba.base.utils.IDSDateUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统权限 枚举
 * 一级id
 *     两位数
 * 二级id
 *     三位数
 * 三级id
 *     四位数
 * 四级级id
 *     五位数
 */
public enum  SystemPermissionEnum implements CodeGenerateInterface {
    ROLE_API("13","ROLE_API","云签API系统权限管理",PermissionTypeEnum.MODULE,"","云签API系统权限管理",ApplicationIdEnum.ADMIN),
    //用户管理
    ROLE_API_INTERFACE("000","INTERFACE","接口权限",PermissionTypeEnum.MENU,"","接口权限",ROLE_API),
    ROLE_API_OPERATE("001","OPERATE","页面操作",PermissionTypeEnum.MENU,"","页面操作",ROLE_API),
    ROLE_API_OPERATE_DATE("0000","DATE","日期章",PermissionTypeEnum.MENU,"","日期章",ROLE_API_OPERATE),
    ROLE_API_OPERATE_CODE("0001","CODE","二维码",PermissionTypeEnum.MENU,"","二维码",ROLE_API_OPERATE),
    ROLE_API_OPERATE_TITLE("0002","TITLE","自定义标题",PermissionTypeEnum.MENU,"","自定义标题",ROLE_API_OPERATE),
    ROLE_API_OPERATE_UPLOAD("0003","UPLOAD","上传印章",PermissionTypeEnum.MENU,"","上传印章",ROLE_API_OPERATE),
    ROLE_API_OPERATE_INFO("0004","INFO","获取签名信息接口是否有签章信息",PermissionTypeEnum.MENU,"","获取签名信息接口是否有签章信息",ROLE_API_OPERATE),
    ROLE_API_OPERATE_WRITE("0005","WRITE","添加批注",PermissionTypeEnum.BUTTON,"","添加批注",ROLE_API_OPERATE),
    ROLE_API_OPERATE_STAMP("0006","STAMP","无签章签名",PermissionTypeEnum.MENU,"","无签章签名",ROLE_API_OPERATE);


    private String id;
    private String pid;
    private String code;
    private String name;
    private PermissionTypeEnum permissionType;//权限类型
    private String permissionUrl;//权限URL
    private String description;//权限说明
    private CodeGenerateInterface parentCode;//父节点
    private ApplicationIdEnum applicationIdEnum;

    SystemPermissionEnum(String id, String code, String name, PermissionTypeEnum permissionType, String permissionUrl, String description, CodeGenerateInterface parentCode) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.permissionType = permissionType;
        this.permissionUrl = permissionUrl;
        this.description = description;
        this.parentCode = parentCode;
    }

    SystemPermissionEnum(String id, String code, String name, PermissionTypeEnum permissionType, String permissionUrl, String description,ApplicationIdEnum applicationIdEnum) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.permissionType = permissionType;
        this.permissionUrl = permissionUrl;
        this.description = description;
        this.parentCode = null;
        this.applicationIdEnum=applicationIdEnum;
    }

    SystemPermissionEnum(String id, String code, String name, PermissionTypeEnum permissionType, String permissionUrl, String description, CodeGenerateInterface parentCode,ApplicationIdEnum applicationIdEnum) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.permissionType = permissionType;
        this.permissionUrl = permissionUrl;
        this.description = description;
        this.parentCode = parentCode;
        this.applicationIdEnum=applicationIdEnum;
    }


    /**
     * 当前权限code
     *
     * @return
     */
    @Override
    public String code() {
        if(parentCode == null){
            return this.getCode();
        }
        return parentCode.code()+"_"+this.getCode();
    }

    /**
     * 父节点ID
     * @return
     */
    @Override
    public String pid() {
        if(parentCode == null){
            return InterfaceConstant.PERMISSION_ROOT_ID;
        }
        return parentCode.id();
    }

    /**
     * id
     * @return
     */
    @Override
    public String id() {
        if(parentCode == null){
            return this.getId();
        }
        return parentCode.id()+this.getId();
    }

    /**
     * 应用ID
     *
     * @return
     */
    @Override
    public String applicationId() {
        if(this.getApplicationIdEnum() != null){
            return this.getApplicationIdEnum().getId();
        }
        if(parentCode == null){
            return this.getApplicationIdEnum().getId();
        }else{
            return parentCode.applicationId();
        }
    }

    @Override
    public CodeGenerateInterface getCodeGenerateInterface() {
        return parentCode;
    }

    public ApplicationIdEnum getApplicationIdEnum() {
        return applicationIdEnum;
    }

    public void setApplicationIdEnum(ApplicationIdEnum applicationIdEnum) {
        this.applicationIdEnum = applicationIdEnum;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionTypeEnum getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionTypeEnum permissionType) {
        this.permissionType = permissionType;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CodeGenerateInterface getParentCode() {
        return parentCode;
    }

    public void setParentCode(CodeGenerateInterface parentCode) {
        this.parentCode = parentCode;
    }

    public static String sql(String tableName, Map<String,Object> paramMap){
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("INSERT INTO ");
        stringBuffer.append(tableName);
        stringBuffer.append("(");
        int count=paramMap.keySet().size();
        int index=0;
        for(String key:paramMap.keySet()){
            stringBuffer.append(key);
            index++;
            if(index != count){
                stringBuffer.append(",");
            }
        }
        index=0;
        stringBuffer.append(")");
        stringBuffer.append(" VALUES (");
        for(String key:paramMap.keySet()){
            Object value=paramMap.get(key);
            if(value == null){
                stringBuffer.append("null");
            }else if(value instanceof String){
                stringBuffer.append("'");
                stringBuffer.append(value);
                stringBuffer.append("'");
            }else{
                stringBuffer.append(value);
            }
            index++;
            if(index != count){
                stringBuffer.append(",");
            }
        }
        stringBuffer.append(");");
        return stringBuffer.toString();
    }

    public static Map<String,Object> toMap(SystemPermissionEnum systemPermissionEnum){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("id",systemPermissionEnum.id());
        map.put("pid",systemPermissionEnum.pid());
        map.put("name",systemPermissionEnum.getName());
        map.put("code",systemPermissionEnum.code());
        map.put("permission","ROLE_ADMIN");
        map.put("permission_type",systemPermissionEnum.getPermissionType().flag);
        map.put("permission_url",systemPermissionEnum.getPermissionUrl());
        map.put("company_id",InterfaceConstant.DEFAULT_YQ_API_COMPAYID);
        map.put("application_id",systemPermissionEnum.applicationId());
        map.put("description",systemPermissionEnum.getDescription());
        map.put("status","0");
        map.put("version","1.0");
        map.put("create_user_id",InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        map.put("create_time", IDSDateUtils.getNowTime(null));
        return map;
    }


    /**
     * 转换为bean
     * @param systemPermissionEnum
     * @return
     */
    public static Permission toBean(SystemPermissionEnum systemPermissionEnum){
        Permission permission = new Permission();
        permission.setId(systemPermissionEnum.id());
        permission.setPid(systemPermissionEnum.pid());
        permission.setName(systemPermissionEnum.getName());
        permission.setCode(systemPermissionEnum.code());
        permission.setPermission("ROLE_ADMIN");
        permission.setPermissionType(systemPermissionEnum.getPermissionType().flag);
        permission.setPermissionUrl(systemPermissionEnum.getPermissionUrl());
        permission.setCompanyId("");
        permission.setApplicationId(systemPermissionEnum.applicationId());
        permission.setDescription(systemPermissionEnum.getDescription());
        permission.setCreateUserId(InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        permission.setCreateTime(IDSDateUtils.getNowTime(null));
        permission.setStatus("0");
        permission.setVersion("232");
        return permission;
    }

    public static Map<String,Object> insertRoleSql(Role role){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("id",role.getId());
        map.put("pid",role.getPid());
        map.put("name",role.getName());
        map.put("code",role.getCode());
        map.put("company_id",role.getCompanyId());
        map.put("application_id",role.getApplicationId());
        map.put("description",role.getDescription());
        map.put("create_user_id",role.getCreateUserId());
        map.put("create_time",IDSDateUtils.getNowTime(null));
        map.put("update_user_id",role.getUpdateUserId());
        map.put("update_time",IDSDateUtils.getNowTime(null));
        map.put("status","1");
        map.put("version","0");
        return map;
    }

    public static Map<String,Object> insertRolePermission(String roleId,String permisssion_id,String company_id){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("role_id",roleId);
        map.put("permission_id",permisssion_id);
        map.put("company_id",company_id);
        map.put("create_user_id",InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        map.put("create_time",IDSDateUtils.getNowTime(null));
        map.put("update_user_id",InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        map.put("update_time",IDSDateUtils.getNowTime(null));
        map.put("status","1");
        map.put("version","0");
        return map;
    }

    /**
     * 默认的权限
     */
    public static void defaultRolePermission(Role role,List<SystemPermissionEnum> list){
        System.out.println(SystemPermissionEnum.sql("u_role",insertRoleSql(role)));

        for(SystemPermissionEnum systemPermissionEnum:list) {
            Map<String,Object> toMap=SystemPermissionEnum.insertRolePermission(role.getId(),systemPermissionEnum.id(),role.getCompanyId());
            System.out.println(SystemPermissionEnum.sql("u_role_permission",toMap));
        }

        /*for(SystemPermissionEnum systemPermissionEnum:list){
            Map<String,Object> toMap=SystemPermissionEnum.toMap(systemPermissionEnum);
            System.out.println(SystemPermissionEnum.sql("u_permission",toMap));
        }*/
    }

    public static void defaultAdminPermission(){
        //用户默认权限
        Role role=new Role();
        role.setId(InterfaceConstant.DEFAULT_ROLE_ID);
        role.setPid(InterfaceConstant.PERMISSION_ROOT_ID);
        role.setName("云签API默认权限");
        role.setCode("DEFAULT_YQ_API_PERMISSION");
        role.setCompanyId(InterfaceConstant.DEFAULT_YQ_API_COMPAYID);
        role.setApplicationId("5");
        role.setDescription("role");
        role.setCreateUserId(InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        role.setUpdateUserId(InterfaceConstant.SYSTEM_SUPER_ADMIN_USER_ID);
        role.setUpdateTime(IDSDateUtils.getNowTime(null));
        role.setCreateTime(IDSDateUtils.getNowTime(null));
        role.setStatus("1");

        List<SystemPermissionEnum> systemPermissionEnumList=new ArrayList<>();
        for(SystemPermissionEnum systemPermissionEnum:SystemPermissionEnum.values()){
            systemPermissionEnumList.add(systemPermissionEnum);
        }
        defaultRolePermission(role,systemPermissionEnumList);
    }


    public static void main(String[] agrs){
        defaultAdminPermission();
        SystemPermissionEnum[] systemPermissionEnumArray=SystemPermissionEnum.values();
        for(SystemPermissionEnum systemPermissionEnum:systemPermissionEnumArray){
            Map<String,Object> toMap=SystemPermissionEnum.toMap(systemPermissionEnum);
            System.out.println(SystemPermissionEnum.sql("u_permission",toMap));
        }

    }

}
