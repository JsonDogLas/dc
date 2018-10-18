package com.cb.platform.yq.base.permission.test;

/**
 * 应用枚举ID
 */
public enum ApplicationIdEnum {
    INDEX("2","门户"),
    CERT("3","数字证书平台"),
    CLOUD("4","云签平台"),
    ADMIN("5","后台管理平台"),
   // SYS("6","系统配置"),
    VERIFY("7","验证"),
    PROPERTY("8","财务通");

    private String id;
    private String name;

    ApplicationIdEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
